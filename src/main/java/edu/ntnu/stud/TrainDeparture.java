package edu.ntnu.stud;

import java.time.LocalTime;
public class TrainDeparture {
  private LocalTime departureTime;
  private String line;
  private String destination;
  private int trainNumber;
  private int track;
  private LocalTime delay;

  TrainDeparture(LocalTime departureTime, String line, String destination, int trainNumber, int track, LocalTime delay){
    this.departureTime = departureTime;
    this.line = line;
    this.destination = destination;
    this.trainNumber = trainNumber;
    this.track = track;
    this.delay = LocalTime.of(0,0);
  }

  private LocalTime getDepartureTime() {
    return departureTime;
  }

  private String getLine() {
    return line;
  }

  private String getDestination() {
    return destination;
  }

  public int getTrainNumber() {
    return trainNumber;
  }

  public void setTrack(int track) {
    this.track = track;
  }

  public void setDelay(LocalTime delay){
    this.delay = delay;
  }

  public String toString(){
    String info = "";
    info += "Departure time: " + departureTime + "\n";
    info += "Line: " + line + "\n";
    info += "Destination: " + destination + "\n";
    info += "Train number: " + trainNumber + "\n";
    info += "Track: " + track + "\n";
    info += "Delay: " + delay + "\n";
    return info;
  }

}
