package com.ucd.geoservices.rest.auth;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import lombok.Getter;

import org.springframework.stereotype.Component;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.api.ApiKeys;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationList;
import com.stormpath.sdk.application.Applications;
import com.stormpath.sdk.cache.Caches;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;

@Getter
@Component
public class StormpathProvider {

	private final Client client;
	private final Application application;

	public StormpathProvider() {

		String stormpathAppId = Optional.ofNullable(
				System.getenv("stormpath-application-id")).orElse(
				System.getProperty("stormpath-application-id"));
		String stormpathSecretId = Optional.ofNullable(
				System.getenv("stormpath-secret-id")).orElse(
				System.getProperty("stormpath-secret-id"));

		ApiKey apiKey = ApiKeys.builder().setId(stormpathAppId)
				.setSecret(stormpathSecretId).build();
		this.client = Clients
				.builder()
				.setApiKey(apiKey)
				.setCacheManager(
						Caches.newCacheManager()
								.withDefaultTimeToLive(1, TimeUnit.DAYS)
								.withDefaultTimeToIdle(2, TimeUnit.HOURS)
								.withCache(
										Caches.forResource(Account.class)
												// Account-specific cache
												// settings
												.withTimeToLive(1,
														TimeUnit.HOURS)
												.withTimeToIdle(30,
														TimeUnit.MINUTES))
								.build()).build();

		ApplicationList applications = client.getCurrentTenant()
				.getApplications(
						Applications.where(Applications.name().eqIgnoreCase(System.getProperty("appname"))));

		this.application = applications.iterator().next();
	}

}
