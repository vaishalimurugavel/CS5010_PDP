package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * Bean class for Recurring event. This extends Event class.
 * Uses the RecurringBuilder pattern for flexible and readable object creation.
 * </p>
 */
public class RecurringEvent extends Event {

  private String weekdays;
  private int occurrences;
  private LocalDateTime repeatDateTime;
  private LocalDate repeatDate;

  private RecurringEvent(RecurringBuilder builder) {
    super(builder);
    this.weekdays = builder.weekdays;
    this.occurrences = builder.occurrences;
    this.repeatDateTime = builder.repeatDateTime;
    this.repeatDate = builder.repeatDate;
  }

  public static class RecurringBuilder extends Event.EventBuilder {
    private String weekdays = null;
    private int occurrences = 0;
    private LocalDateTime repeatDateTime = null;
    private LocalDate repeatDate = null;

    public RecurringBuilder(String subject) {
      super(subject);
    }

    public RecurringBuilder weekdays(String weekdays) {
      this.weekdays = weekdays;
      return this;
    }

    public RecurringBuilder occurrences(int occurrences) {
      this.occurrences = occurrences;
      return this;
    }

    public RecurringBuilder repeatDateTime(LocalDateTime repeatDateTime) {
      this.repeatDateTime = repeatDateTime;
      return this;
    }

    public RecurringBuilder repeatDate(LocalDate repeatDate) {
      this.repeatDate = repeatDate;
      return this;
    }

    @Override
    public RecurringEvent build() {
      return new RecurringEvent(this);
    }
  }

  // Getters
  public String getWeekdays() {
    return weekdays;
  }

  public int getOccurrences() {
    return occurrences;
  }

  public LocalDateTime getRepeatDateTime() {
    return repeatDateTime;
  }

  public LocalDate getRepeatDate() {
    return repeatDate;
  }
}
