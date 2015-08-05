package com.ucd.geoservices.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.aol.cyclops.streams.Pair;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;

@Component
public class GeocoderService {

	public Optional<Pair<Double, Double>> getLongLat(String fullAddress) {

		Optional<Pair<Double, Double>> longLat = Optional.empty();

		final Geocoder geocoder = new Geocoder();
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder()
				.setAddress(fullAddress.replaceAll(" ", "+")).setLanguage("en")
				.getGeocoderRequest();
		GeocodeResponse geocoderResponse;
		try {
			geocoderResponse = geocoder.geocode(geocoderRequest);

			double longitude = geocoderResponse.getResults().get(0)
					.getGeometry().getLocation().getLng().doubleValue();

			double latitudine = geocoderResponse.getResults().get(0)
					.getGeometry().getLocation().getLat().doubleValue();

			longLat = longLat.of(new Pair(longitude, latitudine));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return longLat;
	}
}
