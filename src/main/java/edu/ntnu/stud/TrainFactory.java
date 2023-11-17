package edu.ntnu.stud;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * The TrainFactory represents a registry for train departures.
 * it is responsible for storing all created train departures and
 * performing actions on them which alter their state.
 *
 */
public class TrainFactory {
  private final HashMap<Integer, TrainDeparture> numberToDepartureMap = new HashMap<>();
  private final Collection<TrainDeparture> trainDepartures = numberToDepartureMap.values();
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  private LocalTime currentTime = null;

  /**
   * Returns the Collection of train departures.
   *
   * @return trainDepartureList
   */
  public Collection<TrainDeparture> getTrainDepartures() {
    return trainDepartures;
  }

  /**
   * Creates a new trainDeparture using the given parameters,
   * and adds it to the list of train departures.
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
  public void addDeparture(LocalTime departureTime, String line,
                           String destination, int trainNumber, int track, LocalTime delay) {
    TrainDeparture trainDeparture;
    if (track == 0) {
      trainDeparture = new TrainDeparture(departureTime, line, destination, trainNumber, delay);
    } else {
      trainDeparture = new TrainDeparture(departureTime, line,
              destination, trainNumber, track, delay);
    }
    numberToDepartureMap.put(trainNumber, trainDeparture);
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
   *
   * @param trainNumber the train number for the train-departure to be found.
   * @return trainDeparture
   */

  public TrainDeparture departureFromNumber(int trainNumber) {
    try {
      return numberToDepartureMap.get(trainNumber);
    } catch (Exception e) {
      return null;
    }
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
    this.trainDepartures.forEach((traindeparture) -> {
      if (traindeparture.getDestination().equalsIgnoreCase(destination)) {
        foundDepartures.add(traindeparture);
      }
    });
    if (foundDepartures.isEmpty()) {
      return null;
    } else {
      return foundDepartures;
    }
  }

  /**
   * Sets the current time to the given time.
   *
   * @param time the time to be set as current time.
   */
  public void setCurrentTime(LocalTime time) {
    currentTime = time;
  }

  /**
   * Returns the current time.
   *
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
  public void removeDeparted() {
    trainDepartures.removeIf(trainDeparture ->
            trainDeparture.getDepartureTimeWithDelay().isBefore(currentTime));
  }

  /**
   * Sorts the train departure list by ascending departure time.
   */
  public void sortByDepartureTime() {
    List<TrainDeparture> departureList = new ArrayList<>(trainDepartures);
    departureList.sort(new TrainDepartureComparator());

    trainDepartures.clear();
    trainDepartures.addAll(departureList);
  }

  public boolean checkLineExists(String line) {
    return trainDepartures.stream()
            .anyMatch(trainDeparture -> trainDeparture.getLine().equalsIgnoreCase(line));
  }

  public boolean checkTrackExists(int track) {
    return trainDepartures.stream()
            .anyMatch(trainDeparture -> trainDeparture.getTrack() == track);
  }

  public ArrayList<LocalTime> departureTimesFromLine(String line) {
    if (checkLineExists(line)){
        ArrayList<LocalTime> departureTimes = new ArrayList<>();
        trainDepartures.forEach((trainDeparture) -> {
            if (trainDeparture.getLine().equalsIgnoreCase(line)) {
            departureTimes.add(trainDeparture.getDepartureTime());
            }
        });
        return departureTimes;
        } else {
        return null;
    }
  }

  public ArrayList<LocalTime> departureTimesFromTrack(int track){
    if (checkTrackExists(track)) {
        ArrayList<LocalTime> departureTimes = new ArrayList<>();
        trainDepartures.forEach((trainDeparture) -> {
            if (trainDeparture.getTrack() == track) {
            departureTimes.add(trainDeparture.getDepartureTime());
            }
        });
        return departureTimes;
        } else {
        return null;
    }
  }

  public boolean checkDepartureTimeExistsTrack(int track, LocalTime time){
    return departureTimesFromTrack(track).contains(time);
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
        TrainDeparture trainDeparture = new TrainDeparture(departureTime, lineName, destination,
                trainNumber, track, delay);
        numberToDepartureMap.put(trainNumber, trainDeparture);
      }
    } catch (Exception e) {
      System.out.println("File not found");
    }
  }
  /**
   * Comparator class for sorting train departures by departure time.
   */

  public static class TrainDepartureComparator implements Comparator<TrainDeparture> {
    @Override
    public int compare(TrainDeparture o1, TrainDeparture o2) {
      return o1.getDepartureTime().compareTo(o2.getDepartureTime());
    }
  }
}

