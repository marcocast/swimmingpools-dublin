package com.ucd.geoservices.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ucd.geoservices.rest.auth.AuthManager;
import com.ucd.geoservices.rest.auth.StormpathAuth;
import com.ucd.geoservices.rest.auth.StormpathProvider;
import com.ucd.geoservices.transformer.UserTransformer;

@Configuration
public class ConfigureBeans {

	@Autowired
	private UserTransformer userTransformer;

	@Autowired
	private StormpathProvider stormpathProvider;

	@Bean
	public AuthManager authManager() {
		return new StormpathAuth(userTransformer, stormpathProvider);
	}

}
