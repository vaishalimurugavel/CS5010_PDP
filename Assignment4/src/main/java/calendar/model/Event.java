package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Event bean class
 */
public class Event {

  private String subject;
  private String location;
  private String description;
  private int eventType;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private boolean isAllDay;
  private LocalDate allDate;
  private LocalDateTime allDateTime;

  public Event(String subject, String location, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, int eventType, boolean isAllDay, LocalDate allDate, LocalDateTime allDateTime) {
    this.subject = subject;
    this.location = location;
    this.description = description;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.eventType = eventType;
    this.isAllDay = isAllDay;
    this.allDate = allDate;
    this.allDateTime = allDateTime;

  }
  public String getSubject() {
    return subject;
  }
  public String getLocation() {
    return location;
  }
  public String getDescription() {
    return description;
  }
  public boolean isAllDay() {
    return isAllDay;
  }
  public LocalDate getAllDayEnd() {
    return allDate;
  }
  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }
  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }
  public int getEventType() {
    return eventType;
  }
  public LocalDate getAllDate() {
    return allDate;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }
  public void setLocation(String location) {
    this.location = location;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public void setStartDateTime(LocalDateTime startDateTime) {
    this.startDateTime = startDateTime;
  }
  public void setEndDateTime(LocalDateTime endDateTime) {
    this.endDateTime = endDateTime;
  }
  public void setEventType(int eventType) {
    this.eventType = eventType;
  }
  public void setAllDay(boolean isAllDay) {
    this.isAllDay = isAllDay;
  }
  public void setAllDayEnd(LocalDate allDate) {
    this.allDate = allDate;
  }
  public void setAllDateTime(LocalDateTime allDateTime) {
    this.allDateTime = allDateTime;
  }

}


