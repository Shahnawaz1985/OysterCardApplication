package com.oyster.card.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oyster.card.beans.CommutationMode;
import com.oyster.card.beans.Station;
import com.oyster.card.beans.Trip;
import com.oyster.card.constants.OysterCardConstants;
import com.oyster.card.exception.GenericException;
import com.oyster.card.exception.IllegalParameterException;
import com.oyster.card.managers.FareManager;
import com.oyster.card.managers.StationManager;

/**
 * 
 * @author Shahnawaz
 *
 */
@Service
public class OysterCardUtils {

	private static Logger logger = Logger.getLogger(OysterCardUtils.class);

	@Autowired
	private StationManager stationManager;

	@Autowired
	private FareManager fareManager;

	/**
	 * @param stationsAndZonesFileResource
	 * @return
	 */
	public Map<String, List<Integer>> createStationNameWithZoneIds(String stationsAndZonesFileResource) {
		Map<String, List<Integer>> stationNameWithZoneIds = new HashMap<>();
		Properties properties = createPropsFromClassPathResource(stationsAndZonesFileResource);
		Enumeration<Object> enumKeys = properties.keys();
		while (enumKeys.hasMoreElements()) {
			String stationName = (String) enumKeys.nextElement();
			String value = properties.getProperty(stationName);
			StringTokenizer tokens = new StringTokenizer(value, ",");
			int counter = tokens.countTokens();
			List<Integer> zoneList = new ArrayList<>();
			for (int i = 0; i < counter; i++) {
				String element = tokens.nextToken();
				zoneList.add(Integer.valueOf(element));
			}
			stationNameWithZoneIds.put(stationName, zoneList);
		}
		return stationNameWithZoneIds;
	}

	/**
	 * @param zonesAndFaresFileResource
	 * @return
	 */
	public Map<String, String> createZonesFaresAndCommutationModes(String zonesAndFaresFileResource) {
		Map<String, String> zonesFaresAndCommutationModes = new HashMap<>();
		Properties properties = createPropsFromClassPathResource(zonesAndFaresFileResource);
		Enumeration<Object> enumKeys = properties.keys();
		while (enumKeys.hasMoreElements()) {
			String key = (String) enumKeys.nextElement();
			String value = properties.getProperty(key);
			StringTokenizer zoneGroups = new StringTokenizer(key, ";");
			int fareGroupsCount = zoneGroups.countTokens();
			for (int i = 0; i < fareGroupsCount; i++) {
				String zoneGroup = zoneGroups.nextToken();
				zonesFaresAndCommutationModes.put(zoneGroup, value);
			}
		}
		return zonesFaresAndCommutationModes;
	}

	/**
	 * @param travelDataFilePath
	 * @return
	 * @throws GenericException
	 * @throws IllegalParameterException
	 */
	public List<Trip> extractTripsFrom(String travelDataFilePath) throws GenericException, IllegalParameterException {
		InputStreamReader inputStreamReader = createIsReader(travelDataFilePath);
		BufferedReader bufferedReader = null;
		if(null != inputStreamReader) {
			bufferedReader = new BufferedReader(inputStreamReader);
		}
		List<Trip> trips = new ArrayList<>();
		String journeyDetails = null;
		try {
			while (null != bufferedReader &&  (journeyDetails = bufferedReader.readLine()) != null) {
				Trip trip;
				Matcher tubeJourneymatcher = OysterCardConstants.COMPILED_TUBE_JOURNEY_PATTERN.matcher(journeyDetails);
				Matcher busJourneymatcher = OysterCardConstants.COMPILED_BUS_JOURNEY_PATTERN.matcher(journeyDetails);
				if (tubeJourneymatcher.find()) {
					String exactTripString = OysterCardConstants.COMPILED_TUBE_JOURNEY_PATTERN.matcher(journeyDetails)
							.replaceAll("");
					trip = initTrip(exactTripString, CommutationMode.TUBE);
				} else if (busJourneymatcher.find()) {
					String exactJourneyString = OysterCardConstants.COMPILED_BUS_JOURNEY_PATTERN.matcher(journeyDetails)
							.replaceAll("");
					trip = initTrip(exactJourneyString, CommutationMode.BUS);
				} else {
					logger.error("Provided Trip details are not in a proper format. Refer: " + journeyDetails);
					throw new IllegalParameterException(
							"Provided Trip details are not in a proper format. Refer: " + journeyDetails);
				}
				trips.add(trip);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
				inputStreamReader.close();
			} catch (IOException e) {
				logger.error("Error in I/O resource cleanup:" + e.getLocalizedMessage());
				e.printStackTrace();
			}

		}
		return trips;
	}

	/**
	 * @param classpathResource
	 * @return
	 */
	private Properties createPropsFromClassPathResource(String classpathResource) {
		Properties properties = new Properties();
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpathResource);
		try {
			properties.load(is);
		} catch (IOException e) {
			logger.error("Error while creating properties file from resource:" + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * @param resourceName
	 * @return
	 */
	private InputStream createInputStream(String resourceName) {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
		if(null == is) {
			throw new GenericException("Resource Not available on classpath.");
		}
		return is;
	}

	/**
	 * @param resource resource
	 * @return InputStreamReader
	 */
	private InputStreamReader createIsReader(String resource) {
		InputStream inputStream = createInputStream(resource);
		InputStreamReader inputStreamReader  = null;
		if(null != inputStream) {
			inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
		}else {
			throw new GenericException("Resource Not available on classpath.");
		}
		return inputStreamReader;
	}

	/**
	 * @param exactTripString exactTripString
	 * @param transporationMode transporationMode
	 * @return Trip
	 * @throws IllegalParameterException IllegalParameterException
	 */
	private Trip initTrip(String exactTripString, CommutationMode commutationMode) throws IllegalParameterException {
		int toindex = exactTripString.indexOf(" to ");
		if (toindex == -1) {
			logger.error("Provided Trip data in invalid");
			throw new IllegalParameterException("Provided Trip data in invalid");
		}
		String fromStationName = exactTripString.substring(0, toindex);
		String toStationName = exactTripString.substring(toindex + OysterCardConstants.UPPER_LIMIT_BOUND, exactTripString.length());
		Station from = stationManager.findStation(fromStationName);
		Station to = stationManager.findStation(toStationName);
		if ((from == null || to == null) && !fareManager.isGenericFareApplicable(commutationMode)) {
			logger.error(
				"Provided From/To Station is not known and"
				+" a generic fare for commutationMode: "
				+commutationMode + " is not available too");
			throw new IllegalParameterException(
				"Provided From/To Station is not known and"
				+" a generic fare for commutationMode: "
				+commutationMode + " is not available too");
		}
		return new Trip(from, to, commutationMode, exactTripString);
	}

}
