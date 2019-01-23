package com.travix.medusa.busyflights.service;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.exceptions.BusyFlightsException;

public interface ServiceProviderWrapper {
	
	BusyFlightsResponse searchFlights(BusyFlightsRequest busyFlightsRequest) throws BusyFlightsException;
}
