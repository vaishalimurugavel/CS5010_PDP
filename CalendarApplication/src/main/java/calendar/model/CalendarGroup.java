package calendar.model;

import java.util.Map;

/**
 * <p>
 * Interface for managing a group of calendars. This interface defines methods for adding, updating,
 * and retrieving calendar events, as well as checking for duplicate calendars.
 * </p>
 **/
public interface CalendarGroup {

  /**
   * Checks if a calendar with the specified name already exists in the group.
   *
   * @param name The name of the calendar to check for duplicates.
   * @return true if a calendar with the given name already exists, false otherwise.
   */
  boolean checkForDuplicates(String name);

  /**
   * Adds a new calendar to the group with the specified name and timezone.
   *
   * @param name     The name of the calendar to be added.
   * @param timezone The timezone of the calendar to be added.
   */
  void addCalendar(String name, String timezone);

  /**
   * Updates an existing calendar with the provided properties.
   *
   * @param prop A map containing the properties to update (e.g., name, timezone).
   */
  void updateCalendar(Map<String, Object> prop);

  /**
   * Retrieves the calendar event manager for the specified calendar by its name.
   *
   * @param name The name of the calendar.
   * @return The `CalendarEvent` associated with the calendar.
   */
  CalendarEvent getCalendarEvent(String name);

  /**
   * Retrieves the calendar associated with the specified name.
   *
   * @param name The name of the calendar to retrieve.
   * @return The `Calendars` object representing the calendar.
   */
  Calendars getCalendar(String name);

  /**
   * Retrieves the calendar associated with the specified name.
   *
   * @param name The name of the calendar to retrieve.
   * @return The `Calendars` object representing the calendar.
   */
  Calendars setCurrentCalendar(String name);

  /**
   * Retrieves the calendar currently in use.
   *
   * @return The `Calendars` object representing the calendar.
   */
  Calendars getCurrentCalendar();

  String[] getCalendarNames();

}