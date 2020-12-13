package com.oyster.card.tests.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.oyster.card.controller.TestOysterWebController;
import com.oyster.card.managers.FareManagerTest;
import com.oyster.card.managers.StationManagerTest;
import com.oyster.card.managers.TravelCardManagerTest;
import com.oyster.card.services.OysterCardServiceTest;
import com.oyster.card.utils.TestOysterCardUtils;

@RunWith(Suite.class)
@SuiteClasses({OysterCardServiceTest.class, 
			   StationManagerTest.class,
			   FareManagerTest.class,
			   TravelCardManagerTest.class,
			   TestOysterCardUtils.class,
			   TestOysterWebController.class})
public class OysterCardTestSuite {

}
