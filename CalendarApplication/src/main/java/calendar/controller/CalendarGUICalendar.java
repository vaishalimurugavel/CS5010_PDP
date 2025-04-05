package calendar.controller;

import java.util.HashMap;
import java.util.Map;

import calendar.model.EventKeys;

/**
 * Created at 04-04-2025
 * Author Vaishali
 **/
public class CalendarGUICalendar implements CalendarFeatures {
  @Override
  public void addCalendar(String name, String timeZone) {
    CalendarFactory.getGroup().addCalendar(name, timeZone);
  }

  @Override
  public void editCalendar(Map<String ,String> properties) {
    Map<String, Object> details = new HashMap<String, Object>();
    details.put(EventKeys.CALENDAR_NAME, properties.get(EventKeys.CALENDAR_NAME));
    details.put(EventKeys.PROPERTY, properties.get(EventKeys.PROPERTY));
    details.put(EventKeys.NEW_VALUE, properties.get(EventKeys.NEW_VALUE));
    CalendarFactory.getGroup().updateCalendar(details);
  }

  @Override
  public void addEvent(Map<String, String> details) {


  }

  @Override
  public void editEvent(Map<String, String> details) {


  }

  @Override
  public void removeEvent(String name) {


  }
}
