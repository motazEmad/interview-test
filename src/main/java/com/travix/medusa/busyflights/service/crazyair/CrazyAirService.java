package com.travix.medusa.busyflights.service.crazyair;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;

@Component
public class CrazyAirService {

	/*
	 * call CrazyAir Web service to search for available flights 
	 * that match the search inputs
	 */
	public ResponseEntity<CrazyAirResponse> search(CrazyAirRequest crazyAirRequest) {
		return null;
	}

}
