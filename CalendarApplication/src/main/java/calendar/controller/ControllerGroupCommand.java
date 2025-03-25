package calendar.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import calendar.model.CalendarEvent;
import calendar.model.EventKeys;

/**
 * <p>
 * ControllerGroupCommand class is responsible for handling group-related commands in the calendar system.
 * These commands include creating and using calendars, copying events between calendars, and editing calendar events.
 * The class implements the ControllerCommand interface and overrides the execute method to process various
 * group-based calendar operations.
 * </p>
 **/
public class ControllerGroupCommand implements ControllerCommand {

  @Override
  public void execute(String command) throws IllegalAccessException {
    String createCalendar = "create calendar --name ([A-Za-z]+) --timezone ([A-Za-z]+/[A-Za-z_]+)";
    Pattern create = Pattern.compile(createCalendar);
    Matcher createMatcher = create.matcher(command);

    String useCalendar = "use calendar --name ([A-Za-z]+)";
    Pattern useCalPattern = Pattern.compile(useCalendar);
    Matcher useCalMatcher = useCalPattern.matcher(command);

    String targetPattern = " --target ([A-Za-z0-9_]+)";

    String copyCalendar = "copy event (.+?) on (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})"
            + targetPattern + " to (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})";
    Pattern copyCalPattern = Pattern.compile(copyCalendar);
    Matcher copyCalMatcher = copyCalPattern.matcher(command);

    String copyCalendar1 = "copy event (.+?) on (\\d{4}-\\d{2}-\\d{2})" + targetPattern
            + " to (\\d{4}-\\d{2}-\\d{2})";
    Pattern copyCalPattern1 = Pattern.compile(copyCalendar1);
    Matcher copyCalMatcher1 = copyCalPattern1.matcher(command);


    String copyCalendar2 = "copy events between (\\d{4}-\\d{2}-\\d{2}) and (\\d{4}-\\d{2}-\\d{2})"
            + targetPattern + " to (\\d{4}-\\d{2}-\\d{2})";
    Pattern copyCalPattern2 = Pattern.compile(copyCalendar2);
    Matcher copyCalMatcher2 = copyCalPattern2.matcher(command);


    String editCalendar = "edit calendar --name ([A-Za-z0-9_]+) --property ([A-Za-z0-9_]+) ([A-Za-z0-9_]+)";
    Pattern editCalPattern = Pattern.compile(editCalendar);
    Matcher editCalMatcher = editCalPattern.matcher(command);

    if (createMatcher.matches()) {
      CalendarFactory.getGroup().addCalendar(createMatcher.group(1), createMatcher.group(2));
    }
    else if (useCalMatcher.matches()) {
      CalendarEvent model = CalendarFactory.getGroup().getCalendarEvent(useCalMatcher.group(1));
      CalendarFactory.setModel(model);
      System.out.println("Using Calendar Event: "  + useCalMatcher.group(1));
    }
    else if (copyCalMatcher.matches()) {
      String eventName = copyCalMatcher.group(1);
      String cal2 = copyCalMatcher.group(3);
      LocalDateTime on = LocalDateTime.parse(copyCalMatcher.group(2));
      LocalDateTime to = LocalDateTime.parse(copyCalMatcher.group(4));
      ZoneId sourceZone = ZoneId.systemDefault();
      ZoneId targetZone = CalendarFactory.getGroup().getCalendar(cal2).getZoneId();

      ZonedDateTime onZoned = on.atZone(sourceZone).withZoneSameInstant(targetZone);
      ZonedDateTime toZoned = to.atZone(sourceZone).withZoneSameInstant(targetZone);

      List<Map<String, Object>> events = CalendarFactory.getModel().getEventForDisplay(eventName);
      events = events.stream().filter(
              (e) -> {
                ZonedDateTime eventStart = ((LocalDateTime) e.get(EventKeys.START_DATETIME))
                        .atZone(sourceZone).withZoneSameInstant(targetZone);
                ZonedDateTime eventEnd = e.get(EventKeys.END_DATETIME) != null
                        ? ((LocalDateTime) e.get(EventKeys.END_DATETIME)).atZone(sourceZone)
                        .withZoneSameInstant(targetZone) : null;
                return eventStart.toLocalDateTime().isEqual(onZoned.toLocalDateTime())
                        || (eventStart.toLocalDateTime().isBefore(toZoned.toLocalDateTime())
                        && eventEnd != null
                        && eventEnd.toLocalDateTime().isAfter(onZoned.toLocalDateTime()))
                        || (eventStart.toLocalDateTime().isBefore(toZoned.toLocalDateTime())
                        && eventStart.toLocalDateTime().isAfter(onZoned.toLocalDateTime()));
              }).collect(Collectors.toList());

      for (Map<String, Object> event : events) {
        event.put(EventKeys.START_DATETIME, ((LocalDateTime) event.get(EventKeys.START_DATETIME))
                .atZone(sourceZone).withZoneSameInstant(targetZone).toLocalDateTime());
        if (event.get(EventKeys.END_DATETIME) != null) {
          event.put(EventKeys.END_DATETIME, ((LocalDateTime) event.get(EventKeys.END_DATETIME))
                  .atZone(sourceZone).withZoneSameInstant(targetZone).toLocalDateTime());
        }
        CalendarFactory.getGroup().getCalendarEvent(cal2).addEvent(event);
      }
    } else if (copyCalMatcher1.matches()) {
      String cal2 = copyCalMatcher1.group(3);
      LocalDate on = LocalDate.parse(copyCalMatcher1.group(1));
      LocalDate to = LocalDate.parse(copyCalMatcher1.group(2));
      ZoneId targetZone = CalendarFactory.getGroup().getCalendar(cal2).getZoneId();

      List<Map<String, Object>> events = CalendarFactory.getModel().getEventsForDisplay();
      events = events.stream().filter(
              (e) -> {
                LocalDate eventDate = ((LocalDate) e.get(EventKeys.ALLDAY_DATE));
                return eventDate.isEqual(on)
                        || (eventDate.isAfter(on) && eventDate.isBefore(to))
                        || (eventDate.isEqual(to));
              }).collect(Collectors.toList());

      for (Map<String, Object> event : events) {
        CalendarFactory.getGroup().getCalendarEvent(cal2).addEvent(event);
      }
    }

    else if (copyCalMatcher2.matches()) {

      String eventName = copyCalMatcher2.group(1);
      String cal2 = copyCalMatcher2.group(3);
      LocalDate on = LocalDate.parse(copyCalMatcher2.group(2));
      LocalDate to = LocalDate.parse(copyCalMatcher2.group(4));
      List<Map<String,Object>> events = CalendarFactory.getModel().getEventForDisplay(eventName);
      events = events.stream().filter(
                      (e) -> ((LocalDate) e.get(EventKeys.ALLDAY_DATE)).isEqual(on)
                              || (((LocalDate) e.get(EventKeys.ALLDAY_DATE)).isBefore(to)
                              && ( e.get(EventKeys.REPEAT_DATE) != null
                              && ((LocalDate) e.get(EventKeys.REPEAT_DATE)).isAfter(on)))
                              || (((LocalDate) e.get(EventKeys.ALLDAY_DATE)).isBefore(to))
                              && ((LocalDate) e.get(EventKeys.ALLDAY_DATE)).isAfter(on))
              .collect(Collectors.toList());
      for (Map<String, Object> event : events) {
        CalendarFactory.getGroup().getCalendarEvent(cal2).addEvent(event);
      }
    }

    else if(editCalMatcher.matches()) {

      String calName = editCalMatcher.group(1);
      String propName = editCalMatcher.group(2);
      String newValue = editCalMatcher.group(3);

      Map<String, Object> edit = new HashMap<>();
      edit.put(EventKeys.CALENDAR_NAME, calName);
      edit.put(EventKeys.PROPERTY, propName);
      edit.put(EventKeys.NEW_VALUE, newValue);

      CalendarFactory.getGroup().updateCalendar(edit);
    }
  }
}
