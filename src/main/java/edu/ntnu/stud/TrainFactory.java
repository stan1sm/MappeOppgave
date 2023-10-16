package edu.ntnu.stud;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


public class TrainFactory {
  ArrayList<TrainDeparture> trainDepartureList = new ArrayList<>();
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  SystemTime systemTime = null;
  Scanner input = new Scanner(System.in);
  LocalTime delay = null;
  LocalTime departureTime = null;
  String line = null;

  public ArrayList<TrainDeparture> getTrainDepartureList() {
    return trainDepartureList;
  }


  public TrainDeparture addDeparture() {
    /*
    *Create a new departure using user input
    */
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
    line = input.nextLine();
    System.out.println("Enter destination: ");
    String destination = input.nextLine();
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    System.out.println("Enter track: ");
    int track = input.nextInt();
    while (true) {
      System.out.println("Enter delay: ");
      input.nextLine(); //consume newline
      String delayString = input.nextLine();
      try {
        delay = LocalTime.parse(delayString, formatter);
        break;
      } catch (Exception e) {
        System.out.println("Invalid time format");
      }
    }
    TrainDeparture trainDeparture = new TrainDeparture(departureTime, line, destination, trainNumber, track, delay);
    trainDepartureList.add(trainDeparture);
    return trainDeparture;
  }

  public void assignTrack() {
    /*
    *Assign a track to a departure
   */
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    for (TrainDeparture trainDeparture : trainDepartureList) {
      if (trainDeparture.getTrainNumber() == trainNumber) {
        System.out.println("Enter track: ");
        int track = input.nextInt();
        trainDeparture.setTrack(track);
      } else {
        System.out.println("Train number not found");
        assignTrack();
      }
    }
  }

  public void addDelay() {
    /*
    *Add delay to a departure
    */
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    for (TrainDeparture trainDeparture : trainDepartureList) {
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

      } else {
        System.out.println("Train number not found");
      }
    }
  }

  public String departureFromNumber() {
    /*
    *Search for a departure by train number
    */
    System.out.println("Enter train number: ");
    int trainNumber = input.nextInt();
    for (TrainDeparture trainDeparture : trainDepartureList) {
      if (trainDeparture.getTrainNumber() == trainNumber) {
        return trainDeparture.toString();
      } else {
        System.out.println("Train number not found");
      }
    }
    return null;
  }

  public String departureFromDestination() {
    /*
    *Search for a departure by destination
    */
    System.out.println("Enter destination: ");
    String destination = input.nextLine();
    for (TrainDeparture trainDeparture : trainDepartureList) {
      if (trainDeparture.getDestination().equalsIgnoreCase(destination)) {
        return trainDeparture.toString();
      } else {
        System.out.println("Destination not found");
      }
    }
    return null;
  }

  public void updateSystemTime() {
    /*
    *Update the system time
    */
    System.out.println("Enter new system time (HH:mm): ");
    String newSystemTimeString = input.nextLine();
    try {
      LocalTime newSystemTime = LocalTime.parse(newSystemTimeString, formatter);
      systemTime.setSystemTime(newSystemTime);
    } catch (Exception e) {
      System.out.println("Invalid time format");
    }
  }

  public void fillTrainDepartureList() {
    Scanner read = new Scanner("Data.txt");
    read.nextLine();
    while (read.hasNextLine()) {
      String line = read.nextLine();
      String[] data = line.split(",");
      LocalTime departureTime = LocalTime.parse(data[0], formatter);
      String lineName = data[1];
      String destination = data[2];
      int trainNumber = Integer.parseInt(data[3]);
      int track = Integer.parseInt(data[4]);
      LocalTime delay = LocalTime.parse(data[5], formatter);
      TrainDeparture trainDeparture = new TrainDeparture(departureTime, lineName, destination, trainNumber, track, delay);
      trainDepartureList.add(trainDeparture);
    }
  }
}
