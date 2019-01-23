package com.travix.medusa.busyflights.service;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.exceptions.BusyFlightsException;
import com.travix.medusa.busyflights.service.toughJet.ToughJetServiceWrapper;

@RestController
@RequestMapping("/busyFlight")
public class BusyFlightWS {
	
	private static final Logger logger = LoggerFactory.getLogger(BusyFlightWS.class);
	
	@Autowired
	private List<? extends ServiceProviderWrapper> serviceProvidersList;
	
	@PostMapping("/searchFlights")
	public ResponseEntity<List<BusyFlightsResponse>> search(@RequestBody BusyFlightsRequest request) {
		List<BusyFlightsResponse> result = serviceProvidersList.stream().map(serviceProvider -> {
			try {
				return serviceProvider.searchFlights(request);
			} catch (BusyFlightsException e) {
				logger.error("failed to search for service Provider " + serviceProvider.getClass(), e);
			}
			return null;
		}).collect(Collectors.toList());
		return ok(result);
	}
}
