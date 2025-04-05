package calendar.controller;

import java.util.Map;

/**
 * Created at 03-04-2025
 * Author Vaishali
 **/
public interface CalendarFeatures {

  void addCalendar(String name, String timeZone);
  void editCalendar(Map<String, String> properties);
  void addEvent(Map<String, String> details);
  void editEvent(Map<String, String> details);
  void removeEvent(String name);
}
