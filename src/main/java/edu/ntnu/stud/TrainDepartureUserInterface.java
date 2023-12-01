package edu.ntnu.stud;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A user interface for the train departure application.
 */
public class TrainDepartureUserInterface {

  private final Scanner input = new Scanner(System.in);
  private final HashMap<Integer, Runnable> options = new HashMap<>();
  private final TrainRegistry trainRegistry = new TrainRegistry();
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  private static final Pattern digits = Pattern.compile("\\D+");




  /**
   * Runs the setCURentTime method, which prompts the user to input a current time.
   * Initialize the user interface. Fills the "options" hashmap, with keys and different methods.
   * fills the train departure list with data from the "TrainDepartureData.txt" file.
   */
  public void init() {
    setCurrentTime();
    System.out.println(trainRegistry.getCurrentTime());
    options.put(1, this::printDepartureOverview);
    options.put(2, this::addTrainDeparture);
    options.put(3, this::assignTrack);
    options.put(4, this::addDelay);
    options.put(5, this::departureFromNumber);
    options.put(6, this::departureFromDestination);
    options.put(7, this::updateCurrentTime);
    options.put(8, trainRegistry::fillTrainDepartureListFromFile);
    options.put(9, trainRegistry::sortByDepartureTime);
    options.put(10, trainRegistry::writeTextToFile);
    trainRegistry.fillTrainDepartureListFromFile();
  }

  /**
   * Start the user interface. Prints a menu with different options for the user.
   * Uses a try catch block to prevent the input of non-number characters.
   * If the input is not a number, a message is displayed,
   * and the user is prompted to input a new number.
   * If the input is a number, the corresponding method is called.
   * If the input is 0, the program exits.
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
        break;
      } else {
        System.out.println("Invalid choice");
      }
    }
  }


  /**
   * Prompts the user to input a line, track, departure time, delay, destination and train number.
   * Using the respective methods for each input.
   * If the line or track already exists, the departure time is received using methods
   * that are specific for each case.
   * Once all variables are set, the
   * {@link TrainRegistry#addDeparture(LocalTime, String, String, int, int, LocalTime)}
   * is called
   * which creates a new train departure using the given parameters.
   */
  public void addTrainDeparture() {
    LocalTime departureTime;
    System.out.println("Enter line: ");
    final String line = lineInput();
    System.out.println("Enter track (0 if not set): ");
    final int track = numberInput();
    if (!trainRegistry.departureTimesFromLine(line).isEmpty()) {
      System.out.println("Enter departure time (HH:mm): ");
      departureTime = departureTimeExistingLine(line);
    } else if (track != 0 && !trainRegistry.departureTimesFromTrack(track).isEmpty()) {
      System.out.println("Enter departure time (HH:mm): ");
      departureTime = departureTimeExistingTrack(track);
    } else {
      System.out.println("Enter departure time (HH:mm): ");
      departureTime = timeInput();
    }
    System.out.println("Enter delay (HH:mm): ");
    final LocalTime delay = timeInput();
    System.out.println("Enter destination: ");
    final String destination = textInput();
    System.out.println("Enter train number: ");
    final int trainNumber = trainNumberFromInput();
    trainRegistry.addDeparture(departureTime, line, destination, trainNumber, track, delay);
  }

  /**
   * Creates a table header for the departure overview.
   *
   * @return A string containing the table header.
   */
  public String tableHeader() {
    String info = "Current Time: " + trainRegistry.getCurrentTime() + "\n";
    info += ("+---------------+-----------------+--------"
      + "------+----------+-------------------+------------+\n");
    info += ("|      Time     |    Destination  |     Track"
      + "    |   Line   |    Train Number   |    Delay   |\n");
    info += ("+---------------+-----------------+--------"
      + "------+----------+-------------------+------------+");
    return info;
  }

  /**
   * Calls
   * {@link TrainRegistry#removeDeparted()}
   * which removes all departures that have already departed.
   * (Their departure time + delay is less than the current time).
   * this is done to prevent the user from seeing departures that have already departed.
   *
   * <p>Prints the table header first using the {@link #tableHeader()} method.
   * Prints the departure overview using a stream
   * and {@link edu.ntnu.stud.TrainDeparture#toString()}
   */
  public void printDepartureOverview() {
    trainRegistry.removeDeparted();
    System.out.println(tableHeader());

    System.out.println(trainRegistry.getTrainDepartures().stream()
        .map(trainDeparture -> trainDeparture.toString()
          + "+---------------+-----------------+--------------"
          + "+----------+-------------------+------------+")
        .collect(Collectors.joining("\n")));
  }

  /**
   * First prints the table header using the {@link #tableHeader()} method.
   *
   * <p>@param departureList the list of departures to be printed.
   */
  public void printAnyDepartures(List<TrainDeparture> departureList) {
    System.out.println(tableHeader());
    System.out.println(departureList.stream()
            .map(trainDeparture -> trainDeparture.toString()
                    + "+---------------+-----------------+--------------"
                    + "+----------+-------------------+------------+")
            .collect(Collectors.joining("\n")));
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
   * Prompts the user to input a train number, which is then sent to
   * {@link TrainRegistry#departureFromNumber(int)}.
   * If the return from this method is not equal to null,
   * the departure information is printed (along with the table header above it).
   *
   * @see #printSingleDeparture(TrainDeparture)
   *      If the method in trainRegistry returns null,
   *      "Train number not found" is displayed.
   */

  public void departureFromNumber() {
    while (true) {
      System.out.println("Enter train number");
      int trainNumber = numberInput();
      TrainDeparture trainDeparture = trainRegistry.departureFromNumber(trainNumber);
      if (trainDeparture != null) {
        printSingleDeparture(trainDeparture);
        break;
      } else {
        System.out.println("Train number not found");
      }
    }
  }


  /**
   * Prompts the user to input a destination. User input sent to
   * {@link TrainRegistry#departureFromDestination(String)}
   * if the return from this method is not equal to null,
   * the departure prints (with the table header above it)
   *
   * @see #printAnyDepartures(List)
   *      if the method in trainRegistry returns null,
   *      ("No departure with this destination") is displayed.
   */
  public void departureFromDestination() {
    System.out.println("Enter destination");
    String destination = textInput();
    List<TrainDeparture> foundDepartures = trainRegistry.departureFromDestination(destination);
    if (foundDepartures != null) {
      printAnyDepartures(foundDepartures);
    } else {
      System.out.println("No departures with this destination");
    }
  }

  /**
   * Prompts the user to input a current time.
   *
   *<p>using the method {@link #timeInput()}
   * the input is sent to
   * {@link TrainRegistry#setCurrentTime(LocalTime)}
   *
   *<p>After this the
   * {@link TrainRegistry#removeDeparted()}
   * is called in order to remove all departures that have already departed.
   */
  public void setCurrentTime() {
    System.out.println("Enter current time (HH:mm): ");
    trainRegistry.setCurrentTime(timeInput());
    trainRegistry.removeDeparted();
  }

  /**
   * Method used to update the current time in the application.
   * Prompts the user to input a new current time.
   * using the method {@link #timeInput()}
   *
   * <ul>
   *     <li>if the input is before or equal to the current time,
   *     the user is prompted to input a new time.</li>
   *     <li>if the input is after the current time (valid input),
   *     the input is sent to {@link TrainRegistry#setCurrentTime(LocalTime)},
   *     which sets the current time to the input</li>
   *     <li>{@link TrainRegistry#removeDeparted()} is called to remove all departures
   *     that departed prior to the updated time.</li>
   * </ul>
   */
  public void updateCurrentTime() {
    while (true) {
      System.out.println("Enter new current time (HH:mm): ");
      LocalTime time = timeInput();
      if (time.isBefore(trainRegistry.getCurrentTime())) {
        System.out.println("Cannot set time before current time");
      } else if (time.equals(trainRegistry.getCurrentTime())) {
        System.out.println("Time is already set to this time");
      } else {
        trainRegistry.setCurrentTime(time);
        trainRegistry.removeDeparted();
        break;
      }
    }
  }

  /**
   * Method that collects user input for assigning a track to a train departure.
   * <ol>
   *     <li>Prompts user to input a train number, with {@link #numberInput()}</li>
   *     <ul>
   *         <li>If the input is 0 the user is returned to the main menu</li>
   *     </ul>
   *     <li>input sent to {@link TrainRegistry#departureFromNumber(int)}</li>
   *     <ul>
   *         <li>if the return from this method is null
   *         user is prompted to input a new train number</li>
   *         <li>If the return is not null, user is prompted to input a track
   *         with {@link #numberInput()}</li>
   *     </ul>
   *     <li>both inputs are sent to {@link TrainRegistry#assignTrack(int, int)}</li>
   * </ol>
   */
  public void assignTrack() {
    while (true) {
      System.out.println("Enter train number (0 to exit): ");
      int trainNumber = numberInput();
      if (trainRegistry.departureFromNumber(trainNumber) != null) {
        System.out.println("Enter track: ");
        int track = numberInput();
        trainRegistry.assignTrack(trainNumber, track);
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
   * Method that collects user input for adding delay to a train departure.
   * <ol>
   *     <li>Prompts user to input a train number, with {@link #numberInput()}</li>
   *     <ul>
   *         <li>If the input is 0 the user is returned to the main menu</li>
   *     </ul>
   *     <li>input sent to {@link TrainRegistry#departureFromNumber(int)}</li>
   *     <ul>
   *         <li>if the return from this method is null
   *         user is prompted to input a new train number</li>
   *         <li>If the return is not null, user is prompted to input a delay
   *         with {@link #timeInput()}</li>
   *     </ul>
   *     <li>both inputs are sent to {@link TrainRegistry#addDelay(int, LocalTime)}</li>
   * </ol>
   */
  public void addDelay() {
    while (true) {
      System.out.println("Enter train number (0 to exit): ");
      int trainNumber = numberInput();
      if (trainRegistry.departureFromNumber(trainNumber) != null) {
        System.out.println("Enter delay: ");
        LocalTime delay = timeInput();
        trainRegistry.addDelay(trainNumber, delay);
        break;
      } else if (trainNumber == 0) {
        break;
      } else {
        System.out.println("Train number not found");
        addDelay();
      }
    }
  }


  /**
   * Method that is used when creating a new train departure,
   * where the same train number cannot be used twice.
   *
   * <ol>
   *     <li>Prompts the user to input a train number,
   *     with {@link #numberInput()}</li>
   *     <li>Input sent to {@link TrainRegistry#departureFromNumber(int)}</li>
   *     <ul>
   *         <li>If the return from this method is not null,
   *     the user is prompted to input a new train number</li>
   *     <li>If the return is null, the loop exits and the train number is returned</li>
   *     </ul>
   * </ol>
   * 
   *
   * @return trainNumber
   */
  public int trainNumberFromInput() {
    int trainNumber;
    while (true) {
      trainNumber = numberInput();
      if (trainRegistry.departureFromNumber(trainNumber) != null) {
        System.out.println("Train number already exists");
      } else {
        break;
      }
    }
    return trainNumber;
  }

  /**
   * Method that is used to ask for a line input when creating a new train departure.
   *
   * <p>Prompts the user to input a line, which is validated in a while loop.
   * if the input is invalid, prompts the user for a new line.
   * if the input is valid, the line is made into uppercase and returned
   *
   * @return line.toUpperCase()
   */
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

  /**
   * Method that is used to set a departure time,
   * if a train departure on the given line already exists
   * (because two departures on the same line cannot have the same departure time).
   * 
   * <ol>
   *     <li>Prompts the user to input a departure time, with {@link #timeInput()}</li>
   *     <li>Input sent to {@link TrainRegistry#checkDepartureTimeExistsLine(String, LocalTime)}</li>
   *     <ul>
   *         <li>If the return from this method is true,
   *         the user is prompted to input a new departure time</li>
   *         <li>If the return is false, the loop exits and the departure time is returned</li>
   *     </ul>
   * </ol>
   *
   * @param line the line for the train departure that is being created
   * @return departureTime
   */
  public LocalTime departureTimeExistingLine(String line) {
    LocalTime departureTime = timeInput();
    if (trainRegistry.checkDepartureTimeExistsLine(line, departureTime)) {
      System.out.println("Departure time on this line already exists\nTry Again: ");
      departureTime = departureTimeExistingLine(line);
    } else {
      System.out.println("Departure time added");
    }
    return departureTime;
  }

  /**
   * Method that is used to set a departure time,
   * if a train departure on the given track already exists
   * (because two departures on the same track cannot have the same departure time).
   *
   * <ol>
   *     <li>Prompts the user to input a departure time, with {@link #timeInput()}</li>
   *     <li>Input sent to {@link TrainRegistry#checkDepartureTimeExistsTrack(int, LocalTime)}</li>
   *     <ul>
   *         <li>If the return from this method is true,
   *         the user is prompted to input a new departure time</li>
   *         <li>If the return is false, the loop exits and the departure time is returned</li>
   *     </ul>
   * </ol>
   *
   * @param track the track for the train departure that is being created
   * @return departureTime
   */
  public LocalTime departureTimeExistingTrack(int track) {
    LocalTime departureTime = timeInput();
    if (trainRegistry.checkDepartureTimeExistsTrack(track, departureTime)) {
      System.out.println("Departure time on this track already exists\nTry Again: ");
      departureTime = departureTimeExistingTrack(track);
    } else {
      System.out.println("Departure time added");
    }
    return departureTime;
  }

  /**
   * Method that used to gather user input for a time.
   *
   * <p>Prompts the user to input a time, which is validated in a try catch block,
   * by checking if the input matches the format "HH:mm".
   * the try catch is in a while loop, so if the input is invalid,
   * the user is prompted to input a new time.
   *
   * @return time LocalTime object with the time input by the user.
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
   * Method that used to gather user input for a number.
   *
   *<p>Prompts the user to input a number, which is validated in a try catch block.
   * the try catch is in a while loop, so if the input is invalid,
   * the user is prompted to input a new number.
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
   * if the input is valid, the text is returned.
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
