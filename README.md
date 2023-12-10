# Portfolio project IDATA1003 - 2023

STUDENT NAME = Stanislovas Mockus  
STUDENT ID = 106361

## Project description

A train departure overview application written in Java.  
Produces a formatted overview of train departures and offers options to find, sort and add train departures using user input. 

## Project structure

[//]: # (TODO: Describe the structure of your project here. How have you used packages in your structure. Where are all sourcefiles stored. Where are all JUnit-test classes stored. etc.)
  ### Used Packages:
  `java.util` used in order to:
  * Manage data structures, ArrayLists, HashMaps and Collections which store and sort different train departure information.
  * Implement a Scanner object which is used to retrieve user input.

`java.time` used in order to:
  * Create and modify LocalTime objects which manage departure times, delays and the applications internal system time.
  * Format user input in order to create LocalTime variables.

`java.io` used in order to:
  * Implement a BufferedReader object (in order to read data from a file)
  * Handle Input/Output operations that might throw an exception.

`java.nio` used in order to:
  * Creating _Path_ variables.
  * Defining the location of a file.
  * Operate on files.

  Project Files Path: `src/main/java/edu/ntnu/stud`  
  
  Test Files Path: `src/test/java/edu/ntnu/stud`
  
## Link to repository

https://github.com/stan1sm/MappeOppgave

## How to run the project

[//]: # (TODO: Describe how to run your project here. What is the main class? What is the main method?
What is the input and output of the program? What is the expected behaviour of the program?)
The main class which also contains the main method is `TrainDispatchApp`  
Upon running the program, it will ask to input the current time, this is used for initializing the program.

### Creating a `.txt` data file:  
Before running the application you can add your train depetartures to,  
`src/main/resources/TrainDepartureData.txt`, this way the departures will automatically be added  
to the registry upon starting the program.  
NOTE: Your data has to follow the format specified on the first line in `TrainDepartureData.txt`.

### Running the Project:

### In IntelliJ IDEA GUI:
  ### Windows
  * Navigate to `src/main/java/edu/ntnu/stud/TrainDispatchApp.java`
  * `SHIFT+F10` to run the application.

  ### macOS
  * Navigate to `src/main/java/edu/ntnu/stud/TrainDispatchApp.java`
  * `control+R` to run the application.

### In Terminal:  
  1. Make sure maven is installed, you can check this with `mvn --version`  
     You can install maven by following `https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html`
  3. Build the project with `mvn package`  
  4. Navigate to `\MappeOppgave\target`
  5. Run `"java -cp .\TrainDispatchSystem-1.0-SNAPSHOT.jar edu.ntnu.stud.TrainDispatchApp"`

## How to run the tests
### In InTelliJ IDEA GUI:
  ### Windows
  1. Navigate to `src/test/java/edu/ntnu/stud/TrainDepartureTest.java` to test the TrainDeparture class  
     or to `src/test/java/edu/ntnu/stud/TrainRegistryTest.java` to test the TrainRegistry class.
  2. Press `SHIFT + F10` to run the test class.

  ### macOS
  1. Navigate to `src/test/java/edu/ntnu/stud/TrainDepartureTest.java` to test the TrainDeparture class  
     or to `src/test/java/edu/ntnu/stud/TrainRegistryTest.java` to test the TrainRegistry class.
  2. Press `control + R` to run the test class.

## In Terminal:  
 * Navigate to `src/main/java/edu/ntnu/stud`
 * Run all tests with: `mvn test`
 * Test trainDeparture class with `mvn -Dtest=TrainDepartureTest test`
 * Test TrainRegistry class with `mvn -Dtest=TrainRegistryTest test`


## References

[//]: # (TODO: Include references here, if any. For example, if you have used code from the course book, include a reference to the chapter.
Or if you have used code from a website or other source, include a link to the source.)
GeeksForGeeks, September 8, 2023 “Stream in Java”, https://www.geeksforgeeks.org/stream-in-java/
https://stackoverflow.com/questions/31432948/print-list-items-with-java-streams
https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
https://introcs.cs.princeton.edu/java/stdlib/In.java.html


