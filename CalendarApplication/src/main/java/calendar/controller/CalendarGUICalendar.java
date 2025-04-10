package calendar.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import calendar.model.CalendarEvent;
import calendar.model.EventKeys;
import calendar.view.CalendarExport;

/**
 * CalendarGUICalendar extends CalendarFeatures that maps the GUI View to Model.
 **/
public class CalendarGUICalendar implements CalendarFeatures {
  @Override
  public void addCalendar(String name, String timeZone) {
    CalendarFactory.getGroup().addCalendar(name, timeZone);
  }

  @Override
  public void editCalendar(Map<String, String> properties) {
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
    if (properties.containsKey(EventKeys.EVENT_TYPE)) {
      String type = properties.getOrDefault(EventKeys.EVENT_TYPE, null);
      if (properties.containsKey(EventKeys.START_DATETIME)) {
        details.put(EventKeys.START_DATETIME, LocalDateTime.parse(properties.getOrDefault(
                EventKeys.START_DATETIME, null)));
      }
      switch (type) {
        case "Single AllDay":
          details.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
          break;
        case "Recurring AllDay":
          details.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY_RECURRING);
          details.put(EventKeys.REPEAT_DATETIME, LocalDateTime.parse(properties.getOrDefault(
                  EventKeys.REPEAT_DATETIME, null)));
          if (properties.containsKey(EventKeys.WEEKDAYS)) {
            details.put(EventKeys.WEEKDAYS, properties.get(EventKeys.OCCURRENCES));
          }
          break;
        default:
          details.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);
          if (properties.containsKey(EventKeys.END_DATETIME)) {
            details.put(EventKeys.END_DATETIME, LocalDateTime.parse(properties.getOrDefault(
                    EventKeys.END_DATETIME, null)));
          }
      }
    }
    try {
      CalendarFactory.getModel().addEvent(details);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void editEvent(Map<String, String> properties) {
    Map<String, Object> details = new HashMap<String, Object>();
    if (properties.containsKey(EventKeys.SUBJECT)) {
      details.put(EventKeys.SUBJECT, properties.get(EventKeys.SUBJECT));
    }
    if (properties.containsKey(EventKeys.START_DATETIME)) {
      details.put(EventKeys.START_DATETIME, properties.get(EventKeys.START_DATETIME));
    }
    if (properties.containsKey(EventKeys.END_DATETIME)) {
      details.put(EventKeys.END_DATETIME, properties.get(EventKeys.END_DATETIME));
    }
    if (properties.containsKey(EventKeys.LOCATION)) {
      details.put(EventKeys.PROPERTY, EventKeys.DESCRIPTION);
      details.put(EventKeys.NEW_VALUE, properties.get(EventKeys.LOCATION));
      CalendarFactory.getModel().updateEvent(details);
    }
    if (properties.containsKey(EventKeys.DESCRIPTION)) {
      details.put(EventKeys.PROPERTY, EventKeys.DESCRIPTION);
      details.put(EventKeys.NEW_VALUE, properties.get(EventKeys.DESCRIPTION));
      CalendarFactory.getModel().updateEvent(details);
    }
    if (properties.containsKey(EventKeys.NEW_VALUE)) {
      details.put(EventKeys.PROPERTY, EventKeys.SUBJECT);
      details.put(EventKeys.NEW_VALUE, properties.get(EventKeys.NEW_VALUE));
      CalendarFactory.getModel().updateEvent(details);
    }

  }


  @Override
  public void exportCalendar(String name) throws IOException {
    try {
      FileOutputStream file = new FileOutputStream(name);
      CalendarFactory.setExport(new CalendarExport(file));
      CalendarFactory.getExport().displayOutput(CalendarFactory.getModel().getEventsForDisplay());
    } catch (IOException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void setCalendar(String name) {
    CalendarEvent event = CalendarFactory.getGroup().getCalendarEvent(name);
    CalendarFactory.setModel(event);
  }

  @Override
  public void importCalendar(Map<String, Object> properties) {
    try {
      CalendarFactory.getModel().addEvent(properties);
    } catch (Exception e) {
      throw new IllegalArgumentException("Calendar is Busy");
    }

  }
}
