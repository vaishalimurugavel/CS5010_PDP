package calendar;

/**
 * Event bean class
 */
public class Event {

  private String subject;
  private String location;
  private String description;
  private enum eventType{
    SINGLE, RECURRING;
  }
  private DateTime startDateTime;
  private DateTime endDateTime;

}
