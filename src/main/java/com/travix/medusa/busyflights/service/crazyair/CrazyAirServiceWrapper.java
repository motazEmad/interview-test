package com.travix.medusa.busyflights.service.crazyair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.exceptions.BusyFlightsException;
import com.travix.medusa.busyflights.service.ServiceAdapter;
import com.travix.medusa.busyflights.service.ServiceProviderWrapper;

/**
 *  this is a wrapper for CrazyAir Web service handle the call
 *  for the web service 
 *
 */
@Component
public class CrazyAirServiceWrapper implements ServiceProviderWrapper, ServiceAdapter<CrazyAirRequest, CrazyAirResponse> {

	private static final DateTimeFormatter CRAZYAIR_ISO_LOCAL_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	private static final DateTimeFormatter BUSYFLIGHT_ISO_DATE_TIME = DateTimeFormatter.ISO_DATE_TIME;
	private static final String CRAZYAIR_SUPPLIER = "CrazyAir";
	
	@Autowired
	private CrazyAirService service;
	
	@Override
	public BusyFlightsResponse searchFlights(BusyFlightsRequest busyFlightsRequest) throws BusyFlightsException {
		CrazyAirRequest crazyAirRequest = convertRequest(busyFlightsRequest);
		ResponseEntity<CrazyAirResponse> response = service.search(crazyAirRequest);
		if(response.getStatusCode().is2xxSuccessful()) {
			return convertSuccessResponse(response.getBody());
		}
		return convertFailureResponse(response);
	}

	public CrazyAirRequest convertRequest(BusyFlightsRequest busyFlightsRequest) {
		CrazyAirRequest crazyAirRequest = new CrazyAirRequest();
		BeanUtils.copyProperties(busyFlightsRequest, crazyAirRequest);
		crazyAirRequest.setPassengerCount(busyFlightsRequest.getNumberOfPassengers());
		return crazyAirRequest;
	}

	public BusyFlightsResponse convertSuccessResponse(CrazyAirResponse body) {
		BusyFlightsResponse busyFlightsResponse = new BusyFlightsResponse();
		BeanUtils.copyProperties(body, busyFlightsResponse);
		busyFlightsResponse.setFare(body.getPrice());
		busyFlightsResponse.setSupplier(CRAZYAIR_SUPPLIER);
		busyFlightsResponse.setDepartureDate(BUSYFLIGHT_ISO_DATE_TIME.format(LocalDateTime.parse(body.getDepartureDate(), CRAZYAIR_ISO_LOCAL_DATE_TIME)));
		busyFlightsResponse.setArrivalDate(BUSYFLIGHT_ISO_DATE_TIME.format(LocalDateTime.parse(body.getArrivalDate(), CRAZYAIR_ISO_LOCAL_DATE_TIME)));
		return busyFlightsResponse;
	}
	
	/**
	 * to handle different error codes returns from the service
	 * @param response
	 * @return
	 */
	public BusyFlightsResponse convertFailureResponse(ResponseEntity<CrazyAirResponse> response) throws BusyFlightsException {
		throw new BusyFlightsException("failed to call CrazyAir flights with errorcode: " + response.getStatusCodeValue());
	}
}
