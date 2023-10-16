package edu.ntnu.stud;
import java.time.LocalTime;
public class SystemTime {
  public LocalTime systemTime;

  public SystemTime(LocalTime systemTime) {
    this.systemTime = systemTime;
  }

  public LocalTime getSystemTime() {
    return systemTime;
  }

  public void setSystemTime(LocalTime systemTime) {
    this.systemTime = systemTime;
  }

}
