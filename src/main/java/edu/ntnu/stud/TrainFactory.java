package edu.ntnu.stud;
import java.sql.Date;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TrainFactory {
  Scanner input = new Scanner(System.in);

  /*
   *Create a new departure using user input
   */
  public TrainDeparture addDeparture() {
    /*
    *Create a new departure using user input
    */
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    LocalTime departureTime = null;
    LocalTime delay = null;
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
    String destination = input.nextLine();
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    System.out.println("Enter track: ");
    int track = input.nextInt();
    while (true) {
      System.out.println("Enter delay: ");
      String delayString = input.nextLine();
      try {
        delay = LocalTime.parse(delayString, formatter);
        break;
      } catch (Exception e) {
        System.out.println("Invalid time format");
      }
    }
    TrainDeparture trainDeparture = new TrainDeparture(departureTime, line, destination, trainNumber, track, delay);
    return trainDeparture;
  }
}
