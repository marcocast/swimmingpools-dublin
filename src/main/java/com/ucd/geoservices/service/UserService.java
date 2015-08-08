package com.ucd.geoservices.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ucd.geoservices.model.User;
import com.ucd.geoservices.rest.auth.AuthDecoder;
import com.ucd.geoservices.rest.auth.AuthManager;

@Component
public class UserService {

	@Autowired
	private AuthManager authManager;

	public User create(User user) {
		return authManager.create(user);
	}

	public String getRefreshToken(String basicAuthEncoded) {
		String[] lap = AuthDecoder.decodeBasic(basicAuthEncoded);
		return authManager.getRefreshToken(lap[0], lap[1]);
	}

	public String getRefreshToken(String email, String password) {
		return authManager.getRefreshToken(email, password);
	}

	public String getAccessToken(String apiKeyToken) {
		return authManager.generateAccessToken(apiKeyToken);

	}

	public User getUser(HttpServletRequest request) {
		return authManager
				.getUser(request.getSession().getAttribute("account"));
	}

	public void deleteUser(HttpServletRequest request) {
		authManager.deleteUser(request.getSession().getAttribute("account"));
	}

	public void deleteTokens(HttpServletRequest request) {
		authManager.deleteTokens(request.getSession().getAttribute("account"));

	}
}
