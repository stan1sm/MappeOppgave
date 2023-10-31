package edu.ntnu.stud;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * A user interface for the train departure application.
 */
public class TrainDepartureUserInterface {

  Scanner input = new Scanner(System.in);
  HashMap<String, Runnable> options = new HashMap<>();
  TrainFactory trainFactory = new TrainFactory();
  ArrayList<TrainDeparture> trainDepartureList = trainFactory.getTrainDepartureList();
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");



  /**
   * Initialize the user interface.
   */
  public void init() {
    setCurrentTime();
    System.out.println(trainFactory.getCurrentTime());
    options.put("1", this::printDepartureOverview);
    options.put("2", this::addTrainDeparture);
    options.put("3", trainFactory::assignTrack);
    options.put("4", trainFactory::addDelay);
    options.put("5", this::departureFromNumber);
    options.put("6", this::departureFromDestination);
    options.put("7", this::setCurrentTime);
    options.put("8", trainFactory::fillTrainDepartureList);
    options.put("9", () -> System.exit(0));
    trainFactory.fillTrainDepartureList();
  }

  /**
   * Start the user interface.
   */
  public void start() {

    while (true) {
      System.out.println("1. Overview of all departures");
      System.out.println("2. Add a Departure");
      System.out.println("3. Assign Track to Departure");
      System.out.println("4. Add Delay to departure");
      System.out.println("5. Search for departure with Train Number");
      System.out.println("6. Search for departure with Destination");
      System.out.println("7. Update Current Time");
      System.out.println("8. Fill train departure list with data");
      System.out.println("9. Exit");
      String choice = input.nextLine();


      if (options.containsKey(choice)) {
        options.get(choice).run();
      } else {
        throw new IllegalArgumentException("Invalid choice");
      }
    }
  }

  public void addTrainDeparture(){
    LocalTime departureTime;
    LocalTime delay;
    while (true) {
      System.out.println("Enter departure time (HH:mm): ");
      String departureTimeString = input.nextLine();
      try {
        departureTime = LocalTime.parse(departureTimeString, formatter);
        break;
      } catch (Exception e) {
        System.out.println("Invalid time format");
      }
    }
    System.out.println("Enter line: ");
    String line = input.nextLine();
    System.out.println("Enter destination: ");
    final String destination = input.nextLine();
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    System.out.println("Enter track: ");
    int track = input.nextInt();
    while (true) {
      System.out.println("Enter delay: ");
      input.nextLine(); //consume newline
      String delayString = input.nextLine();
      try {
        delay = LocalTime.parse(delayString, formatter);
        break;
      } catch (Exception e) {
        System.out.println("Invalid time format");
      }
    }
    trainFactory.addDeparture(departureTime, line, destination, trainNumber, track, delay);
  }
  public String tableHeader(){
    String info = "Current Time: " + trainFactory.getCurrentTime() + "\n";
    info += "+--------+--------------+--------+--------+---------------+-----------+\n";
    info += ("| Time   | Departures   |Track   | Line   |Train Number   |   Delay   |\n");
    info += ("+--------+--------------+--------+--------+---------------+-----------+");
    return info;
  }

  /**
   * Print an overview of all departures.
   */
  public void printDepartureOverview() {
    trainDepartureList.removeIf(
            trainDeparture -> trainDeparture.getDepartureTime().isBefore(trainFactory.getCurrentTime()));
    System.out.println(tableHeader());
    for (TrainDeparture trainDeparture : trainDepartureList) {
      System.out.println(trainDeparture);
      System.out.println("+--------+--------------+--------+--------+---------------+-----------+");
    }
  }

  public void departureFromNumber(){
    System.out.println("Enter train number");
    int trainNumber = input.nextInt();
    TrainDeparture trainDeparture = trainFactory.departureFromNumber(trainNumber);
    if(trainDeparture != null){
      System.out.println(trainDeparture);
    }else{
      System.out.println("Train number not found");
    }
  }

  public void departureFromDestination(){
    System.out.println("Enter destination");
    String destination = input.nextLine();
    TrainDeparture trainDeparture = trainFactory.departureFromDestination(destination);
    if(trainDeparture != null){
      System.out.println(trainDeparture);
    }else{
      System.out.println("Train number not found");
    }
  }

  public void setCurrentTime(){
    while (true) {
      System.out.println("Enter departure time (HH:mm): ");
      String departureTimeString = input.nextLine();
      try {
        LocalTime departureTime = LocalTime.parse(departureTimeString, formatter);
        trainFactory.setCurrentTime(departureTime);
        break;
      } catch (Exception e) {
        System.out.println("Invalid time format");
      }
    }
  }
}
