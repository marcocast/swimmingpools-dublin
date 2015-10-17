package com.ucd.geoservices.app;

import com.aol.micro.server.MicroserverApp;
import com.aol.micro.server.rest.client.nio.RestClient;

public class MainTest {

	private MicroserverApp server;
	private final RestClient<String> rest = new RestClient(1000, 1000).withAccept("application/xml");

}
