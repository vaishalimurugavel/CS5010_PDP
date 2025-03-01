package calendar.model;

/**
 * Event bean class
 */
public class Event {

  private String subject;
  private String location;
  private String description;
  private int eventType;
  private DateTime startDateTime;
  private DateTime endDateTime;

  public Event(String subject, String location, String description, DateTime startDateTime, DateTime endDateTime, int eventType) {
    this.subject = subject;
    this.location = location;
    this.description = description;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.eventType = eventType;

  }

}
