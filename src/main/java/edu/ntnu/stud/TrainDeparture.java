package edu.ntnu.stud;

import java.util.ArrayList;
import java.util.regex.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class TrainDeparture {

  private static boolean staticPartPrinted = false;
  private LocalTime departureTime;
  private String line;
  private String destination;
  private  int trainNumber;
  private int track;
  private LocalTime delay;
  private static final Pattern linePattern = Pattern.compile("[A-Z]\\d");
  private static final Pattern timePattern = Pattern.compile("\\d{2}:\\d{2}");


  DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
  TrainDepartureUserInterface userInterface = new TrainDepartureUserInterface();

  TrainDeparture(LocalTime departureTime, String line, String destination, int trainNumber, int track, LocalTime delay) {
    if (trainNumber < 1) {
      throw new IllegalArgumentException("Train number must be greater than 1");
    }
    if (track < 1) {
      throw new IllegalArgumentException("Track must be greater than 1");
    }
    if (departureTime == null) {
      throw new IllegalArgumentException("Departure time cannot be null");
    }
    if (!linePattern.matcher(line).matches()) {
      throw new IllegalArgumentException("Line must be a capital letter followed by a number");
    }
    if (!timePattern.matcher(departureTime.toString()).matches()) {
      throw new IllegalArgumentException("Departure time must be in the format HH:mm");
    }
    if (!timePattern.matcher(delay.toString()).matches()) {
      throw new IllegalArgumentException("Delay must be in the format HH:mm");
    }
    this.departureTime = departureTime;
    this.line = line;
    this.destination = destination;
    this.trainNumber = trainNumber;
    this.track = track;
    this.delay = delay;
  }

  public LocalTime getDepartureTime() {
    return departureTime;
  }

  private String getLine() {
    return line;
  }

  public String getDestination() {
    return destination;
  }

  public int getTrainNumber() {
    return trainNumber;
  }

  public void setTrack(int track) {
    this.track = track;
  }

  public void setDelay(LocalTime delay) {
    this.delay = delay;
  }

  public int getTrack() {
    return track;
  }

  public LocalTime getDelay() {
    return delay;
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
    info.append("+--------+--------------+--------+--------+---------------+-----------+");
    return info.toString();
  }

}
