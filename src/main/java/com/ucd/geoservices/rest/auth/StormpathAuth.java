package com.ucd.geoservices.rest.auth;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;

import jersey.repackaged.com.google.common.collect.Maps;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.api.ApiAuthenticationResult;
import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.api.ApiKeyList;
import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.authc.UsernamePasswordRequest;
import com.stormpath.sdk.http.HttpMethod;
import com.stormpath.sdk.http.HttpRequest;
import com.stormpath.sdk.http.HttpRequests;
import com.stormpath.sdk.oauth.AccessTokenResult;
import com.stormpath.sdk.resource.ResourceException;
import com.ucd.geoservices.model.User;
import com.ucd.geoservices.transformer.UserTransformer;

@Component
public class StormpathAuth implements AuthManager {

	private final UserTransformer userTransformer;
	private final StormpathProvider stormpathProvider;

	@Autowired
	public StormpathAuth(UserTransformer userTransformer,
			StormpathProvider stormpathProvider) {
		this.userTransformer = userTransformer;
		this.stormpathProvider = stormpathProvider;

	}

	@Override
	public User create(User user) {
		Account account = stormpathProvider.getClient().instantiate(
				Account.class);

		// Set the account properties
		account.setGivenName(user.getUsername());
		account.setSurname(user.getUsername());
		account.setUsername(user.getUsername()); // optional, defaults to email
													// if unset
		account.setEmail(user.getEmail());
		account.setPassword(user.getPassword());

		// Create the account using the existing Application object
		account = stormpathProvider.getApplication().createAccount(account);

		return userTransformer.fromStormpathUser(account);
	}

	@Override
	public String getRefreshToken(String email, String password) {
		String securityToken = "";
		try {
			AuthenticationRequest authReq = new UsernamePasswordRequest(email,
					password);
			Account account = stormpathProvider.getApplication()
					.authenticateAccount(authReq).getAccount();

			// create a new api key and set it store it in a cookie
			ApiKey apiKey = account.createApiKey();
			String concat = apiKey.getId() + ":" + apiKey.getSecret();

			securityToken = Base64.encodeBase64String(concat.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new WebApplicationException("Invalid email or password.",
					Status.UNAUTHORIZED);
		}

		return securityToken;

	}

	@Override
	public String generateAccessToken(String apiKeyToken) {
		// Set up HTTP Headers
		Map<String, String[]> headers = Maps.newHashMap();
		headers.put(HttpHeaders.AUTHORIZATION, new String[] { "Basic "
				+ apiKeyToken });
		headers.put(HttpHeaders.CONTENT_TYPE,
				new String[] { "application/x-www-form-urlencoded" });
		headers.put(HttpHeaders.ACCEPT, new String[] { "application/json" });
		String[] param = { "client_credentials" };
		Map<String, String[]> params = Maps.newHashMap();
		params.put("grant_type", param);
		HttpRequest requestCustom = HttpRequests.method(HttpMethod.POST)
				.headers(headers).parameters(params).build();
		AccessTokenResult resultToken = (AccessTokenResult) stormpathProvider
				.getApplication().authenticateOauthRequest(requestCustom)
				.withTtl(60 * 15).execute();
		return resultToken.getTokenResponse().toJson();
	}

	@Override
	public void authenticateRequest(HttpServletRequest request) {
		try {
			ApiAuthenticationResult result = stormpathProvider.getApplication()
					.authenticateApiRequest(request);
			Account account = result.getAccount();
			request.getSession().setAttribute("account", account);
		} catch (ResourceException ex) {
			throw new WebApplicationException("Invalid token.",
					Status.UNAUTHORIZED);
		}

	}

	@Override
	public User getUser(Object accountObject) {
		return userTransformer.fromStormpathUser((Account) accountObject);
	}

	@Override
	public void deleteTokens(Object accountObject) {
		Account account = (Account) accountObject;
		ApiKeyList apiKeys = account.getApiKeys();
		for (ApiKey key : apiKeys) {
			key.delete();
		}
	}

	@Override
	public void deleteUser(Object accountObject) {
		((Account) accountObject).delete();
	}
}
