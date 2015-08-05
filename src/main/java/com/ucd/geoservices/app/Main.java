package com.ucd.geoservices.app;

import com.aol.micro.server.MicroserverApp;
import com.aol.micro.server.config.Microserver;

@Microserver(basePackages = { "com.ucd.geoservices" })
public class Main {

	public static final GeoservicesModule GEOSERVICES_MODULE = new GeoservicesModule(
			"geoservices/resources");

	public static void main(String[] args) {

		new MicroserverApp(GEOSERVICES_MODULE).start();

	}

}
