package calendar.controller;

import java.io.IOException;
import java.util.Map;

/**
 * Created at 03-04-2025
 * Author Vaishali
 **/
public interface CalendarFeatures {

  void addCalendar(String name, String timeZone);
  void editCalendar(Map<String, String> properties);
  void addEvent(Map<String, String> properties);
  void editEvent(Map<String, String> properties);
  void removeEvent(String name);
  void exportCalendar(String name) throws IOException;
}
