package edu.ntnu.stud;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * A class for handling time.
 */
public class Time {

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  public LocalTime currentTime;

  public Time(String currentTime) {
    this.currentTime = LocalTime.parse(currentTime);
  }


  public LocalTime getCurrentTime() {
    return currentTime;
  }

  public void setCurrentTime(LocalTime currentTime) {
    this.currentTime = currentTime;
  }

}
