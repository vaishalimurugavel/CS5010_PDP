package calendar.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import calendar.model.EventKeys;
import calendar.view.CalendarExport;

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
    Map<String, Object> details = new HashMap<String, Object>();
    if(properties.containsKey(EventKeys.SUBJECT)){
      details.put(EventKeys.SUBJECT, properties.get(EventKeys.SUBJECT));
    }
    if(properties.containsKey(EventKeys.LOCATION)){
      details.put(EventKeys.LOCATION, properties.get(EventKeys.LOCATION));
    }
    if(properties.containsKey(EventKeys.DESCRIPTION)){
      details.put(EventKeys.DESCRIPTION, properties.get(EventKeys.DESCRIPTION));
    }
    if(properties.containsKey(EventKeys.START_DATETIME)){
      details.put(EventKeys.START_DATETIME, properties.get(EventKeys.START_DATETIME));
    }
    if(properties.containsKey(EventKeys.END_DATETIME)){
      details.put(EventKeys.END_DATETIME, properties.get(EventKeys.END_DATETIME));
    }
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
    if(properties.containsKey(EventKeys.PRIVATE)){
      details.put(EventKeys.PRIVATE, properties.get(EventKeys.PRIVATE) != null);
    }
    CalendarFactory.getModel().updateEvent(details);
  }


  @Override
  public void exportCalendar(String name) throws IOException {
    CalendarFactory.setModel(CalendarFactory.getGroup().getCalendar(name)
            .getCalendarEventEvents());
    try {
      FileOutputStream file = new FileOutputStream(name);
      CalendarFactory.setExport(new CalendarExport(file));
      CalendarFactory.getExport().displayOutput(CalendarFactory.getModel().getEventsForDisplay());
    } catch (IOException e) {
      throw new IOException(e);
    }
  }
}
