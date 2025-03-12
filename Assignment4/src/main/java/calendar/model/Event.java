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
  private String weekdays = null;
  private int occurrences = 0;
  private LocalDateTime repeatDateTime = null;
  private LocalDate repeatDate = null;


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
    this.weekdays = builder.weekdays;
    this.occurrences = builder.occurrences;
    this.repeatDateTime = builder.repeatDateTime;
    this.repeatDate = builder.repeatDate;
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
    private String weekdays = null;
    private int occurrences = 0;
    private LocalDateTime repeatDateTime = null;
    private LocalDate repeatDate = null;

    /**
     * Constructor of EventBuilder.
     *
     * @param subject Name of the event.
     */
    public EventBuilder(String subject) {
      this.subject = subject;
    }

    /**
     * Initialize startDateTime of the event.
     *
     * @param startDateTime DateTime
     * @return EventBuilder instance returned.
     */
    public EventBuilder startDateTime(LocalDateTime startDateTime) {
      this.startDateTime = startDateTime;
      return this;
    }

    /**
     * Initialize endDateTime of the event.
     *
     * @param endDateTime DateTime
     * @return EventBuilder instance returned.
     */
    public EventBuilder endDateTime(LocalDateTime endDateTime) {
      this.endDateTime = endDateTime;
      return this;
    }

    /**
     * Initialize location of the event.
     *
     * @param location String
     * @return EventBuilder instance returned.
     */
    public EventBuilder location(String location) {
      this.location = location;
      return this;
    }

    /**
     * Initialize description of the event.
     *
     * @param description String
     * @return EventBuilder instance returned.
     */
    public EventBuilder description(String description) {
      this.description = description;
      return this;
    }

    /**
     * Initialize if the event is private.
     *
     * @param privateEvent int
     * @return EventBuilder instance returned.
     */
    public EventBuilder privateEvent(int privateEvent) {
      this.privateEvent = privateEvent;
      return this;
    }

    /**
     * Initialize if the event is an all day event.
     *
     * @param isAllDay boolean
     * @return EventBuilder instance returned.
     */
    public EventBuilder allDay(boolean isAllDay) {
      this.isAllDay = isAllDay;
      return this;
    }

    /**
     * Initialize allDAte of the event.
     *
     * @param allDate Date
     * @return EventBuilder instance returned.
     */
    public EventBuilder allDate(LocalDate allDate) {
      this.allDate = allDate;
      return this;
    }

    /**
     * Initialize allDateTime of the event.
     *
     * @param allDateTime DateTime
     * @return EventBuilder instance returned.
     */
    public EventBuilder allDateTime(LocalDateTime allDateTime) {
      this.allDateTime = allDateTime;
      return this;
    }

    /**
     * Builds the Event class
     *
     * @return Event instance returned.
     */
    public Event build() {
      return new Event(this);
    }

    /**
     * Initialize subject of the event.
     *
     * @param subject String
     * @return EventBuilder instance returned.
     */
    public EventBuilder subject(String subject) {
      this.subject = subject;
      return this;
    }

    /**
     * Initialize weekdays of the event.
     *
     * @param weekdays String
     * @return EventBuilder instance returned.
     */
    public EventBuilder weekdays(String weekdays) {
      this.weekdays = weekdays;
      return this;
    }

    /**
     * Initialize occurances of the event.
     *
     * @param occurrences int
     * @return EventBuilder instance returned.
     */
    public EventBuilder occurrences(int occurrences) {
      this.occurrences = occurrences;
      return this;
    }

    /**
     * Initialize repeatDateTime of the event.
     *
     * @param repeatDateTime DateTime
     * @return EventBuilder instance returned.
     */
    public EventBuilder repeatDateTime(LocalDateTime repeatDateTime) {
      this.repeatDateTime = repeatDateTime;
      return this;
    }

    /**
     * Initialize repeatDate of the event.
     *
     * @param repeatDate Date
     * @return EventBuilder instance returned.
     */
    public EventBuilder repeatDate(LocalDate repeatDate) {
      this.repeatDate = repeatDate;
      return this;
    }
  }

  /**
   * Returns Name of the event
   *
   * @return String
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Returns location of the event
   *
   * @return String
   */
  public String getLocation() {
    return location;
  }

  /**
   * Returns Description of the event
   *
   * @return String
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns if it is an allDay.
   *
   * @return boolean
   */
  public boolean isAllDay() {
    return isAllDay;
  }

  /**
   * Returns Date of when allDay event ends.
   *
   * @return LocalDate
   */
  public LocalDate getAllDayEnd() {
    return allDate;
  }

  /**
   * Returns the start date of the event.
   *
   * @return LocalDateTime
   */
  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  /**
   * Returns the end date of the event.
   *
   * @return LocalDateTime
   */
  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  /**
   * Returns the type of event.
   *
   * @return int
   */
  public int getEventType() {
    return privateEvent;
  }

  /**
   * Returns the allDate  of the event.
   *
   * @return LocalDate
   */
  public LocalDate getAllDate() {
    return allDate;
  }

  /**
   * Returns the alldate time of the event.
   *
   * @return LocalDateTime
   */
  public LocalDateTime getAllDateTime() {
    return allDateTime;
  }

  /**
   * Returns the repeat dates of the event.
   *
   * @return LocalDateTime
   */
  public LocalDateTime getRepeatDateTime() {
    return repeatDateTime;
  }

  /**
   * Returns the repeat date of the event.
   *
   * @return LocalDate
   */
  public LocalDate getRepeatDate() {
    return repeatDate;
  }

}
