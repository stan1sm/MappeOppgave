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
import java.util.stream.Collectors;


/**
 * The TrainFactory represents a registry for train departures.
 * it is responsible for storing all created train departures and
 * performing actions on them which alter their state.
 *
 */
public class TrainFactory {
  private final HashMap<Integer, TrainDeparture> numberToDepartureMap = new HashMap<>();
  private final ArrayList<TrainDeparture> trainDepartures
      = new ArrayList<>(numberToDepartureMap.values());
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  private LocalTime currentTime = null;

  /**
   * Returns the ArrayList of train departures.
   *
   * @return trainDepartureList
   */
  public ArrayList<TrainDeparture> getTrainDepartures() {
    return trainDepartures;
  }

  /**
   * Creates a new trainDeparture using the given parameters,
   * and adds it to the numberToDepartureMap.
   * also calls the {@link #updateTrainDepartureList()}
   * receives parameters from
   * {@link TrainDepartureUserInterface#addTrainDeparture()}
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
    updateTrainDepartureList();
  }

  /**
   * Finds a specific train-departure using the train number,
   * and sets its track to the given track.
   * receives parameters from
   * {@link TrainDepartureUserInterface#assignTrack()}
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
   * receives parameters from
   * {@link TrainDepartureUserInterface#addDelay()}
   *
   * @param trainNumber the train number of the train departure to be assigned a delay.
   * @param delay       the delay to be assigned to the train departure.
   */
  public void addDelay(int trainNumber, LocalTime delay) {
    departureFromNumber(trainNumber).setDelay(delay);
    departureFromNumber(trainNumber);
  }

  /**
   * Receives a train number parameter from
   * {@link TrainDepartureUserInterface#departureFromNumber()}
   * and returns the train departure with the given train number.
   * returns null if a train departure with the given train number doesn't exist.
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
   * Receives a destination parameter from
   * {@link TrainDepartureUserInterface#departureFromDestination()}
   * Uses a lambda expression to loop through the train departure list,
   * and add all train departures with the given destination to a new list,
   * and returns the list of trainDepartures with the given destination.
   * if the list is empty, the method returns null.
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
   * Receives a time parameter from the setCurrentTime() method,
   * in the user interface class.
   * and sets the current time to the given time.
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
   * the numberToDepartureMap keys for these values are added to a List,
   * and then the values with these keys are removed from the numberToDepartureMap.
   */
  public void removeDeparted() {
    List<Integer> toRemove = new ArrayList<>();
    numberToDepartureMap.forEach((key, value) -> {
      if (value.getDepartureTimeWithDelay().isBefore(currentTime)) {
        toRemove.add(key);
      }
    });
    toRemove.forEach(numberToDepartureMap::remove);

    trainDepartures.removeIf(trainDeparture ->
            trainDeparture.getDepartureTimeWithDelay().isBefore(currentTime));
  }

  /**
   * Sorts the train departure list by ascending departure time,
   * using the TrainDepartureComparator class.
   */
  public void sortByDepartureTime() {
    trainDepartures.sort(new TrainDepartureComparator());
  }

  /**
   * Receives a line parameter from the user interface class,
   * uses a stream to check if there are any train departures with the given line.
   * if there are, the method returns true, if not, it returns false.
   *
   * @param line the line to be checked for.
   * @return boolean
   */
  public boolean checkLineExists(String line) {
    return trainDepartures.stream()
            .anyMatch(trainDeparture -> trainDeparture.getLine().equalsIgnoreCase(line));
  }

  /**
   * Receives a track parameter from the user interface class,
   * uses a stream to check if there are any train departures with the given track.
   * if there are, the method returns true, if not, it returns false.
   *
   * @param track the track to be checked for.
   * @return boolean
   */
  public boolean checkTrackExists(int track) {
    return trainDepartures.stream()
            .anyMatch(trainDeparture -> trainDeparture.getTrack() == track);
  }

  /**
   * Receives a line parameter from the user interface class,
   * adds the departure times of departures with the given line to a list,
   * and returns the list.
   * if the list is empty, the method returns null.
   *
   * @param line the line of which departure times are to be found.
   * @return departureTimes
   */
  public ArrayList<LocalTime> departureTimesFromLine(String line) {
    ArrayList<LocalTime> departureTimes = new ArrayList<>();
    trainDepartures.forEach((trainDeparture) -> {
      if (trainDeparture.getLine().equalsIgnoreCase(line)) {
        departureTimes.add(trainDeparture.getDepartureTime());
      }
    });
    if (departureTimes.isEmpty()) {
      return null;
    } else {
      return departureTimes;
    }
  }

  /**
   * Receives a track parameter from the user interface class,
   * adds the departure times of departures with the given track to a list,
   * and returns the list.
   * if the list is empty, the method returns null.
   *
   * @param track the track of which departure times are to be found.
   * @return departureTimes
   */
  public ArrayList<LocalTime> departureTimesFromTrack(int track) {
    ArrayList<LocalTime> departureTimes = new ArrayList<>();
    trainDepartures.forEach((trainDeparture) -> {
      if (trainDeparture.getTrack() == track) {
        departureTimes.add(trainDeparture.getDepartureTime());
      }
    });
    if (departureTimes.isEmpty()) {
      return null;
    } else {
      return departureTimes;
    }
  }

  /**
   * Returns true if there are any trainDepartures on the given track,
   * that have the given departure time.
   * if not, the method returns false.
   *
   * @param track the track to be checked for.
   * @param time the departure time to be checked for.
   * @return boolean
   */
  public boolean checkDepartureTimeExistsTrack(int track, LocalTime time) {
    return departureTimesFromTrack(track).contains(time);
  }

  /**
   * Returns true if there are any trainDepartures on the given line,
   * that have the given departure time.
   * if not, the method returns false.
   *
   * @param line the line to be checked for.
   * @param time the departure time to be checked for.
   * @return boolean
   */
  public boolean checkDepartureTimeExistsLine(String line, LocalTime time) {
    return departureTimesFromLine(line).contains(time);
  }

  /**
   * clears the TrainDepartures ArrayList,
   * and then adds all values from the numberToDepartureMap to the ArrayList.
   */
  private void updateTrainDepartureList() {
    trainDepartures.clear();
    trainDepartures.addAll(numberToDepartureMap.values());
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
        updateTrainDepartureList();
      }
    } catch (Exception e) {
      System.out.println("File not found");
    }
  }

  /**
   * Comparator class for sorting train departures by departure time.
   * Used in the sortByDepartureTime() method.
   */
  public static class TrainDepartureComparator implements Comparator<TrainDeparture> {
    @Override
    public int compare(TrainDeparture o1, TrainDeparture o2) {
      return o1.getDepartureTime().compareTo(o2.getDepartureTime());
    }
  }
}

