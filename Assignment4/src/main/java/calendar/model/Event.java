package calendar.model;

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

  public Event(String subject, String location, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, int eventType, boolean isAllDay) {
    this.subject = subject;
    this.location = location;
    this.description = description;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.eventType = eventType;
    this.isAllDay = isAllDay;

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
  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }
  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }
  public int getEventType() {
    return eventType;
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

  public static class EventKeys{
      public static final String SUBJECT = "subject";
      public static final String LOCATION = "location";
      public static final String DESCRIPTION = "description";
      public static final String EVENT_TYPE = "eventType";
      public static final String START_DATETIME = "startDateTime";
      public static final String END_DATETIME = "endDateTime";
      public static final String AUTO_DECLINE = "autoDecline";
      public static final String PROPERTY = "property";
      public static final String NEW_VALUE = "newValue";
      public static final String ALL_DAY = "allDayEvent";
  }
}


