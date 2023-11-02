package edu.ntnu.stud;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * The TrainFactory represents a registry for train departures.
 * it is responsible for storing all created train departures and
 * performing actions on them which alter their state.
 *
 */
public class TrainFactory {
  ArrayList<TrainDeparture> trainDepartureList = new ArrayList<>();
  HashMap<String, TrainDeparture> trainDestinationMap = new HashMap<>();
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  LocalTime currentTime = null;

  /**
   * Returns the list of train departures.
   * @return trainDepartureList
   */
  public ArrayList<TrainDeparture> getTrainDepartureList() {
    return trainDepartureList;
  }

  /**
   * Creates a new trainDeparture using the given parameters and adds it to the list of train departures.
   * also adds the train departure to the hashmap with the destination as key.
   * receives parameters from the addDeparture() method in the user interface class.
   *
   * @param departureTime the departuretime of the train
   * @param line the line the train is operating on
   * @param destination the destination of the train
   * @param trainNumber
   * @param track
   * @param delay
   */
  public void addDeparture(LocalTime departureTime, String line, String destination, int trainNumber, int track, LocalTime delay) {
    TrainDeparture trainDeparture;
    if (track == 0){
      trainDeparture = new TrainDeparture(departureTime, line, destination, trainNumber, delay);
    }else{
      trainDeparture = new TrainDeparture(departureTime, line, destination, trainNumber, track, delay);
    }
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

  public void removeDeparted(){
    for(TrainDeparture trainDeparture : trainDepartureList){
      if(trainDeparture.getDepartureTimeWithDelay().isBefore(currentTime)){
        trainDepartureList.remove(trainDeparture);
        trainDestinationMap.remove(trainDeparture.getDestination());
      }
    }
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
        trainDepartureList.add(trainDeparture);
      }
    } catch (Exception e) {
      System.out.println("File not found");
    }
  }


}

