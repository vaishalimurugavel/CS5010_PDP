package calendar.model;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Represents a calendar group that holds information about its title,
 * timezone, and associated events.
 * </p>
 **/
public class Calendars {

  private String title;
  private ZoneId zoneId;
  private String zoneName;
  private CalendarEvent calendarEvents;

  /**
   * Constructor to initialize a calendar group with its title, timezone, and timezone name.
   *
   * @param title    The title or name of the calendar group.
   * @param zoneId   The timezone associated with the calendar group.
   * @param zoneName The name of the timezone.
   */
  public Calendars(String title, ZoneId zoneId, String zoneName) {
    this.title = title;
    this.zoneId = zoneId;
    this.zoneName = zoneName;
    // Initialize the calendar events manager for this calendar group.
    calendarEvents = new CalenderEventManager();
  }

  /**
   * Returns the title of the calendar group.
   *
   * @return The title of the calendar group.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the calendar group.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the `CalendarEvent` associated with this calendar group.
   * This allows access to the calendar events associated with the group.
   *
   * @return The `CalendarEvent` object for this calendar group.
   */
  public CalendarEvent getCalendarEventEvents() {
    return calendarEvents;
  }

  /**
   * Returns the `ZoneId` associated with the calendar group.
   *
   * @return The `ZoneId` representing the timezone of the calendar group.
   */
  public ZoneId getZoneId() {
    return zoneId;
  }

  /**
   * Returns the name of the timezone associated with the calendar group.
   *
   * @return The name of the timezone (e.g., "UTC", "America/New_York").
   */
  public String getZoneName() {
    return zoneName;
  }

  /**
   * Sets the name of the timezone associated with the calendar group.
   */
  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
    this.zoneId = ZoneId.of(zoneName);
  }

  public List<Map<String, Object>> getEventsForDisplay() {
    return calendarEvents.getEventsForDisplay();
  }

  public Map<String, Object>[] getEvents() {
    return new Map[0];
  }

}