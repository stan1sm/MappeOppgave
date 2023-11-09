package edu.ntnu.stud;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * The TrainFactory represents a registry for train departures.
 * it is responsible for storing all created train departures and
 * performing actions on them which alter their state.
 *
 */
public class TrainFactory {
  public ArrayList<TrainDeparture> trainDepartureList = new ArrayList<>();
  public HashMap<Integer, TrainDeparture> trainNumberMap = new HashMap<>();
  public Collection<TrainDeparture> values = trainNumberMap.values();
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  private LocalTime currentTime = null;

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
   * @param departureTime the departure time of the train
   * @param line the line the train is operating on
   * @param destination the destination of the train
   * @param trainNumber the train number of the train
   * @param track the track number of the train
   * @param delay the delay of the train
   */
  public void addDeparture(LocalTime departureTime, String line, String destination, int trainNumber, int track, LocalTime delay) {
    TrainDeparture trainDeparture;
    if (track == 0){
      trainDeparture = new TrainDeparture(departureTime, line, destination, trainNumber, delay);
    }else{
      trainDeparture = new TrainDeparture(departureTime, line, destination, trainNumber, track, delay);
    }
    trainDepartureList.add(trainDeparture);
  }

  /**
   * Finds a specific train-departure using the train number,
   * and sets its track to the given track.
   *
   * @param trainNumber the train number of the train departure to be assigned a track.
   * @param track       the track number to be assigned to the train departure.
   */
  public void assignTrack(int trainNumber, int track) {
    departureFromNumber(trainNumber).setTrack(track);
    departureFromNumber(trainNumber);
  }

  public ArrayList<TrainDeparture> testLoop(String destination){
    Iterator<TrainDeparture> iterator = values.iterator();
    ArrayList<TrainDeparture> foundDepartures = new ArrayList<>();
    while(iterator.hasNext()){
      if (iterator.next().getDestination().equalsIgnoreCase(destination)){
        foundDepartures.add(iterator.next());
      }
    }
    return foundDepartures;
  }

  /**
   * Finds a specific train-departure using the train number, and sets its delay to the given delay.
   *
   * @param trainNumber the train number of the train departure to be assigned a delay.
   * @param delay       the delay to be assigned to the train departure.
   */
  public void addDelay(int trainNumber, LocalTime delay) {
    departureFromNumber(trainNumber).setDelay(delay);
    departureFromNumber(trainNumber);
  }

  /**
   * Loops through the TrainDeparture list and find a departure using the train number.
   * returns null if a departure with the given train number doesn't exist.
   * @param trainNumber the train number for the train-departure to be found.
   * @return trainDeparture
   */

  public TrainDeparture departureFromNumber(int trainNumber) {
    for(TrainDeparture trainDeparture : trainDepartureList){
      if(trainDeparture.getTrainNumber() == trainNumber) {
        return trainDeparture;
      }
    }
    return null;
  }

  /**
   * returns a train departure using the destination as key.
   * returns null if a train departure with the given destination doesn't exist.
   *
   * @param destination the destination of the train departure to be found.
   * @return trainDeparture
   */
  public ArrayList<TrainDeparture> departureFromDestination(String destination) {
    ArrayList<TrainDeparture> foundDepartures = new ArrayList<>();
    this.trainDepartureList.forEach((traindeparture)-> {
      if (traindeparture.getDestination().equalsIgnoreCase(destination)) {
        foundDepartures.add(traindeparture);
      }
    });
    if (foundDepartures.isEmpty()){
      return null;
    }else{
      return foundDepartures;
    }
  }

    /**
     * Sets the current time to the given time.
     * @param time the time to be set as current time.
     */
  public void setCurrentTime(LocalTime time) {
    currentTime = time;
  }

    /**
     * Returns the current time.
     * @return currentTime
     */
  public LocalTime getCurrentTime() {
    return currentTime;
  }

  /**
   * Loops through the TrainDeparture list and removes train departures,
   * if their departure time + delay is before the current time.
   * also removes these departures from the trainDestinationMap.
   */
  //FOR THE REPORT: Used iterator to avoid ConcurrentModificationException
  public void removeDeparted(){
      trainDepartureList.removeIf(trainDeparture -> trainDeparture.getDepartureTimeWithDelay().isBefore(currentTime));
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
        trainNumberMap.put(trainNumber, trainDeparture);
      }
    } catch (Exception e) {
      System.out.println("File not found");
    }
  }


}

