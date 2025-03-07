package calendar.model;

import java.time.LocalDate;
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
public class RecurringEvent extends Event {

  private String weekdays;
  private int occurrences;
  private LocalDateTime repeatDateTime;
  private LocalDate repeatDate;

  public RecurringEvent(String subject, String location, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, int eventType, boolean allDay, String  weekdays, int occurrences, LocalDateTime repeatDateTime, LocalDate repeatDate, LocalDate allDate, LocalDateTime allDateTime) {
    super(subject, location, description, startDateTime, endDateTime, eventType, allDay, allDate, allDateTime);
    this.weekdays = weekdays;
    this.occurrences = occurrences;
    this.repeatDateTime = repeatDateTime;
    this.repeatDate = repeatDate;
  }
}
