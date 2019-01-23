package com.travix.medusa.busyflights.service.toughJet;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import com.travix.medusa.busyflights.exceptions.BusyFlightsException;
import com.travix.medusa.busyflights.service.ServiceAdapter;
import com.travix.medusa.busyflights.service.ServiceProviderWrapper;

@Component
public class ToughJetServiceWrapper implements ServiceProviderWrapper, ServiceAdapter<ToughJetRequest, ToughJetResponse> {

	private static final DateTimeFormatter TOUGHJET_ISO_LOCAL_DATE_TIME = DateTimeFormatter.ISO_INSTANT;
	private static final DateTimeFormatter BUSYFLIGHT_ISO_DATE_TIME = DateTimeFormatter.ISO_DATE_TIME;
	private static final String TOUGHJET_SUPPLIER = "ToughJet";
	
	@Autowired
	private ToughJetService service;
	
	@Override
	public BusyFlightsResponse searchFlights(BusyFlightsRequest busyFlightsRequest) throws BusyFlightsException {
		ToughJetRequest toughJetRequest = convertRequest(busyFlightsRequest);
		ResponseEntity<ToughJetResponse> response = service.search(toughJetRequest);
		if(response.getStatusCode().is2xxSuccessful()) {
			return convertSuccessResponse(response.getBody());
		}
		return convertFailureResponse(response);
	}

	@Override
	public ToughJetRequest convertRequest(BusyFlightsRequest busyFlightsRequest) {
		ToughJetRequest toughJetRequest = new ToughJetRequest();
		toughJetRequest.setFrom(busyFlightsRequest.getOrigin());
		toughJetRequest.setTo(busyFlightsRequest.getDestination());
		toughJetRequest.setOutboundDate(busyFlightsRequest.getDepartureDate());
		toughJetRequest.setInboundDate(busyFlightsRequest.getReturnDate());
		toughJetRequest.setNumberOfAdults(busyFlightsRequest.getNumberOfPassengers());
		return toughJetRequest;
	}

	@Override
	public BusyFlightsResponse convertSuccessResponse(ToughJetResponse body) {
		BusyFlightsResponse busyFlightsResponse = new BusyFlightsResponse();
		busyFlightsResponse.setAirline(body.getCarrier());
		busyFlightsResponse.setSupplier(TOUGHJET_SUPPLIER);
		double totalprice = body.getBasePrice() + body.getTax();
		busyFlightsResponse.setFare(
				body.getDiscount() != 0 ? (totalprice - (totalprice * body.getDiscount() / 100)) : totalprice);
		busyFlightsResponse.setDepartureAirportCode(body.getDepartureAirportName());
		busyFlightsResponse.setDestinationAirportCode(body.getArrivalAirportName());
		busyFlightsResponse.setDepartureDate(BUSYFLIGHT_ISO_DATE_TIME.format(LocalDateTime.ofInstant(Instant.parse(body.getOutboundDateTime()), ZoneOffset.UTC)));
		busyFlightsResponse.setArrivalDate(BUSYFLIGHT_ISO_DATE_TIME.format(LocalDateTime.ofInstant(Instant.parse(body.getInboundDateTime()),ZoneOffset.UTC)));
		return busyFlightsResponse;
	}

	@Override
	public BusyFlightsResponse convertFailureResponse(ResponseEntity<ToughJetResponse> response)
			throws BusyFlightsException {
		throw new BusyFlightsException("failed to call ToughJet with error code: "+ response.getStatusCodeValue());
	}

	
}
