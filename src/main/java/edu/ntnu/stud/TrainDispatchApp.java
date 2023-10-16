package edu.ntnu.stud;
import java.util.concurrent.TimeUnit;

public class TrainDispatchApp {
  public static void main(String[] args) {
    /*
    *Create a new user interface and start it
     */
    TrainDepartureUserInterface userInterface = new TrainDepartureUserInterface();
    System.out.println("Initializing...");
    userInterface.init();
    System.out.println("Starting...");
    userInterface.start();
    }
}
