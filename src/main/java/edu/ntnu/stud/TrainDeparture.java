package edu.ntnu.stud;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.regex.Pattern;

/**
 * A class that represents a train departure.
 */
public class TrainDeparture {


  private LocalTime departureTime;
  private final String line;
  private final String destination;
  private final int trainNumber;
  private int track;
  private LocalTime delay;
  private static final Pattern timePattern = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");


  /**
   * @param departureTime The time of departure.
   * @param line The line the train is on.
   * @param destination The destination of the train.
   * @param trainNumber The train number.
   * @param track The track the train is on.
   * @param delay The delay of the train.
   * @throws DateTimeException if the departure time or delay is not in the format HH:mm.
   * @throws NullPointerException if the destination is null.
   */
  TrainDeparture(LocalTime departureTime, String line, String destination,
          int trainNumber, int track, LocalTime delay) {
    if (!timePattern.matcher(departureTime.toString()).matches()) {
      throw new DateTimeException("Departure time must be in the format HH:mm");
    }
    if (!timePattern.matcher(delay.toString()).matches()) {
      throw new DateTimeException("Delay must be in the format HH:mm");
    }
    if (destination == null) {
      throw new NullPointerException("Destination cannot be null");
    }
    this.departureTime = departureTime;
    this.line = line;
    this.destination = destination;
    this.trainNumber = trainNumber;
    this.track = track;
    this.delay = delay;
  }

  /**
   * Returns noe...
   *
   * @param departureTime The time of departure
   * @param line The line the train is on
   * @param destination The destination of the train
   * @param trainNumber The train number
   * @param delay The delay of the train
   */
  TrainDeparture(LocalTime departureTime, String line, String destination,
                 int trainNumber, LocalTime delay) {
    this(departureTime, line, destination, trainNumber, -1, delay);
  }


  public LocalTime getDepartureTime() {
    return departureTime;
  }

  public String getLine() {
    return line;
  }

  public String getDestination() {
    return destination;
  }

  public int getTrainNumber() {
    return trainNumber;
  }

  /**
   * @param track The track the train is on.
   * @throws IllegalArgumentException if the track is less than -1.
   */
  public void setTrack(int track) {
    if (track <= 0) {
      throw new IllegalArgumentException("Track cannot be less than 0");
    } else {
      this.track = track;
    }
  }

  /**
   * @param delay The delay of the train.
   * @throws DateTimeException if the delay is not in the format HH:mm.
   */
  public void setDelay(LocalTime delay) {
    if (delay == LocalTime.of(0, 0)) {
      throw new DateTimeException("Delay cannot be null");
    }
    this.delay = delay;
  }

  public int getTrack() {
    return track;
  }

  public LocalTime getDelay() {
    return delay;
  }

  public LocalTime getDepartureTimeWithDelay() {
    return departureTime.plusHours(delay.getHour()).plusMinutes(delay.getMinute());
  }


  /**
   *  Returns a string representation of the train departure.
   */
  public String toString() {
    StringBuilder info = new StringBuilder();
    int destinationLength = destination.length();
    info.append("| ").append(departureTime).append(String.format("%" + 4 + "s", " | "));
    info.append(destination).append(String.format("%" + (15 - destinationLength) + "s", " | "));
    info.append(track).append(String.format("%" + 8 + "s", " | "));
    info.append(line).append(String.format("%" + 7 + "s", " | "));
    info.append(trainNumber).append(String.format("%" + 15 + "s", " | "));
    info.append(delay).append(String.format("%" + 7 + "s", "|")).append("\n");
    return info.toString();
  }

}
