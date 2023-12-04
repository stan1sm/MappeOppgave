package edu.ntnu.stud;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * TrainDepartureUserInterface lets users interact with the application.
 *
 * <p>The class is responsible for gathering user input,
 * that is required in order to use the different functions that the application provides.
 * It also displays information to the user.
 *
 *<p>The class uses a HashMap to store the different options for the user.
 * And uses a Scanner in order to gather user input.
 *
 *<p>All input is validated in their respective methods.
 * This is done in order to prevent the user from inputting invalid data.
 *
 * <p>This class assumes that the TrainRegistry class is working,
 * in order to manage TrainDeparture-objects.
 *
 * @see TrainRegistry
 */
public class TrainDepartureUserInterface {

  private final Scanner input = new Scanner(System.in);
  private final HashMap<Integer, Runnable> options = new HashMap<>();
  private final TrainRegistry trainRegistry = new TrainRegistry();
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  private static final Pattern digits = Pattern.compile("\\D+");
  private static final String ASK_FOR_TIME = "Enter departure time (HH:mm): ";
  private static final String TABLE_LINE = "+----------------+----------+---------------------"
          + "+-----------------+------------+------------+";
  private static final String NUMBER_NOT_FOUND = "Train number not found";


  /**
   * Method that initializes the user interface class.
   * <ul>
   *     <li>Calls {@link #setCurrentTime()}</li>
   *     <li>Populates {@link #options} with the different options for the user.</li>
   *     <li>Calls {@link TrainRegistry#fillTrainDepartureListFromFile()}
   *     in order to populate the registry with train departures.</li>
   * </ul>
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
    trainRegistry.fillTrainDepartureListFromFile();
  }

  /**
   * Method used to start the user interface.
   *
   *<ul>
   *     <li>Prints a menu with different options for the user.</li>
   *     <li>Uses a try catch block to prevent the input of non-number characters.</li>
   *     <li>If the input is not a number,
   *     or if its a number that is bigger than the amount of choices, a message is displayed,
   *     and the user is prompted to input a new number.</li>
   *     <li>If the input is a number, the corresponding method is called.</li>
   *     <li>If the input is 0, the program exits.</li>
   *</ul>
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
   * Method used to collect user input for creating a new train departure.
   *
   * <p>Prompts the user to input a line, track,
   * departure time, delay, destination and train number.
   * Using the respective methods for each type of input.
   *
   *<p>If the line or track already exists, the departure time is received using methods
   * that are specific for each case.
   *
   * <p>Once all variables are set
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
      System.out.println(ASK_FOR_TIME);
      departureTime = departureTimeExistingLine(line);
    } else if (track != 0 && !trainRegistry.departureTimesFromTrack(track).isEmpty()) {
      System.out.println(ASK_FOR_TIME);
      departureTime = departureTimeExistingTrack(track);
    } else {
      System.out.println(ASK_FOR_TIME);
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
   * Method that returns a string containing the table header.
   *
   * @return A string containing the table header.
   */
  public String tableHeader() {
    String info = "Current Time: " + trainRegistry.getCurrentTime() + "\n";
    info += (TABLE_LINE + "\n");
    info += ("| Departure Time |    Line  |     Train Number    "
            + "|   Destination   |    Delay   |    Track   |\n");
    info += (TABLE_LINE);
    return info;
  }

  /**
   * Method used to print the complete departure overview.
   *
   * <p>Calls
   * {@link TrainRegistry#removeDeparted()}
   * which removes all departures that have already departed.
   * (Their departure time + delay is less than the current time).
   * this is done to prevent the user from seeing departures that have already departed.
   *
   * <p>Prints the table header first using {@link #tableHeader()}.
   * Uses a stream to print the String representation of each TrainDeparture object.
   */
  public void printDepartureOverview() {
    trainRegistry.removeDeparted();
    System.out.println(tableHeader());

    System.out.println(trainRegistry.getTrainDepartures().stream()
        .map(trainDeparture -> trainDeparture.toString()
          + TABLE_LINE)
        .collect(Collectors.joining("\n")));
  }

  /**
   * Method used to print any list of TrainDeparture objects.
   *
   *<p>First prints the table header using {@link #tableHeader()}.
   * Then uses a stream to print the String representation of each TrainDeparture object.
   *
   * @param departureList the list of departures to be printed.
   */
  public void printAnyDepartures(List<TrainDeparture> departureList) {
    System.out.println(tableHeader());
    System.out.println(departureList.stream()
            .map(trainDeparture -> trainDeparture.toString()
                    + "+----------------+----------+---------------------"
                    + "+-----------------+------------+------------+")
            .collect(Collectors.joining("\n")));
  }

  /**
   * Method used to print a single TrainDeparture object.
   *
   * <p>First prints the table header using {@link #tableHeader()}.
   * Then prints the String representation of the TrainDeparture object.
   *
   * @param departure the departure to be printed.
   */
  public void printSingleDeparture(TrainDeparture departure) {
    System.out.println(tableHeader());
    System.out.println(departure);
  }

  /**
   * Method that collects user input in order to search for a departure,
   * based on the train number. And prints the departure if it exists.
   *
   * <ol>
   *     <li>Prompts the user to input a train number, with {@link #numberInput()}</li>
   *     <li>Input sent to {@link TrainRegistry#departureFromNumber(int)}</li>
   *     <ul>
   *         <li>If the return from this method is not null, calls
   *         {@link #printSingleDeparture(TrainDeparture)} with the returned TrainDeparture
   *         as a parameter for the method.</li>
   *         <li>If the return is null, "Train number not found is displayed,
   *         and the user is returned to the main menu.</li>
   *     </ul>
   * </ol>
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
        System.out.println(NUMBER_NOT_FOUND);
      }
    }
  }


  /**
   * Method that collects user input in order to search for a departures,
   * based on the destination. And prints the departures if any exist.
   *
   * <ol>
   *     <li>Prompts the user to input a train number, with {@link #numberInput()}</li>
   *     <li>Input sent to {@link TrainRegistry#departureFromNumber(int)}</li>
   *     <ul>
   *         <li>If the return from this method is not null, calls
   *         {@link #printSingleDeparture(TrainDeparture)} with the returned TrainDeparture
   *         as a parameter for the method.</li>
   *         <li>If the return is null, "Train number not found is displayed,
   *         and the user is returned to the main menu.</li>
   *     </ul>
   * </ol>
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
   * Method that collects user input for setting the current time in the application.
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
   * Method that collects user input for updating the current time in the application.
   *
   * <p>Prompts the user to input a new current time.
   * using the method {@link #timeInput()}
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
    boolean trackExit = false;
    while (!trackExit){
        System.out.println("Enter train number (0 to exit): ");
      int trainNumber = numberInput();
        if (trainNumber == 0) {
            trackExit = true;
        }
        else{
          if (trainRegistry.departureFromNumber(trainNumber) != null) {
            System.out.println("Enter track: ");
            int track = numberInput();
            trainRegistry.assignTrack(trainNumber, track);
            trackExit = true;
          } else {
            System.out.println(NUMBER_NOT_FOUND);
          }
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
    boolean delayExit = false;
    while (!delayExit){
      System.out.println("Enter train number (0 to exit): ");
        int trainNumber = numberInput();
        if (trainNumber == 0) {
            delayExit = true;
        }
        else{
          if (trainRegistry.departureFromNumber(trainNumber) != null) {
            System.out.println("Enter delay (HH:mm): ");
            LocalTime delay = timeInput();
            trainRegistry.addDelay(trainNumber, delay);
            delayExit = true;
          } else {
            System.out.println(NUMBER_NOT_FOUND);
          }
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
   * (Separate method for this because a line can contain digits and letters).
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
   *     <li>Input sent to
   *     {@link TrainRegistry#checkDepartureTimeExistsLine(String, LocalTime)}</li>
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
   * <p>if the input is valid, the loop exits and the time is returned.
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
   *<p>Prompts the user to input a number, which is validated in a try catch block,
   * by checking if the input is a number.
   * the try catch is in a while loop, so if the input is invalid,
   * the user is prompted to input a new number.
   *
   * <p>if the input is valid, the loop exits and the number is returned.
   *
   * @return number the number input by the user.
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
   * Method that used to gather user input for a text.
   *
   *<p>Prompts the user to input a text, which is validated in a while loop,
   * using a pattern matcher to check if the input contains digits.
   * if the input is invalid, prompts the user for a new text.
   * if the input is valid, the loop exits and the text is returned.
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
