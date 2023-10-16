package edu.ntnu.stud;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TrainFactory {
  ArrayList<TrainDeparture> TrainDepartureList = new ArrayList<>();
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  Scanner input = new Scanner(System.in);
  LocalTime delay = null;
  public TrainDeparture addDeparture() {
    /*
    *Create a new departure using user input
    */
    LocalTime departureTime = null;
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
    TrainDepartureList.add(trainDeparture);
    return trainDeparture;
  }

  public TrainDeparture assignTrack(){
    /*
    *Assign a track to a departure
    */
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    for(TrainDeparture trainDeparture : TrainDepartureList) {
      if (trainDeparture.getTrainNumber() == trainNumber) {
        System.out.println("Enter track: ");
        int track = input.nextInt();
        trainDeparture.setTrack(track);
        return trainDeparture;
      }else {
        System.out.println("Train number not found");
      }
    }
    return null;
  }

  public TrainDeparture addDelay(){
    /*
    *Add delay to a departure
    */
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    for(TrainDeparture trainDeparture : TrainDepartureList) {
      if (trainDeparture.getTrainNumber() == trainNumber) {
        System.out.println("Enter delay (HH:mm): ");
        String delayString = input.nextLine();
        try {
          delay = LocalTime.parse(delayString, formatter);
          break;
        } catch (Exception e) {
          System.out.println("Invalid time format");
        }
        trainDeparture.setDelay(delay);
        return trainDeparture;
      }else {
        System.out.println("Train number not found");
      }
    }
    return null;
  }
}
