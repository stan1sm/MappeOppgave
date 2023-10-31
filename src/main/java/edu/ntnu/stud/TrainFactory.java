package edu.ntnu.stud;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * A factory for creating train departures.
 */
public class TrainFactory {
  ArrayList<TrainDeparture> trainDepartureList = new ArrayList<>();
  HashMap<Integer, TrainDeparture> trainNumberMap = new HashMap<>();
  HashMap<String, TrainDeparture> trainDestinationMap = new HashMap<>();
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  LocalTime currentTime = null;

  public ArrayList<TrainDeparture> getTrainDepartureList() {
    return trainDepartureList;
  }

  public HashMap<String, TrainDeparture> getTrainDestinationMap() {
    return trainDestinationMap;
  }

  /**
   *Create a new departure using user input.
   */
  public void addDeparture(LocalTime departureTime, String line, String destination, int trainNumber, int track, LocalTime delay) {
    TrainDeparture trainDeparture = new TrainDeparture(departureTime, line, destination, trainNumber, track, delay);
    trainDepartureList.add(trainDeparture);
    trainDestinationMap.put(destination, trainDeparture);
  }

  public TrainDeparture assignTrack(int trainNumber, int track) {
    departureFromNumber(trainNumber).setTrack(track);
    return departureFromNumber(trainNumber);
  }

  public TrainDeparture addDelay(int trainNumber, LocalTime delay) {
    /*
    *Add delay to a departure
    */
    departureFromNumber(trainNumber).setDelay(delay);
    return departureFromNumber(trainNumber);
  }

  public TrainDeparture departureFromNumber(int trainNumber) {
    for(TrainDeparture trainDeparture : trainDepartureList){
      if(trainDeparture.getTrainNumber() == trainNumber) {
        return trainDeparture;
      }
    }
    return null;
  }

  public TrainDeparture departureFromDestination(String destination) {
    try{
      return trainDestinationMap.get(destination);
    }catch (NoSuchElementException e){
      System.out.println("A train with this destination doesnt exist");
    }
    return null;
  }

  public void setCurrentTime(LocalTime time) {
    currentTime = time;
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
        trainDestinationMap.put(destination,trainDeparture);
        trainNumberMap.put(trainNumber, trainDeparture);
        trainDepartureList.add(trainDeparture);
      }
    } catch (Exception e) {
      System.out.println("File not found");
    }
  }


}

