package edu.ntnu.stud;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * A user interface for the train departure application.
 */
public class TrainDepartureUserInterface {

  Scanner input = new Scanner(System.in);
  HashMap<String, Runnable> options = new HashMap<>();
  TrainFactory trainFactory = new TrainFactory();
  ArrayList<TrainDeparture> trainDepartureList = trainFactory.getTrainDepartureList();
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  private static final Pattern digits = Pattern.compile("\\D+");




  /**
   * Initialize the user interface.
   */
  public void init() {
    setCurrentTime();
    System.out.println(trainFactory.getCurrentTime());
    options.put("1", this::printDepartureOverview);
    options.put("2", this::addTrainDeparture);
    options.put("3", this::assignTrack);
    options.put("4", this::addDelay);
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

  /**
   * Adds a new train departure to the train schedule. The method prompts the user
   * to input departure time, destination, train number, track, and delay. It validates
   * the user input and ensures that valid information is provided before adding the
   * departure to the train schedule.
   *
   * Each parameter is validated individually in seperate while loops.
   * If the user input is invalid, a message is displayed explaining what is wrong with the input.
   * If the user input is valid, the while loop is exited and the user can input the next parameter.
   * values for trainNumber and destination assign to temporary variables which get checked for validity.
   * before the actual values are assigned to the train departure.
   *
   * once all parameters are valid, they are sent to the "addTrainDeparture" method in the trainFactory class.
   * where a train departure object is created and added to the train departure list.
   *
   */
  public void addTrainDeparture() {
    LocalTime departureTime;
    LocalTime delay;
    String destination;
    int trainNumber;
    int track;
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
    final String line = input.nextLine();
    while (true) {
      System.out.println("Enter destination: ");
      String tempDestination = input.nextLine();
      if (digits.matcher(tempDestination).matches() && !line.isEmpty()) {
        destination = tempDestination;
        break;
      } else {
        System.out.println("Destination cannot be empty or contain digits");
      }
    }
    while (true) {
      try {
        System.out.println("Enter train number: ");
        int tempTrainNumber = Integer.parseInt(input.nextLine());
        if (trainFactory.departureFromNumber(tempTrainNumber) != null) {
          System.out.println("Train number already exists");
        } else {
          trainNumber = tempTrainNumber;
          break;
        }
      } catch (Exception e) {
        System.out.println("Invalid train number");
      }
    }
    while (true) {
      try {
        System.out.println("Enter track: ");
        track = Integer.parseInt(input.nextLine());
        break;
      } catch (Exception e) {
        System.out.println("Invalid track number");
      }
    }
    while (true) {
      System.out.println("Enter delay(HH:mm): ");
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

    /**
     * Creates a table header for the departure overview.
     *
     * @return A string containing the table header.
     */
  public String tableHeader() {
    String info = "Current Time: " + trainFactory.getCurrentTime() + "\n";
    info += "+--------+--------------+--------+--------+---------------+-----------+\n";
    info += ("| Time   | Departures   |Track   | Line   |Train Number   |   Delay   |\n");
    info += ("+--------+--------------+--------+--------+---------------+-----------+");
    return info;
  }

  /**
   * Removes all departures from the trainDepartureList if their departure time is later than the current time.
   * which indicated that they have already departed and are no longer relevant.
   * Prints the table header first using the "tableHeader()" method.
   * Prints the departure overview using a for loop to iterate through the trainDepartureList.
   */
  public void printDepartureOverview() {
    trainDepartureList.removeIf(trainDeparture ->
            trainDeparture.getDepartureTime().isBefore(trainFactory.getCurrentTime()));
    System.out.println(tableHeader());
    for (TrainDeparture trainDeparture : trainDepartureList) {
      System.out.println(trainDeparture);
      System.out.println("+--------+--------------+--------+--------+---------------+-----------+");
    }
  }


  /**
   * Prompts the user to input a train number, input is validated in a try catch block.
   * if the input is invalid, a message is displayed and the user is prompted to input a new train number.
   * the input is parsed to an integer and sent to the "departureFromNumber" method in the trainFactory class.
   * if the return from this method is not equal to null, the departure prints (with the table header above it)
   * if the method in trainFactory returns null, ("Train Number not found") is displayed.
   */
  public void departureFromNumber() {
    while (true){
      System.out.println("Enter train number");
      try {
        int trainNumber = Integer.parseInt(input.nextLine());
        TrainDeparture trainDeparture = trainFactory.departureFromNumber(trainNumber);
        if (trainDeparture != null) {
          tableHeader();
          System.out.println(trainDeparture);
          break;
        } else {
          System.out.println("Train number not found");
        }
      } catch (Exception e) {
        System.out.println("Invalid train number");
      }
    }
  }



  public void departureFromDestination() {
    System.out.println("Enter destination");
    String destination = input.nextLine();
    TrainDeparture trainDeparture = trainFactory.departureFromDestination(destination);
    if (trainDeparture != null) {
      System.out.println(trainDeparture);
    } else {
      System.out.println("Train number not found");
    }
  }

  public void setCurrentTime() {
    while (true) {
      System.out.println("Enter current time (HH:mm): ");
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

  public void assignTrack() {
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    if (trainFactory.departureFromNumber(trainNumber) != null) {
      System.out.println("Enter track: ");
      int track = input.nextInt();
      trainFactory.assignTrack(trainNumber, track);
    } else {
      System.out.println("Train number not found");
      assignTrack();
    }
  }


  public void addDelay() {
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    if (trainFactory.departureFromNumber(trainNumber) != null) {
      System.out.println("Enter delay: ");
      String delayString = input.nextLine();
      LocalTime delay = LocalTime.parse(delayString, formatter);
      trainFactory.addDelay(trainNumber, delay);
    } else {
      System.out.println("Train number not found");
    }
  }
}
