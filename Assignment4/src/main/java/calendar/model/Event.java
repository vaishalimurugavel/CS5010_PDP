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

}
