package edu.ntnu.stud;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
  private final ArrayList<TrainDeparture> trainDepartureList
      = new ArrayList<>(numberToDepartureMap.values());
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  private LocalTime currentTime = null;

  /**
   * Returns the ArrayList of train departures.
   *
   * @return trainDepartureList
   */
  public ArrayList<TrainDeparture> getTrainDepartures() {
    return trainDepartureList;
  }

  /**
   * Adds a new train departure with provided parameters to the system.
   *
   *
   *<p>Creates a new trainDeparture using the given parameters,
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
   *
   * Receives a destination parameter from
   * {@link TrainDepartureUserInterface#departureFromDestination()}
   * converts the trainDepartureList ArrayList to a stream, checks Objects
   * in the stream for the given destination, and collects them to a list.
   * if the list is empty, the method returns null.
   * if not, the method returns the list.
   *
   * @param destination the destination of the train departure to be found.
   * @return trainDeparture
   */
  public List<TrainDeparture> departureFromDestination(String destination) {
    List<TrainDeparture> foundDepartures = trainDepartureList.stream()
            .filter(trainDeparture -> trainDeparture.getDestination().equalsIgnoreCase(destination))
            .collect(Collectors.toList());

    return foundDepartures.isEmpty() ? null : foundDepartures;
  }

  /**
   * Receives a time parameter from
   * {@link TrainDepartureUserInterface#setCurrentTime()}
   *and sets the current time to the given time.
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
   * Removed departures that have departed from the system.
   *
   *<p>This method uses a stream to check if any train departures have departed.
   * <ul>
   *   <li>Identifies departures where departure time + delay is less then the current time</li>
   *   <li>Adds these departures to a List "toRemove"</li>
   *   <li>Removes all departures in "toRemove" from the numberToDepartureMap
   *   and the "trainDepartureList" arraylist</li>
   * </ul>
   */
  public void removeDeparted() {
    List<Integer> toRemove = numberToDepartureMap.keySet().stream()
            .filter(key -> numberToDepartureMap.get(key).getDepartureTimeWithDelay()
              .isBefore(currentTime))
            .toList();
    toRemove.forEach(numberToDepartureMap::remove);

    trainDepartureList.removeIf(trainDeparture ->
            trainDeparture.getDepartureTimeWithDelay().isBefore(currentTime));
  }

  /**
   * Sorts the train departure list by ascending departure time,
   *
   *<p>this method uses the {@link TrainDepartureComparator} class to sort the list.
   * note that the {@link #numberToDepartureMap} is not sorted, as it is not used in the
   * user interface.
   */
  public void sortByDepartureTime() {
    trainDepartureList.sort(new TrainDepartureComparator());
  }

  /**
   * Returns departure times for departures on a specific line.
   *
   *<p><ul>
   *   <li>Receives a line parameter from {@link TrainDepartureUserInterface}</li>
   *   <li>Filters {@link #trainDepartureList} by line</li>
   *   <li>Collects all LocalTime values to a list</li>
   *   </ul>
   *
   * @param line the line of which departure times are to be found.
   * @return a list of departure times for departures on the given line.
   */
  public List<LocalTime> departureTimesFromLine(String line) {
    return trainDepartureList.stream()
            .filter(trainDeparture -> trainDeparture.getLine().equalsIgnoreCase(line))
            .map(TrainDeparture::getDepartureTime)
            .toList();
  }

  /**
   * Returns departure times for departures on a specific track.
   *
   *<p><ul>
   *   <li>Receives a track parameter from {@link TrainDepartureUserInterface}</li>
   *   <li>Filters {@link #trainDepartureList} by track</li>
   *   <li>Collects all LocalTime values to a list</li>
   *   </ul>
   *
   * @param track the track of which departure times are to be found.
   * @return a list of departure times for departures on the given track.
   */
  public List<LocalTime> departureTimesFromTrack(int track) {
    return trainDepartureList.stream()
      .filter(trainDeparture -> trainDeparture.getTrack() == track)
      .map(TrainDeparture::getDepartureTime)
      .toList();
  }

  /**
   * Checks if a given departure time exists on a given track.
   *
   * <p>Retrieves a list from {@link #departureTimesFromTrack(int)} and checks if the list
   * contains the given departure time.
   *
   * @param track the line to be checked for.
   * @param time the departure time to be checked for.
   * @return {@code true} if the departure time exists on the given track,
   *         {@code false} otherwise.
   */
  public boolean checkDepartureTimeExistsTrack(int track, LocalTime time) {
    return departureTimesFromTrack(track).contains(time);
  }

  /**
   * Checks if a given departure time exists on a given line.
   *
   * <p>Retrieves a list from {@link #departureTimesFromLine(String)} and checks if the list
   * contains the given departure time.
   *
   * @param line the line to be checked for.
   * @param time the departure time to be checked for.
   * @return {@code true} if the departure time exists on the given line,
   *         {@code false} otherwise.
   */
  public boolean checkDepartureTimeExistsLine(String line, LocalTime time) {
    return departureTimesFromLine(line).contains(time);
  }

  /**
   * Updates the train departure list.
   *
   *<p><ul>
   *   <li>Clears the trainDepartureList</li>
   *   <li>Adds all values from {@link #numberToDepartureMap} to the ArrayList</li>
   *   <li>Sorts the {@link #trainDepartureList} using {@link #sortByDepartureTime()}</li>
   *</ul>
   * This method ensures that the {@link #trainDepartureList} correctly reflects
   * the values in {@link #numberToDepartureMap}
   */
  private void updateTrainDepartureList() {
    trainDepartureList.clear();
    trainDepartureList.addAll(numberToDepartureMap.values());
    sortByDepartureTime();
  }

  /**
   * Reads data from a file and adds the information to the system.
   *
   * <p>Creates a new File object from,
   * <a href="\MappeOppgave\TrainDepartureData.txt">TrainDepartureData.txt</a>
   * and a new Scanner object from the file.
   * The first line of the file is skipped, as it contains the column names.
   * Each line is split into an array of strings, and variables are created
   * from the array values. A new {@link TrainDeparture} object is created
   *
   * <p><ul>
   *   <li>Creates a new {@link TrainDeparture} object</li>
   *   <li>Adds this object to {@link #numberToDepartureMap}</li>
   *   <li>Calls {@link #updateTrainDepartureList()}</li>
   *   <li>Calls {@link #sortByDepartureTime()}</li>
   * </ul>
   */
  public void fillTrainDepartureList() {
    try {
      File datafile = new File("TrainDepartureData.txt");
      Scanner read = new Scanner(datafile);
      read.nextLine();
      while (read.hasNextLine()) {
        String line = read.nextLine().trim();
        if (line.isEmpty()) {
          continue;
        }
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
        sortByDepartureTime();
      }
      read.close();
    } catch (Exception e) {
      System.out.println("File not found");
    }
  }

  /**
   * Comparator class for comparing objects by departure time.
   * Used in {@link #sortByDepartureTime()}
   */
  public static class TrainDepartureComparator implements Comparator<TrainDeparture> {
    @Override
    public int compare(TrainDeparture o1, TrainDeparture o2) {
      return o1.getDepartureTime().compareTo(o2.getDepartureTime());
    }
  }
}

