package com.travix.medusa.busyflights.service;

import org.springframework.http.ResponseEntity;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.exceptions.BusyFlightsException;

public interface ServiceAdapter<T, R> {

	T convertRequest(BusyFlightsRequest busyFlightsRequest);
	BusyFlightsResponse convertSuccessResponse(R body);
	BusyFlightsResponse convertFailureResponse(ResponseEntity<R> response) throws BusyFlightsException;
}
