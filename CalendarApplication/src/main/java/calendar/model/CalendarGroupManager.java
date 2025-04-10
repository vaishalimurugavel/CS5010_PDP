package calendar.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    } else {
      throw new IllegalArgumentException("No Calendar available with the name " + name);
    }
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
    if (calendarGroups.isEmpty()) {
      return null;
    }

    String[] names = new String[calendarGroups.size()];
    int i = 0;
    for (String calendar : calendarGroups.keySet()) {
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

  /**
   * Update Calendar Property.
   *
   * @param updateProps A map containing the properties to update (e.g., name, timezone).
   */
  public void updateCalendar(Map<String, Object> updateProps) {
    String calendarName = (String) updateProps.get(EventKeys.CALENDAR_NAME);
    String property = (String) updateProps.get(EventKeys.PROPERTY);
    String newValue = (String) updateProps.get(EventKeys.NEW_VALUE);

    Calendars calendar = getCalendar(calendarName);
    if (property.equals(EventKeys.TIMEZONE)) {
      String oldTimezone = calendar.getZoneName();
      calendar.setZoneName(newValue);

      // Adjust all event times
      ZoneId oldZoneId = ZoneId.of(oldTimezone);
      ZoneId newZoneId = ZoneId.of(newValue);

      for (Map<String, Object> event : calendar.getEvents()) {
        LocalDateTime oldStart = (LocalDateTime) event.get(EventKeys.START_DATETIME);
        LocalDateTime oldEnd = (LocalDateTime) event.get(EventKeys.END_DATETIME);

        if (oldStart != null && oldEnd != null) {
          ZonedDateTime zonedStart = oldStart.atZone(oldZoneId);
          ZonedDateTime zonedEnd = oldEnd.atZone(oldZoneId);

          event.put(EventKeys.START_DATETIME, zonedStart.withZoneSameInstant(newZoneId)
                  .toLocalDateTime());
          event.put(EventKeys.END_DATETIME, zonedEnd.withZoneSameInstant(newZoneId)
                  .toLocalDateTime());
        }
      }
    } else if (property.equals(EventKeys.CALENDAR_NAME)) {
      calendar.setTitle(newValue);
      calendarGroups.put(newValue, getCalendar(calendarName));
      calendarGroups.remove(calendarName);

    }

  }

}