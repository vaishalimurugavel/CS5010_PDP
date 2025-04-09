package calendar.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import calendar.model.Event;
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
  public void addEvent(Map<String, String> properties) {
    Map<String, Object> details = new HashMap<String, Object>();
    details.put(EventKeys.SUBJECT, properties.get(EventKeys.SUBJECT));
    details.put(EventKeys.LOCATION, properties.getOrDefault(EventKeys.LOCATION, null));
    details.put(EventKeys.DESCRIPTION, properties.getOrDefault(EventKeys.DESCRIPTION, null));
    details.put(EventKeys.START_DATETIME, LocalDateTime.parse( properties.getOrDefault(EventKeys.START_DATETIME, null)));
    details.put(EventKeys.END_DATETIME, LocalDateTime.parse(properties.getOrDefault(EventKeys.END_DATETIME, null)));
    if(properties.containsKey(EventKeys.EVENT_TYPE)){
      String type = properties.getOrDefault(EventKeys.EVENT_TYPE, null);
      switch (type) {
        case "Single AllDay":
          details.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
          break;
        case "Recurring AllDay":
          details.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY_RECURRING);
          if(properties.containsKey(EventKeys.OCCURRENCES)){
            details.put(EventKeys.OCCURRENCES , properties.get(EventKeys.OCCURRENCES));
          }
          break;
        default:
          details.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);
      }
    }
    CalendarFactory.getModel().addEvent(details);
  }

  @Override
  public void editEvent(Map<String, String> properties) {


  }

  @Override
  public void removeEvent(String name) {


  }

  @Override
  public void exportCalendar(String name) throws IOException {
    CalendarFactory.setModel(CalendarFactory.getGroup().getCalendar(name)
            .getCalendarEventEvents());
    try {
      CalendarFactory.getExport().displayOutput(CalendarFactory.getModel().getEventsForDisplay());
    } catch (IOException e) {
      throw new IOException(e);
    }
  }
}
