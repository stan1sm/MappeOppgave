package edu.ntnu.stud;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A user interface for the train departure application.
 */
public class TrainDepartureUserInterface {

  private final Scanner input = new Scanner(System.in);
  private final HashMap<Integer, Runnable> options = new HashMap<>();
  private final TrainFactory trainFactory = new TrainFactory();
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
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
    options.put(9, trainFactory::sortByDepartureTime);
    trainFactory.fillTrainDepartureList();
  }

  /**
   * Start the user interface. Prints a menu with different options for the user.
   * Uses a try catch block to prevent the input of non-number characters.
   * If the input is not a number, a message is displayed,
   * and the user is prompted to input a new number.
   * If the input is a number, the corresponding method is called.
   * If the input is 9, the program exits.
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
      System.out.println("9. Sort train departure list by ascending departure time");
      System.out.println("0. Exit");

      System.out.print("Enter your choice: ");
      int choice = numberInput();
      if (choice > 0 && choice <= options.size()) {
        options.get(choice).run();
      } else if (choice == 0) {
        System.out.println("Exiting...");
        System.exit(0);
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
   *
   *
   *<p>Each parameter is validated individually in separate while loops.
   * If the user input is invalid, a message is displayed explaining what is wrong with the input.
   * If the user input is valid, the while loop is exited and the user can input the next parameter.
   * values for trainNumber and destination assign to temporary variables
   * which get checked for validity.
   * before the actual values are assigned to the train departure.
   *
   *<p>once all parameters are valid,
   * they are sent to the "addTrainDeparture" method in the trainFactory class.
   * where a train departure object is created and added to the train departure list.
   *
   */
  public void addTrainDeparture() {
    LocalTime departureTime = null;
    System.out.println("Enter line: ");
    final String line = lineInput();
    System.out.println("Enter track (0 if not set): ");
    final int track = numberInput();
    if (trainFactory.checkLineExists(line)){
      System.out.println("Enter departure time (HH:mm): ");
      departureTime = departureTimeExistingLine(line);
    } else if (trainFactory.checkTrackExists(track) && track != 0){
        System.out.println("Enter departure time (HH:mm): ");
        departureTime = departureTimeExistingTrack(track);
    }
    System.out.println("Enter delay (HH:mm): ");
    final LocalTime delay = timeInput();
    System.out.println("Enter destination: ");
    final String destination = textInput();
    System.out.println("Enter train number: ");
    final int trainNumber = trainNumberFromInput();
    trainFactory.addDeparture(departureTime, line, destination, trainNumber, track, delay);
  }

  /**
   * Creates a table header for the departure overview.
   *
   * @return A string containing the table header.
   */
  public String tableHeader() {
    String info = "Current Time: " + trainFactory.getCurrentTime() + "\n";
    info += ("+---------------+-----------------+--------"
      + "------+----------+-------------------+------------+\n");
    info += ("|      Time     |    Departures   |     Track"
      + "    |   Line   |    Train Number   |    Delay   |\n");
    info += ("+---------------+-----------------+--------"
      + "------+----------+-------------------+------------+");
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

    System.out.println(trainFactory.getTrainDepartures().stream()
        .map(trainDeparture -> trainDeparture.toString()
          + "+---------------+-----------------+--------------"
          + "+----------+-------------------+------------+")
        .collect(Collectors.joining("\n")));
  }

  /**
   * Prints a list of departures using the table header,
   * and the toString method in the trainDeparture class.
   *
   * @param departureList the list of departures to be printed.
   */
  public void printAnyDepartures(ArrayList<TrainDeparture> departureList) {
    System.out.println(tableHeader());
    for (TrainDeparture trainDeparture : departureList) {
      System.out.println(trainDeparture);
      System.out.println("+----------+-----------------+------------"
          + "+----------+-------------------+------------+");
    }
  }

  /**
   * Prints a single departure using the table header,
   * and the toString method in the trainDeparture class.
   *
   * @param departure the departure to be printed.
   */
  public void printSingleDeparture(TrainDeparture departure) {
    System.out.println(tableHeader());
    System.out.println(departure);
  }

  /**
   * Prompts the user to input a train number, input is validated in a try catch block.
   * if the input is invalid,
   * a message is displayed and the user is prompted to input a new train number.
   * the input is parsed to an integer,
   * and sent to the "departureFromNumber" method in the trainFactory class.
   * if the return from this method is not equal to null,
   * the departure prints (with the table header above it)
   * if the method in trainFactory returns null, ("Train Number not found") is displayed.
   */
  public void departureFromNumber() {
    while (true) {
      System.out.println("Enter train number");
      try {
        int trainNumber = Integer.parseInt(input.nextLine());
        TrainDeparture trainDeparture = trainFactory.departureFromNumber(trainNumber);
        if (trainDeparture != null) {
          printSingleDeparture(trainDeparture);
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
   * if the return from this method is not equal to null,
   * the departure prints (with the table header above it)
   * if the method in trainFactory returns null,
   * ("No departure with this destination") is displayed.
   */
  public void departureFromDestination() {
    System.out.println("Enter destination");
    String destination = input.nextLine();
    ArrayList<TrainDeparture> foundDepartures = trainFactory.departureFromDestination(destination);
    if (foundDepartures != null) {
      printAnyDepartures(foundDepartures);
    } else {
      System.out.println("No departures with this destination");
    }
  }

  /**
   * Prompts the user to input a new current time.
   * User input is validated in a try catch block.
   * if the input is invalid, a message is displayed,
   * and the user is prompted to input a new current time.
   * if the input is valid, the input is parsed to a local time,
   * and sent to the "setCurrentTime" method in the trainFactory class.
   * which sets the current time to the input.
   * calls "removeDeparted" method in the trainFactory class,
   * which removes all departures that departed before the current time.
   */
  public void setCurrentTime() {
    System.out.println("Enter current time (HH:mm): ");
    trainFactory.setCurrentTime(timeInput());
    trainFactory.removeDeparted();
  }

  /**
   * Prompts the user to input a new current time. User input is validated in a try catch block.
   * if the time format is invalid, a message is displayed, prompts user for a different time.
   * if the time is before the current time,
   * a message is displayed, prompts user for a different time.
   * if time is valid and before current time,
   * the time is sent to the "setCurrentTime" method in the trainFactory class.
   */
  public void updateCurrentTime() {
    while (true) {
      System.out.println("Enter new current time (HH:mm): ");
      LocalTime time = timeInput();
      if (time.isBefore(trainFactory.getCurrentTime())) {
        System.out.println("Cannot set time before current time");
      } else if (time.equals(trainFactory.getCurrentTime())) {
        System.out.println("Time is already set to this time");
      } else {
        trainFactory.setCurrentTime(time);
        break;
      }
    }
  }

  /**
   * Prompts the user to input a train number,
   * which gets sent to the "departureFromNumber" method in the trainFactory class.
   * If the return from this method is not null,
   * it means that a departure with this train number exists.
   * and the user is prompted to input a track number,
   * which is sent to the assignTrack method in the trainFactory class.
   * which sets the track number for the departure.
   * if a departure with this train number does not exist,
   * the user is prompted to input a new train number.
   */
  public void assignTrack() {
    while (true) {
      System.out.println("Enter train number (0 to exit): ");
      int trainNumber = numberInput();
      if (trainFactory.departureFromNumber(trainNumber) != null) {
        System.out.println("Enter track: ");
        int track = numberInput();
        trainFactory.assignTrack(trainNumber, track);
        break;
      } else if (trainNumber == 0) {
        break;
      } else {
        System.out.println("Train number not found");
        assignTrack();
      }
    }
  }


  /**
   * Prompts the user to enter a train number,
   * which is sent to the "departureFromNumber" method in the trainFactory class.
   * if the return from this method is not null,
   * it means that a departure with this train number exists.
   * and the user is prompted to input a delay,
   * which is sent to the addDelay method in the trainFactory class.
   * which adds the delay to the departure.
   * if a departure with this train number does not exist,
   * the user is prompted to input a new train number.
   */
  public void addDelay() {
    System.out.println("Enter train number: ");
    int trainNumber = numberInput();
    if (trainFactory.departureFromNumber(trainNumber) != null) {
      System.out.println("Enter delay: ");
      LocalTime delay = timeInput();
      trainFactory.addDelay(trainNumber, delay);
    } else {
      System.out.println("Train number not found");
    }
  }


  /**
   * Prompts the user to input a train number, which is validated in a try catch block.
   * if the input is invalid, prompts the user for a new train number.
   *
   * @return trainNumber
   */
  public int trainNumberFromInput() {
    int trainNumber;
    while (true) {
      trainNumber = numberInput();
      if (trainFactory.departureFromNumber(trainNumber) != null) {
        System.out.println("Train number already exists");
      } else {
        break;
      }
    }
    return trainNumber;
  }

  public String lineInput() {
    String line;
    while (true) {
      line = input.nextLine();
      if (line.isEmpty()) {
        System.out.println("Line Cannot be empty.\nTry again: ");
      } else {
        break;
      }
    }
    return line.toUpperCase();
  }

  public LocalTime departureTimeExistingLine(String line){
    LocalTime finalDepartureTime = timeInput();
    trainFactory.departureTimesFromLine(line).forEach(time -> {
      if (time.equals(finalDepartureTime)) {
        System.out.println("Departure time already exists\nTry Again: ");
        departureTimeExistingLine(line);
      } else {
        System.out.println("Departure time added");
      }
    });
    return finalDepartureTime;
  }

  public LocalTime departureTimeExistingTrack(int track){
    LocalTime departureTime;
    while (true) {
      departureTime = timeInput();
      if(trainFactory.checkDepartureTimeExistsTrack(track, departureTime)){
        System.out.println("Departure time already exists\nTry Again: ");
        departureTimeExistingTrack(track);
      } else {
        System.out.println("Departure time added");
        break;
      }
    }
    return departureTime;
  }

  /**
   * Prompts the user to input a time, which is validated in a while loop.
   *
   * @return time
   */
  public LocalTime timeInput() {
    LocalTime time;
    while (true) {
      String timeString = input.nextLine();
      try {
        time = LocalTime.parse(timeString, formatter);
        break;
      } catch (Exception e) {
        System.out.println("Invalid time format.\nTry again: ");
      }
    }
    return time;
  }

  /**
   * Prompts the user to input a number, which is validated in a while loop.
   *
   * @return number
   */
  public int numberInput() {
    int number;
    while (true) {
      try {
        number = Integer.parseInt(input.nextLine());
        break;
      } catch (Exception e) {
        System.out.println("Invalid number.\nTry again: ");
      }
    }
    return number;
  }

  /**
   * Prompts the user to input a text, which is validated in a while loop,
   * using a pattern matcher to check if the input contains digits.
   * if the input is invalid, prompts the user for a new text.
   *
   * @return text
   */
  public String textInput() {
    String text;
    while (true) {
      text = input.nextLine();
      if (digits.matcher(text).matches()) {
        return text;
      } else {
        System.out.println("Cannot be empty or contain digits.\nTry again: ");
      }
    }
  }
}
