package calendar.controller;

import java.io.IOException;
import java.util.Map;

/**
 * CalendarFeatures is a Feature class is a wrapper class to implement mapping between views and model.
 **/
public interface CalendarFeatures {

  /**
   * Add calendar to Calendar application.
   * @param name name of the Calendar.
   * @param timeZone Timezone of the calendar.
   */
  void addCalendar(String name, String timeZone);

  /**
   * Edit calendar.
   * @param properties Properties to edit.
   */
  void editCalendar(Map<String, String> properties);

  /**
   * Add Calendar events to the selected Calendar.
   * @param properties Properties of the event.
   */
  void addEvent(Map<String, String> properties);

  /**
   * Edit calendar events of the selected calendar.
   * @param properties Properties of the event.
   */
  void editEvent(Map<String, String> properties);

  /**
   * Export the Calendar events.
   * @param name File path and file name to export.
   * @throws IOException Throws IOException.
   */
  void exportCalendar(String name) throws IOException;

  /**
   * Select a calendar to perform operation.
   * @param name Name of the Calendar.
   */
  void setCalendar(String name);

  /**
   * Import the Calendar events to calendar.
   * @param properties Calendar events.
   */
  void importCalendar(Map<String, Object> properties);
}
