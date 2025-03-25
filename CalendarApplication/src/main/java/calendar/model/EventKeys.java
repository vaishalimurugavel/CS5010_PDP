package calendar.model;

/**
 * <p>
 * Public keys for key names.
 * This class provides constant key names for various event attributes
 * that can be used in the application. These key names are used when
 * serializing, deserializing, or handling event data.
 * </p>
 */
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
   * <p>
   * Enum to identify the type of event.
   * This enum represents the different types of events:
   * - SINGLE: A one-time event.
   * - RECURRING: An event that occurs repeatedly.
   * - ALL_DAY: A full-day event with no specific start/end times.
   * - ALL_DAY_RECURRING: A recurring full-day event.
   * </p>
   */
  public enum EventType {
    SINGLE, RECURRING, ALL_DAY, ALL_DAY_RECURRING
  }
}
