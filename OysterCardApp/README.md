Problem statement 
--------------------
Design a limited version of London’s Oyster card system. Demonstrate a user loading a card with £30, 
`and taking the following trips, and then viewing the balance.
- Tube Holborn to Earl’s Court
- 328 bus from Earl’s Court to Chelsea
- Tube Earl’s court to Hammersmith

When the user passes through the inward barrier at the station, their oyster card is charged the maximum fare.
When they pass out of the barrier at the exit station, the fare is calculated and the maximum fare transaction 
removed and replaced with the real transaction (in this way, if the user doesn’t swipe out, they are charged the maximum fare).

All bus journeys are charged at same price. The system should favour the customer where more than one fare is 
possible for a given journey. E.g. Holburn to Earl’s Court is charged at £2.50. For the purposes of this test use the following data:

Stations and zones

Station  | Zone(s)
---------------------
Holborn | 1
Earl’s Court | 1, 2
Wimbledon | 3
Hammersmith | 2

Fares

Journey  | Fare
-------- | -------
Anywhere in Zone 1 | £2.50
Any one zone outside zone 1 | £2.00
Any two zones including zone 1 | £3.00
Any two zones excluding zone 1 | £2.25
Any three zones | £3.20
Any bus journey | £1.80

The maximum possible fare is therefore £3.20.

Solution 
-----------
1. For this particular use case, Fares information is loaded from zonesAndFares.txt, format of which is as below:
Key is -> Zone sets delimited by ';'. Each zone set may be having multiple zones, each delimited by ','
Value is -> Fare (associated with those zone sets) and the TransporationMode, delimited by '@'
2. Stations and zones information is loaded from stationsAndZones.txt, format of which is as below:
Key is -> Station name
Value is -> Set of zones to which this station belongs to
3. For Spring Boot client (OysterCardSystemAppClient#main), Travel data has been loaded from travelData.txt
5. In order to run this application as Rest service. Do a maven build. After that initiate mvn spring-boot:run.
6. Aftrer application is started, point browser to - http://localhost:8080/oyster/test-oyster-service
7. This will return Json response for the entire flow. Information comprises of all_station_details, all_fare_details, card details, trip_details
	journeyDetails, and finally all trip details. This entire flow is being picked up from travelData.txt, stationsAndZones.txt, zonesAndFares.txt.
From the unit testing and integration testing perspective: 
4. Package wise different test cases integrated into Junit Suite - OysterCardTestSuite.java
5. We can invoke all the Junits in one go from this suite. Altrenatively we can execute Junits one on one basis.
6. Sample data used for Junit Test Execution - dummy_stationsAndZones.txt, dummy_zonesAndFares.txt, invalidTravelData.txt, invalidTravelData1.txt
	travelData1.txt, travelData2.txt, travelData3.txt
	
7. Sample JSON Response, Code Coverage Screen shot, class diagram and sequence diagram is attached in OysterApplication.docx file.
