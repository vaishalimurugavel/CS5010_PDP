package calendar.model;

import java.time.LocalDateTime;

/**
 * Created at 03-03-2025
 * Author Vaishali
 **/

/**
 * <p>
 * Bean class for Recurring event. This extends Event class
 * </p>
 */
class RecurringEvent extends Event {

  private int[] weekdays;
  private int occurrences;

  public RecurringEvent(String subject, String location, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, int eventType, boolean allDay, int[] weekdays, int occurrences) {
    super(subject, location, description, startDateTime, endDateTime, eventType, allDay);
    this.weekdays = weekdays;
    this.occurrences = occurrences;
  }

  public static class RecurringEventKeys {
    public static final String WEEKDAYS = "weekdays";
    public static final String OCCURRENCES = "occurrences";
  }
}
