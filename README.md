
#### JDK 11 is required
<br><br/>
#### Assumptions
1. for each unique user identified by PAN, timestamps of his/hers action must be unique. eg user cannot tap on at stop1 
then tap off at stop2 at the same time. It also applies to cancelled trips. There will be at least one second difference 
touch on and off.
2. each unique user must have only one active trip at one time. eg it is never possible for a user to touch on
to start a trip, touch on again to start another trip, then touch off to finish the first trip.
<br><br/>
#### Command build
```
./gradlew clean build
```
this will generate the jar executable file ```bus-fare-service-0.0.1-SNAPSHOT.jar``` in the source directory.
<br><br/>
#### Command to run the app
```
java -jar bus-fare-service-0.0.1-SNAPSHOT.jar 1_exampleInput.csv
```
inputFile needs to be in the same directory with the jar file. User must have write permission in the directory to 
allow export of the output file.
I included a few test files
1. 1_exampleInput.csv (a simple completed trip)
2. 2_cancelledTrip.csv (a simple cancelled trip)
3. 3_incompleteTrip.csv (a simple incomplete trip)
4. 4_MixedTypeTrips.csv (a more complex test case)
5. 5_20000Trips.csv (over 20000 trips test file)
<br><br/>
#### output file
```trips.csv```
trips are sorted by time started