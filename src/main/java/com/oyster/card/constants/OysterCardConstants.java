package com.oyster.card.constants;

import java.util.regex.Pattern;
/**
 * @author Shahnawaz
 */
public interface OysterCardConstants {
	
	 String ZONES_AND_FARES_TXT = "zonesAndFares.txt";
	 String STATIONS_AND_ZONES_TXT = "stationsAndZones.txt";
	 String TRAVEL_DATA_TXT = "travelData.txt";
	 Pattern COMPILED_TUBE_JOURNEY_PATTERN = 
		Pattern.compile("^Tube ", Pattern.CASE_INSENSITIVE);
	 Pattern COMPILED_BUS_JOURNEY_PATTERN = 
		Pattern.compile("^([1-9][0-9]*) bus from ", Pattern.CASE_INSENSITIVE);
	 String SUCCESS = "success";
	 String EMPTY_SPACES = "";
	 int UPPER_LIMIT_BOUND = 4;
	String ERROR = "error";
}
