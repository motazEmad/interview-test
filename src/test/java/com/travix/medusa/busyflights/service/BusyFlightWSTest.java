package com.travix.medusa.busyflights.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import com.travix.medusa.busyflights.service.crazyair.CrazyAirService;
import com.travix.medusa.busyflights.service.crazyair.CrazyAirServiceWrapper;
import com.travix.medusa.busyflights.service.toughJet.ToughJetService;
import com.travix.medusa.busyflights.service.toughJet.ToughJetServiceWrapper;
import org.hamcrest.Matchers;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class BusyFlightWSTest {

	private static final DateTimeFormatter CRAZYAIR_ISO_LOCAL_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	private static final DateTimeFormatter TOUGHJET_ISO_LOCAL_DATE_TIME = DateTimeFormatter.ISO_INSTANT;

	@Mock
	private CrazyAirService crazyAirService;

	@Mock
	private ToughJetService toughJetService;
	
	@InjectMocks
	@Autowired
	private CrazyAirServiceWrapper crazySerivceWrapper;
	
	@InjectMocks
	@Autowired
	private ToughJetServiceWrapper toughJetWrapper;

	@Autowired
	MockMvc mvc;

	@Test
	public void shouldReturnAgrregatedResponse() {
		CrazyAirResponse crazyAirResponse = new CrazyAirResponse();
		crazyAirResponse.setAirline("Fly World Airline");
		LocalDateTime date = LocalDateTime.now();
		String text = date.format(CRAZYAIR_ISO_LOCAL_DATE_TIME);
		crazyAirResponse.setArrivalDate(text);
		crazyAirResponse.setDepartureDate(text);
		crazyAirResponse.setCabinclass("A");
		crazyAirResponse.setDepartureAirportCode("DXB");
		crazyAirResponse.setDestinationAirportCode("ABC");
		crazyAirResponse.setPrice(1000);
		Mockito.when(crazyAirService.search(Mockito.any(CrazyAirRequest.class)))
				.thenReturn(ResponseEntity.ok(crazyAirResponse));
		
		ToughJetResponse toughJetResponse = new ToughJetResponse();
		toughJetResponse.setArrivalAirportName("XYZ");
		toughJetResponse.setDepartureAirportName("DXB");
		toughJetResponse.setBasePrice(1000);
		toughJetResponse.setDiscount(10);
		toughJetResponse.setTax(50);
		toughJetResponse.setCarrier("Amazying Flight AirLine");
		ZonedDateTime zoneDate = ZonedDateTime.now();
		String toughJetTime = zoneDate.format(TOUGHJET_ISO_LOCAL_DATE_TIME);
		toughJetResponse.setInboundDateTime(toughJetTime);
		toughJetResponse.setOutboundDateTime(toughJetTime);
		
		Mockito.when(toughJetService.search(Mockito.any(ToughJetRequest.class)))
				.thenReturn(ResponseEntity.ok(toughJetResponse));

		try {
			BusyFlightsRequest busyFlightsRequest = new BusyFlightsRequest();
			mvc.perform(MockMvcRequestBuilders.post("/busyFlight/searchFlights/")
					.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
					.content(new ObjectMapper().writeValueAsString(busyFlightsRequest)))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

}
