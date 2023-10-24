package edu.ntnu.stud;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class TrainFactory {
  ArrayList<TrainDeparture> trainDepartureList = new ArrayList<>();
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  Time time = null;
  Scanner input = new Scanner(System.in);
  LocalTime delay = null;
  LocalTime departureTime = null;
  String line = null;
  LocalTime currentTime = null;

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
          trainDeparture.setDelay(delay);
          break;
        } catch (Exception e) {
          System.out.println("Invalid time format");
        }
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

  public void setCurrentTime() {
    System.out.println("Enter current time (HH:mm): ");
    String currentTimeString = input.nextLine();
    currentTime = LocalTime.parse(currentTimeString, formatter);

  }

  public LocalTime getCurrentTime() {
    return currentTime;
  }

  /**
   * Fill the train departure list with data from a file.
   */
  public void fillTrainDepartureList() {
    try {
      File datafile = new File("Data.txt");
      Scanner read = new Scanner(datafile);
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
    } catch (Exception e) {
      System.out.println("File not found");
    }
  }

  /**
   * Print an overview of all departures.
   */
  public void printDepartureOverview() {
    trainDepartureList.removeIf(
        trainDeparture -> trainDeparture.getDepartureTime().isAfter(currentTime));
    String info = "Current time: " + currentTime + "\n";
    info += "+--------+--------------+--------+--------+---------------+-----------+\n";
    info += ("| Time   | Departures   |Track   | Line   |Train Number   |   Delay   |\n");
    info += ("+--------+--------------+--------+--------+---------------+-----------+");
    System.out.println(info);
    for (TrainDeparture trainDeparture : trainDepartureList) {
      System.out.println(trainDeparture);
    }
  }

}

