package edu.ntnu.stud;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class that represents a registry of TrainDeparture objects.
 * <ul>
 *     <li>Has methods for adding, removing, and modifying TrainDeparture objects.</li>
 *     <li>Has methods that are used for searching for specific TrainDeparture objects</li>
 * </ul>
 *
 *
 *<p>No direct user interaction, all methods receive parameters from
 * {@link TrainDepartureUserInterface}
 *
 * @see TrainDeparture
 * @see TrainDepartureUserInterface
 */
public class TrainRegistry {
  private final HashMap<Integer, TrainDeparture> numberToDepartureMap = new HashMap<>();
  private final ArrayList<TrainDeparture> trainDepartureList
      = new ArrayList<>(numberToDepartureMap.values());
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  private LocalTime currentTime = null;

  /**
   * Method that returns the TrainDepartureList.
   *
   * @return trainDepartureList ArrayList with TrainDeparture objects.
   */
  public List<TrainDeparture> getTrainDepartures() {
    return trainDepartureList;
  }

  /**
   * Method that returns the numberToDepartureMap.
   *
   * <p>Used for testing purposes.
   * @return numberToDepartureMap HashMap with TrainDeparture objects.
   */
  public Map<Integer, TrainDeparture> getNumberToDepartureMap() {
      return numberToDepartureMap;
    }

  /**
   * Method that adds a new TrainDeparture object to the system.
   *
   *<p>Creates a new trainDeparture using the given parameters,
   * and adds it to the numberToDepartureMap.
   * also calls{@link #updateTrainDepartureList()}
   *
   * <p>Receives parameters from
   * {@link TrainDepartureUserInterface#addTrainDeparture()}
   *
   *<ul>
   *     <li>If the track is 0, the constructor for
   *     {@link TrainDeparture} without track is used.</li>
   *     <li>If the track is not 0, the constructor for
   *     {@link TrainDeparture} with track is used.</li>
   *</ul>
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
   * Method that sets the track of a specific TrainDeparture.
   *
   *<p>Receives parameters from,
   * {@link TrainDepartureUserInterface#addDelay()}
   * <ul>
   *   <li>Selects TrainDeparture using {@link #departureFromNumber(int)}
   *   and sets the track of the TrainDeparture to the given track</li>
   * </ul>
   *
   * @param trainNumber the train number of the train departure to be assigned a delay.
   * @param track the delay to be assigned to the train departure.
   */
  public void assignTrack(int trainNumber, int track) {
    departureFromNumber(trainNumber).setTrack(track);
  }

  /**
   * Method that sets the delay of a specific TrainDeparture.
   *
   *<p>Receives parameters from,
   * {@link TrainDepartureUserInterface#addDelay()}
   * <ul>
   *   <li>Selects TrainDeparture using {@link #departureFromNumber(int)}
   *   and sets the delay of the TrainDeparture to the given delay</li>
   * </ul>
   *
   * @param trainNumber the train number of the train departure to be assigned a delay.
   * @param delay       the delay to be assigned to the train departure.
   */
  public void addDelay(int trainNumber, LocalTime delay) {
    departureFromNumber(trainNumber).setDelay(delay);
  }

  /**
   * Method that find a specific TrainDeparture using the train number.
   *
   *<p>Receives a train number parameter from
   * {@link TrainDepartureUserInterface#departureFromNumber()}
   * <ul>
   *     <li>If the train number is found in {@link #numberToDepartureMap}
   *      returns the TrainDeparture associated with the train number.</li>
   *      <li>If the train number is not found, returns {@code null}</li>
   * </ul>
   *
   * @param trainNumber the train number for the TrainDeparture to be found.
   * @return a TrainDeparture object with the given train number.
   *         or {@code null} if the key/value pair is not found.
   */
  public TrainDeparture departureFromNumber(int trainNumber) {
    try {
      return numberToDepartureMap.get(trainNumber);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Method that finds all TrainDepartures with a specific destination.
   *
   *<p>Receives a destination parameter from
   * {@link TrainDepartureUserInterface#departureFromDestination()}
   * filters {@link #trainDepartureList} by destination
   *
   *<p>Ignores case sensitivity, creates a list of all found departures
   *
   * @param destination the destination of the train departure to be found.
   * @return a list with TrainDeparture objects that have the specified destination.
   *         returns {@code null} if the list is empty.
   */
  public List<TrainDeparture> departureFromDestination(String destination) {
    List<TrainDeparture> foundDepartures = trainDepartureList.stream()
            .filter(trainDeparture -> trainDeparture.getDestination().equalsIgnoreCase(destination))
            .toList();

    return foundDepartures.isEmpty() ? null : foundDepartures;
  }

  /**
   * Method that sets the current time of the application.
   *
   *<p>Receives a time parameter from
   * {@link TrainDepartureUserInterface#setCurrentTime()}
   * and sets the current time to the given time.
   *
   * @param time the time to be set as current time.
   */
  public void setCurrentTime(LocalTime time) {
    currentTime = time;
  }

  /**
   * Method that returns the current time of the application.
   *
   * @return currentTime
   */
  public LocalTime getCurrentTime() {
    return currentTime;
  }

  /**
   * Method that removes all departures that have departed.
   *
   *<p>This method uses a stream to check if any train departures have departed.
   * <ul>
   *   <li>Identifies departures where departure time + delay is less then the current time</li>
   *   <li>Adds these departures to a List "toRemove"</li>
   *   <li>Removes all departures in "toRemove" from {@link #numberToDepartureMap}</li>
   *   and {@link #trainDepartureList}</li>
   * </ul>
   */
  public void removeDeparted() {
    List<Integer> toRemove = numberToDepartureMap.keySet().stream()
            .filter(key -> numberToDepartureMap.get(key).getDepartureTimeWithDelay()
              .isBefore(currentTime))
            .toList();
    toRemove.forEach(numberToDepartureMap::remove);

    updateTrainDepartureList();
  }

  /**
   * Method that sorts {@link #trainDepartureList} by departure time.
   *
   *<p>this method uses  class to sort the list.
   * note that the {@link #numberToDepartureMap} is not sorted, as it is not used in the
   * user interface.
   */
  public void sortByDepartureTime() {
    trainDepartureList.sort(Comparator.comparing(TrainDeparture::getDepartureTime));
  }

  /**
   * Method that returns the departure times for all departures on a specific line.
   *
   *<ul>
   *   <li>Receives a line parameter from {@link TrainDepartureUserInterface}</li>
   *   <li>Filters {@link #trainDepartureList} by line</li>
   *   <li>Collects all LocalTime values to a list,
   *   the list is later returned</li>
   *</ul>
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
   * Method that returns the departure times for all departures on a specific track.
   *
   *<p><ul>
   *   <li>Receives a track parameter from {@link TrainDepartureUserInterface}</li>
   *   <li>Filters {@link #trainDepartureList} by track</li>
   *   <li>Collects all LocalTime values to a list,
   *   the list is later returned.</li>
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
   * Method that checks if a given departure time exists on a given track.
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
   * Method that checks if a given departure time exists on a given line.
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
   * Method that updates {@link #trainDepartureList}. So that it reflects the values in
   * {@link #numberToDepartureMap}.
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
   * Method that reads a file and creates {@link TrainDeparture} objects from the data.
   *
   *<p>Reads "TrainDepartureData.txt" from the current working directory.
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
  public void fillTrainDepartureListFromFile() {
    Path filePath = Paths.get("src/main/resources/TrainDepartureData.txt");
    try (BufferedReader reader = Files.newBufferedReader(filePath)) {
      reader.readLine(); // skip first line

      String line = reader.readLine().trim();
      while (line != null) {
        String[] data = line.split(",");
        LocalTime departureTime = LocalTime.parse(data[0], formatter);
        String lineName = data[1].trim();
        String destination = data[2].trim();
        int trainNumber = Integer.parseInt(data[3].trim());
        int track = Integer.parseInt(data[4].trim());
        LocalTime delay = LocalTime.parse(data[5].trim(), formatter);
        line = reader.readLine();
        TrainDeparture trainDeparture = new TrainDeparture(departureTime, lineName, destination,
            trainNumber, track, delay);
        numberToDepartureMap.put(trainNumber, trainDeparture);
        updateTrainDepartureList();
        sortByDepartureTime();
      }
    } catch (IOException e) {
        System.out.println("Error reading file");
    }
  }

}

