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
  HashMap<Integer, Runnable> options = new HashMap<>();
  TrainFactory trainFactory = new TrainFactory();
  ArrayList<TrainDeparture> trainDepartureList = trainFactory.getTrainDepartureList();
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  private static final Pattern digits = Pattern.compile("\\D+");




  /**
   * Runs the setCURentTime method, which prompts the user to input a current time.
   * Initialize the user interface. Fills the "options" hashmap, with keys and different methods.
   * fills the train departure list with data from the "Data.txt" file.
   */
  public void init() {
    setCurrentTime();
    System.out.println(trainFactory.getCurrentTime());
    options.put(1, this::printDepartureOverview);
    options.put(2, this::addTrainDeparture);
    options.put(3, this::assignTrack);
    options.put(4, this::addDelay);
    options.put(5, this::departureFromNumber);
    options.put(6, this::departureFromDestination);
    options.put(7, this::updateCurrentTime);
    options.put(8, trainFactory::fillTrainDepartureList);
    options.put(9, () -> System.exit(0));
    trainFactory.fillTrainDepartureList();
  }

  /**
   * Start the user interface. Prints a menu with different options for the user.
   * Uses a try catch block to prevent the input of non-number characters.
   * If the input is not a number, a message is displayed and the user is prompted to input a new number.
   * If the input is a number, the corresponding method is called.
   * If the input is 9, the program exits.
   */
  public void start() {
    Scanner menuChoice = new Scanner(System.in);
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

      System.out.print("Enter your choice: ");
      int choice = Integer.parseInt(menuChoice.nextLine());
      if(choice > 0 && choice <= options.size()){
        options.get(choice).run();
      } else {
        System.out.println("Invalid choice");
      }
    }
  }


  /**
   * Adds a new train departure to the train schedule. The method prompts the user
   * to input departure time, destination, train number, track, and delay. It validates
   * the user input and ensures that valid information is provided before adding the
   * departure to the train schedule.
   * <p>
   * Each parameter is validated individually in separate while loops.
   * If the user input is invalid, a message is displayed explaining what is wrong with the input.
   * If the user input is valid, the while loop is exited and the user can input the next parameter.
   * values for trainNumber and destination assign to temporary variables
   * which get checked for validity.
   * before the actual values are assigned to the train departure.
   *<p>
   * once all parameters are valid, they are sent to the "addTrainDeparture" method in the trainFactory class.
   * where a train departure object is created and added to the train departure list.
   *
   */
  public void addTrainDeparture() {
    LocalTime departureTime = departureTimeFromInput();
    LocalTime delay = delayFromInput();
    String line = lineFromInput();
    String destination = destinationFromInput();
    int trainNumber = trainNumberFromInput();
    int track = trackFromInput();
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
   * Calls the "removeDeparted" method in the trainFactory class.
   * which removes all departures that have already departed.
   * (Their departure time + delay is less than the current time).
   * Prints the table header first using the "tableHeader()" method.
   * Prints the departure overview using a for loop to iterate through the trainDepartureList.
   */
  public void printDepartureOverview() {
    trainFactory.removeDeparted();
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


  /**
   * Prompts the user to input a destination. User input sent to
   * "departureFromDestination" method in the trainFactory class.
   * if the return from this method is not equal to null, the departure prints (with the table header above it)
   * if the method in trainFactory returns null, ("No departure with this destination") is displayed.
   */
  public void departureFromDestination() {
    System.out.println("Enter destination");
    String destination = input.nextLine();
    ArrayList<TrainDeparture> foundDepartures = trainFactory.departureFromDestination(destination);
    if (foundDepartures != null) {
        System.out.println(tableHeader());
        for (TrainDeparture trainDeparture : foundDepartures) {
            System.out.println(trainDeparture);
            System.out.println("+--------+--------------+--------+--------+---------------+-----------+");
        }
    } else {
      System.out.println("No departures with this destination");
    }
  }

    /**
     * Prompts the user to input a new current time. User input is validated in a try catch block.
     * if the input is invalid, a message is displayed and the user is prompted to input a new current time.
     * if the input is valid, the input is parsed to a local time and sent to the "setCurrentTime" method in the trainFactory class.
     */
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

  /**
   * Prompts the user to input a new current time. User input is validated in a try catch block.
   * if the time format is invalid, a message is displayed, prompts user for a different time.
   * if the time is before the current time, a message is displayed, prompts user for a different time.
   * if time is valid and before current time, the time is sent to the "setCurrentTime" method in the trainFactory class.
   */
  public void updateCurrentTime() {
    while (true) {
      System.out.println("Enter current time (HH:mm): ");
      String departureTimeString = input.nextLine();
      try {
        LocalTime departureTime = LocalTime.parse(departureTimeString, formatter);
        if (departureTime.isBefore(trainFactory.getCurrentTime())) {
          System.out.println("Cannot set time before current time");
        } else {
          trainFactory.setCurrentTime(departureTime);
          break;
        }
      } catch (Exception e) {
        System.out.println("Invalid time format");
      }
    }
  }

  /**
   * Prompts the user to input a train number, which gets sent to the "departureFromNumber" method in the trainFactory class.
   * If the return from this method is not null, it means that a departure with this train number exists.
   * and the user is prompted to input a track number, which is sent to the assignTrack method in the trainFactory class.
   * which sets the track number for the departure.
   * if a departure with this train number does not exist, the user is prompted to input a new train number.
   */
  public void assignTrack() {
    while (true) {
      System.out.println("Enter train number (0 to exit): ");
      try {
        int trainNumber = Integer.parseInt(input.nextLine());
        if (trainFactory.departureFromNumber(trainNumber) != null) {
          System.out.println("Enter track: ");
          int track = input.nextInt();
          trainFactory.assignTrack(trainNumber, track);
        } else if (trainNumber == 0) {
          break;
        } else {
          System.out.println("Train number not found");
          assignTrack();
        }
      } catch (Exception e) {
        System.out.println("Invalid train number");
      }
    }
  }


  /**
   * Prompts the user to enter a train number which is sent to the "departureFromNumber" method in the trainFactory class.
   * if the return from this method is not null, it means that a departure with this train number exists.
   * and the user is prompted to input a delay, which is sent to the addDelay method in the trainFactory class.
   * which adds the delay to the departure.
   * if a departure with this train number does not exist, the user is prompted to input a new train number.
   */
  public void addDelay() {
    System.out.println("Enter train number: ");
    try{
      int trainNumber = Integer.parseInt(input.nextLine());
      if (trainFactory.departureFromNumber(trainNumber) != null) {
        System.out.println("Enter delay: ");
        String delayString = input.nextLine();
        LocalTime delay = LocalTime.parse(delayString, formatter);
        trainFactory.addDelay(trainNumber, delay);
      } else {
        System.out.println("Train number not found");
      }
    } catch (Exception e) {
      System.out.println("Invalid train number");
    }
  }

  public LocalTime departureTimeFromInput(){
    LocalTime departureTime;
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
    return departureTime;
  }

  public String lineFromInput() {
    while (true) {
      System.out.println("Enter line: ");
      String line = input.nextLine();
      if (line.isEmpty()) {
        System.out.println("Line cannot be empty");
      }else{
        return line.toUpperCase();
      }
    }
  }

  public String destinationFromInput(){
    String destination;
    while (true) {
      System.out.println("Enter destination: ");
      String tempDestination = input.nextLine();
      if (digits.matcher(tempDestination).matches()) {
        destination = tempDestination;
        break;
      } else {
        System.out.println("Destination cannot be empty or contain digits");
      }
    }
    return destination;
  }

  public int trainNumberFromInput(){
    int trainNumber;
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
    return trainNumber;
  }

  public int trackFromInput(){
    int track;
    while (true) {
      try {
        System.out.println("Enter track (0 if not set): ");
        track = Integer.parseInt(input.nextLine());
        break;
      } catch (Exception e) {
        System.out.println("Invalid track number");
      }
    }
    return track;
  }

  public LocalTime delayFromInput(){
    LocalTime delay;
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
    return delay;
  }
}
