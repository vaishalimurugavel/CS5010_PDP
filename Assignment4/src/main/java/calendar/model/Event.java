package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Event bean class using EventBuilder pattern.
 */
public class Event {

  private String subject = null;
  private String location = null;
  private String description = null;
  private int privateEvent = 0;
  private LocalDateTime startDateTime = null;
  private LocalDateTime endDateTime = null;
  private boolean isAllDay = false;
  private LocalDate allDate = null;
  private LocalDateTime allDateTime = null;

  Event(EventBuilder builder) {
    this.subject = builder.subject;
    this.location = builder.location;
    this.description = builder.description;
    this.privateEvent = builder.privateEvent;
    this.startDateTime = builder.startDateTime;
    this.endDateTime = builder.endDateTime;
    this.isAllDay = builder.isAllDay;
    this.allDate = builder.allDate;
    this.allDateTime = builder.allDateTime;
  }

  public static class EventBuilder {
    private String subject;
    private String location;
    private String description;
    private int privateEvent;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean isAllDay;
    private LocalDate allDate;
    private LocalDateTime allDateTime;

    public EventBuilder(String subject) {
      this.subject = subject;
    }

    public EventBuilder startDateTime(LocalDateTime startDateTime) {
      this.startDateTime = startDateTime;
      return this;
    }
    
    public EventBuilder endDateTime(LocalDateTime endDateTime) {
      this.endDateTime = endDateTime;
      return this;
    }
    
    public EventBuilder location(String location) {
      this.location = location;
      return this;
    }

    public EventBuilder description(String description) {
      this.description = description;
      return this;
    }

    public EventBuilder privateEvent(int privateEvent) {
      this.privateEvent = privateEvent;
      return this;
    }

    public EventBuilder allDay(boolean isAllDay) {
      this.isAllDay = isAllDay;
      return this;
    }

    public EventBuilder allDate(LocalDate allDate) {
      this.allDate = allDate;
      return this;
    }

    public EventBuilder allDateTime(LocalDateTime allDateTime) {
      this.allDateTime = allDateTime;
      return this;
    }

    public Event build() {
      return new Event(this);
    }

    public EventBuilder subject(String subject) {
      this.subject = subject;
      return this;
    }
  }

  // Getters
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

  public LocalDate getAllDayEnd() {
    return allDate;
  }

  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  public int getEventType() {
    return privateEvent;
  }

  public LocalDate getAllDate() {
    return allDate;
  }

  public LocalDateTime getAllDateTime() {
    return allDateTime;
  }
}
