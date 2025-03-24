package calendar.model;

/**
 * Public keys for key names.
 **/
public class EventKeys {

  public static final String SUBJECT = "subject";
  public static final String LOCATION = "location";
  public static final String DESCRIPTION = "description";
  public static final String PRIVATE = "private";
  public static final String EVENT_TYPE = "eventType";
  public static final String START_DATETIME = "startDateTime";
  public static final String END_DATETIME = "endDateTime";
  public static final String AUTO_DECLINE = "autoDecline";
  public static final String PROPERTY = "property";
  public static final String NEW_VALUE = "newValue";
  public static final String WEEKDAYS = "weekdays";
  public static final String OCCURRENCES = "occurrences";
  public static final String REPEAT_DATETIME = "repeatDateTime";
  public static final String REPEAT_DATE = "repeatDate";
  public static final String ALLDAY_DATE = "allDayDate";

  /**
   * Enum to identify the type of event.
   */
  public enum EventType {
    SINGLE, RECURRING, ALL_DAY, ALL_DAY_RECURRING
  }
}
