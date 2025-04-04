package calendar.model;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Manages a collection of calendar groups. This class implements the `CalendarGroup`
 * interface and provides
 * functionality for adding, retrieving, checking duplicates, and updating calendar groups.
 * </p>
 **/
public class CalendarGroupManager implements CalendarGroup {

  static Map<String, Calendars> calendarGroups = new HashMap<>();
  Calendars currentCalendar;

  public CalendarGroupManager() {
    calendarGroups.put("Default Calendar", new Calendars("Default Calendar",
            ZoneId.systemDefault(), ZoneId.systemDefault().getId()));
  }

  @Override
  public CalendarEvent getCalendarEvent(String groupName) {
    Calendars calendars = calendarGroups.get(groupName);
    CalendarEvent calendarEvent;
    if (calendars != null) {
      calendarEvent = calendars.getCalendarEventEvents();
    } else {
      throw new IllegalArgumentException("No Calendar available with the name " + groupName);
    }
    return calendarEvent;
  }

  @Override
  public Calendars getCalendar(String name) {
    if (calendarGroups.containsKey(name)) {
      return calendarGroups.get(name);
    }
    return null;
  }

  @Override
  public Calendars setCurrentCalendar(String name) {
    currentCalendar = getCalendar(name);
    return currentCalendar;
  }

  @Override
  public Calendars getCurrentCalendar() {
    if (currentCalendar == null) {
      return null;
    }
    return currentCalendar;
  }

  @Override
  public String[] getCalendarNames() {
    if(calendarGroups.isEmpty()){
      return null;
    }

    String[] names = new String[calendarGroups.size()];
    int i = 0;
    for(String calendar : calendarGroups.keySet()){
      names[i++] = calendar;
    }
    return names;
  }


  @Override
  public boolean checkForDuplicates(String name) {
    return calendarGroups.containsKey(name);
  }

  @Override
  public void addCalendar(String groupName, String timeZone) {
    if (checkForDuplicates(groupName)) {
      throw new IllegalArgumentException("Duplicate group name " + groupName);
    }
    Calendars calendars = new Calendars(groupName, ZoneId.of(timeZone), timeZone);
    calendarGroups.put(groupName, calendars);
  }

  @Override
  public void updateCalendar(Map<String, Object> prop) {
    String groupName = (String) prop.get(EventKeys.CALENDAR_NAME);
    Calendars calendars = calendarGroups.get(groupName);
    if (calendars != null) {
      String propertyName = (String) prop.get(EventKeys.PROPERTY);
      String newValue = (String) prop.get(EventKeys.NEW_VALUE);
      if (propertyName.equals(EventKeys.CALENDAR_NAME)) {
        calendars.setTitle(newValue);
        calendarGroups.remove(groupName);
        calendarGroups.put(newValue, calendars);
      } else if (propertyName.equals(EventKeys.TIMEZONE)) {
        calendars.setZoneName(newValue);
      }

    } else {
      throw new IllegalArgumentException("No Calendar available with the name " + groupName);
    }
  }
}
