package com.oyster.card.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.oyster.card.managers.FareManager;
import com.oyster.card.managers.StationManager;
import com.oyster.card.managers.TravelCardManager;
import com.oyster.card.service.client.OysterCardAppApplication;
import com.oyster.card.services.OysterCardService;
import com.oyster.card.services.impl.OysterCardServiceImpl;
import com.oyster.card.utils.OysterCardUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OysterCardAppApplication.class}, 
	properties = {"spring.main.allow-bean-definition-overriding=true"})
public class TestOysterWebController {
	
	@TestConfiguration
    static class OysterCardServiceTestContextConfiguration {
  
        @Bean
        public StationManager stationManager() {
            return new StationManager();
        }
        
        @Bean
        public FareManager fareManager() {
            return new FareManager();
        }
        
        @Bean
        public TravelCardManager travelCardManager() {
            return new TravelCardManager();
        }
        
        @Bean
        public OysterCardUtils oysterCardUtils() {
            return new OysterCardUtils();
        }
        
        @Bean
        public OysterCardService oysterCardService() {
            return new OysterCardServiceImpl();
        }
    }
	
	private MockMvc mockMvc;
	
	@Autowired
	private OysterWebController oysterWebController;
	
	@Before
	public void setup() {
		this.mockMvc = standaloneSetup(this.oysterWebController).build();
	}
	
	@Test
    public void testOysterCard1() throws Exception {
        this.mockMvc.perform(get("/oyster/test-oyster-service")).andExpect(status().isOk())
                .andExpect(content().string(containsString("all_station_details")));
    }
	
	@Test
    public void testOysterCard2() throws Exception {
        this.mockMvc.perform(get("/oyster/test-oyster-service")).andExpect(status().isOk())
                .andExpect(content().string(containsString("all_fare_details")));
    }
	
	@Test
    public void testOysterCard3() throws Exception {
        this.mockMvc.perform(get("/oyster/test-oyster-service")).andExpect(status().isOk())
                .andExpect(content().string(containsString("trip_details")));
    }
	
	@Test
    public void testOysterCard4() throws Exception {
        this.mockMvc.perform(get("/oyster/test-oyster-service")).andExpect(status().isOk())
                .andExpect(content().string(containsString("all_trip_details")));
    }
	
	

}
