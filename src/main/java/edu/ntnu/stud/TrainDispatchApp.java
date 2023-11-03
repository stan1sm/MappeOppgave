package edu.ntnu.stud;


/**
 * TrainDispatchApp is the main class of the application.
 * It is responsible for initializing and starting the user interface.
 * It also contains the main method.
 */
public class TrainDispatchApp {

  /**
   * The main method of the application.
   * creates a new user interface object, initializes it and starts it.
   * @param args command line arguments
   */
  public static void main(String[] args) {
    TrainDepartureUserInterface userInterface = new TrainDepartureUserInterface();
    System.out.println("Initializing...");
    userInterface.init();
    System.out.println("Starting...");
    userInterface.start();
    }
}
