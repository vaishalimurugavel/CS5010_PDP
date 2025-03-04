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

  public Event(String subject, String location, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, int eventType) {
    this.subject = subject;
    this.location = location;
    this.description = description;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.eventType = eventType;

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
  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }
  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  public static class EventKeys{
      public static final String SUBJECT = "subject";
      public static final String LOCATION = "location";
      public static final String DESCRIPTION = "description";
      public static final String EVENT_TYPE = "eventType";
      public static final String START_DATETIME = "startDateTime";
      public static final String END_DATETIME = "endDateTime";
      public static final String AUTO_DECLINE = "autoDecline";
  }
}


