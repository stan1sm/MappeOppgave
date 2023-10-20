package edu.ntnu.stud;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A user interface for the train departure application.
 */
public class TrainDepartureUserInterface {

  Scanner input = new Scanner(System.in);
  Map<String, Runnable> options = new HashMap<>();
  TrainFactory trainFactory = new TrainFactory();
  ArrayList<TrainDeparture> trainDepartureList = trainFactory.getTrainDepartureList();
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


  /**
   * Initialize the user interface.
   */
  public void init() {
    trainFactory.setCurrentTime();
    System.out.println(trainFactory.getCurrentTime());
    options.put("1", () -> trainDepartureList.forEach(System.out::println));
    options.put("2", trainFactory::addDeparture);
    options.put("3", trainFactory::assignTrack);
    options.put("4", trainFactory::addDelay);
    options.put("5", trainFactory::departureFromNumber);
    options.put("6", trainFactory::departureFromDestination);
    options.put("7", trainFactory::setCurrentTime);
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
}
