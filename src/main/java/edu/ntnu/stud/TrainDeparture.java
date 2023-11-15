package edu.ntnu.stud;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.regex.Pattern;

/**
 * Represents a train departure with specific details such as departure time, line, destination,
 * train number, track number, and delay.
 */
public class TrainDeparture {

  private final LocalTime departureTime;
  private final String line;
  private final String destination;
  private final int trainNumber;
  private int track;
  private LocalTime delay;
  private static final Pattern timePattern = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");



  /**
   * Constructs a TrainDeparture object with the specified departure time, line, destination,
   * train number, track number, and delay.
   *
   * @param departureTime The departure time of the train in the format HH:mm.
   * @param line The line on which the train is operating.
   * @param destination The destination of the train.
   * @param trainNumber The unique identifier of the train.
   * @param track The track number from which the train departs (-1 if unknown).
   * @param delay The delay time in minutes (in the format HH:mm) if the train is delayed.
   * @throws DateTimeException If the departure time or delay format is invalid.
   * @throws NullPointerException If the destination is null.
   */
  TrainDeparture(LocalTime departureTime, String line, String destination,
          int trainNumber, int track, LocalTime delay) {
    if (!timePattern.matcher(departureTime.toString()).matches()) {
      throw new DateTimeException("Departure time must be in the format HH:mm");
    } else {
      this.departureTime = departureTime;
    }
    if (!timePattern.matcher(delay.toString()).matches()) {
      throw new DateTimeException("Delay must be in the format HH:mm");
    } else {
      this.delay = delay;
    }
    if (destination == null) {
      throw new NullPointerException("Destination cannot be null");
    } else {
      this.destination = destination;
    }
    if (line.isEmpty()) {
      throw new NullPointerException("Line cannot be empty");
    } else {
      this.line = line;
    }
    if (track <= 0) {
      this.track = -1;
    } else {
      this.track = track;
    }
    this.trainNumber = trainNumber;
  }


  //NEED?
  /**
   * Constructs a TrainDeparture object with the specified departure time, line, destination,
   * train number, and delay. The track number is set to -1 as it is unknown.
   *
   * @param departureTime The departure time of the train in the format HH:mm.
   * @param line The line on which the train is operating.
   * @param destination The destination of the train.
   * @param trainNumber The unique identifier of the train.
   * @param delay The delay time in minutes (in the format HH:mm) if the train is delayed.
   * @throws DateTimeException If the departure time or delay format is invalid.
   * @throws NullPointerException If the destination is null.
   */
  TrainDeparture(LocalTime departureTime, String line, String destination,
                 int trainNumber, LocalTime delay) {
    this(departureTime, line, destination, trainNumber, -1, delay);
  }

  /**
   * Gets the departure time of the train.
   *
   * @return The departure time of the train.
   */
  public LocalTime getDepartureTime() {
    return departureTime;
  }


  /**
   * Gets the line on which the train is operating.
   *
   * @return The line of the train.
   */
  public String getLine() {
    return line;
  }

  /**
   * Gets the destination of the train.
   *
   * @return The destination of the train.
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Gets the unique identifier of the train.
   *
   * @return The train number.
   */
  public int getTrainNumber() {
    return trainNumber;
  }

  /**
   * Sets the track number for the train. Throws a IllegalArgumentException if the track number
   * is less than or equal to 0, because this will only be used on methods where the track number
   * was not set when the TrainDeparture object was created.
   *
   * @param track The track number from which the train departs.
   * @throws IllegalArgumentException If the track number is less than or equal to 0.
   */
  public void setTrack(int track) {
    if (track <= 0) {
      throw new IllegalArgumentException("Track cannot be less than 0");
    } else {
      this.track = track;
    }
  }

  /**
   * Sets the delay time for the train. Throws a DateTimeException if the delay time format is
   * invalid. Cheks whether the time format is valid by using a regular expression. ("timePattern").
   *
   * @param delay The delay time in HH:mm.
   * @throws DateTimeException If the delay time format is invalid.
   */
  public void setDelay(LocalTime delay) {
    if (delay == LocalTime.of(0, 0)) {
      throw new DateTimeException("Cannot update delay to 00:00");
    } else {
      this.delay = delay;
    }
  }

  /**
   * Gets the track number from which the train departs.
   *
   * @return The track number.
   */
  public int getTrack() {
    return track;
  }

  /**
   * Gets the delay time of the train in minutes.
   *
   * @return The delay time of the train.
   */
  public LocalTime getDelay() {
    return delay;
  }

  /**
   * Gets the departure time of the train considering the delay.
   *
   * @return The departure time with delay taken into account.
   */
  public LocalTime getDepartureTimeWithDelay() {
    return departureTime.plusHours(delay.getHour()).plusMinutes(delay.getMinute());
  }

  /**
   * Returns a formatted string representation of the TrainDeparture object.
   * The string contains departure time, destination, track number, line, train number,
   * and delay, separated by vertical bars ('|') and padded for alignment.
   *
   * @return A formatted string representing the TrainDeparture object's details.
   */
  public String toString() {
    StringBuilder info = new StringBuilder();
    int destinationLength = destination.length();
    if (delay != LocalTime.of(0, 0)) {
      info.append("| ").append(departureTime).append("(+")
        .append(delay).append(")").append(String.format("%" + 2 + "s", " | "));
    } else {
      info.append("| ").append(String.format("%" + 4 + "s", " "))
        .append(departureTime).append(String.format("%" + 7 + "s", " | "));
    }
    info.append(destination).append(String.format("%" + (18 - destinationLength) + "s", " | "));
    if (track == -1) {
      info.append("No Track Yet").append(String.format("%" + 2 + "s", " | "));
    } else {
      info.append(String.format("%" + 6 + "s", " "))
        .append(track).append(String.format("%" + 8 + "s", " | "));
    }
    info.append(String.format("%" + 3 + "s", " "))
      .append(line).append(String.format("%" + 6 + "s", " | "));
    info.append(String.format("%" + 8 + "s", " "))
      .append(trainNumber).append(String.format("%" + 11 + "s", " | "));
    info.append(delay).append(String.format("%" + 7 + "s", "|")).append("\n");
    return info.toString();
  }
}
