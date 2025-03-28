package calendar.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
 * ControllerGroupCommand class is responsible for handling group-related commands in the
 * calendar system.
 * These commands include creating and using calendars, copying events between calendars,
 * and editing calendar events.
 * The class implements the ControllerCommand interface and overrides the execute method to
 * process various
 * group-based calendar operations.
 * </p>
 **/
public class ControllerGroupCommand implements ControllerCommand {

  @Override
  public void execute(String command) throws IllegalArgumentException {
    String createCalendar = "create calendar --name ([A-Za-z0-9]+) " +
            "--timezone ([A-Za-z]+/[A-Za-z_]+)";
    Pattern create = Pattern.compile(createCalendar);
    Matcher createMatcher = create.matcher(command);

    String useCalendar = "use calendar --name ([A-Za-z0-9]+)";
    Pattern useCalPattern = Pattern.compile(useCalendar);
    Matcher useCalMatcher = useCalPattern.matcher(command);

    String targetPattern = " --target ([A-Za-z0-9_]+)";

    String copyCalendar = "copy event (.+?) on (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})"
            + targetPattern + " to (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})";
    Pattern copyCalPattern = Pattern.compile(copyCalendar);
    Matcher copyCalMatcher = copyCalPattern.matcher(command);

    String copyCalendar1 = "copy events on (\\d{4}-\\d{2}-\\d{2})" + targetPattern
            + " to (\\d{4}-\\d{2}-\\d{2})";
    Pattern copyCalPattern1 = Pattern.compile(copyCalendar1);
    Matcher copyCalMatcher1 = copyCalPattern1.matcher(command);


    String copyCalendar2 = "copy events between (\\d{4}-\\d{2}-\\d{2}) and (\\d{4}-\\d{2}-\\d{2})"
            + targetPattern + " to (\\d{4}-\\d{2}-\\d{2})";
    Pattern copyCalPattern2 = Pattern.compile(copyCalendar2);
    Matcher copyCalMatcher2 = copyCalPattern2.matcher(command);

    String editCalendar = "edit calendar --name ([A-Za-z0-9_]+) --property timezone" +
            " ([A-Za-z]+/[A-Za-z_]+)|edit calendar --name ([A-Za-z0-9_]+)" +
            " --property name ([A-Za-z0-9]+)";
    Pattern editCalPattern = Pattern.compile(editCalendar);
    Matcher editCalMatcher = editCalPattern.matcher(command);

    if (createMatcher.matches()) {
      CalendarFactory.getGroup().addCalendar(createMatcher.group(1), createMatcher.group(2));
    } else if (useCalMatcher.matches()) {
      CalendarEvent model = CalendarFactory.getGroup().getCalendarEvent(useCalMatcher.group(1));
      CalendarFactory.setModel(model);
      CalendarFactory.getGroup().setCurrentCalendar(useCalMatcher.group(1));
      System.out.println("Using Calendar Event: " + useCalMatcher.group(1));
    } else if (copyCalMatcher.matches()) {
      String eventName = copyCalMatcher.group(1);
      String cal2 = copyCalMatcher.group(3);
      LocalDateTime on = LocalDateTime.parse(copyCalMatcher.group(2));
      LocalDateTime to = LocalDateTime.parse(copyCalMatcher.group(4));
      ZoneId sourceZone = CalendarFactory.getGroup().getCurrentCalendar().getZoneId();
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
      String cal2 = copyCalMatcher1.group(2);
      LocalDate on = LocalDate.parse(copyCalMatcher1.group(1));
      LocalDate to = LocalDate.parse(copyCalMatcher1.group(3));
      ZoneId targetZone = CalendarFactory.getGroup().getCalendar(cal2).getZoneId();

      List<Map<String, Object>> events = CalendarFactory.getModel().getEvents();

      events = events.stream().filter(e -> {
        LocalDate allDayDate = (LocalDate) e.get(EventKeys.ALLDAY_DATE);
        LocalDateTime startDateTime = (LocalDateTime) e.get(EventKeys.START_DATETIME);

        boolean isInDateRange = false;

        if (allDayDate != null) {
          isInDateRange = (allDayDate.isEqual(on) || (allDayDate.isAfter(on)
                  && allDayDate.isBefore(to)) || allDayDate.isEqual(to));
        } else if (startDateTime != null) {
          LocalDate eventDate = startDateTime.toLocalDate();
          isInDateRange = (eventDate.isEqual(on) ||
                  (eventDate.isAfter(on) && eventDate.isBefore(to)) || eventDate.isEqual(to));
        }

        return isInDateRange;
      }).collect(Collectors.toList());
      for (Map<String, Object> event : events) {
        LocalDateTime startDateTime = (LocalDateTime) event.get(EventKeys.START_DATETIME);

        if (startDateTime != null) {
          LocalDateTime adjustedStDt = to.atStartOfDay();
          ZonedDateTime startZoned =
                  adjustedStDt.atZone(CalendarFactory.getGroup().getCurrentCalendar().getZoneId());
          ZonedDateTime startConverted = startZoned.withZoneSameInstant(targetZone);

          event.put(EventKeys.START_DATETIME, startConverted.toLocalDateTime());
        }

        CalendarFactory.getGroup().getCalendarEvent(cal2).addEvent(event);
      }
    } else if (copyCalMatcher2.matches()) {
      String cal2 = copyCalMatcher2.group(3);
      LocalDate on = LocalDate.parse(copyCalMatcher2.group(1));
      LocalDate to = LocalDate.parse(copyCalMatcher2.group(2));
      LocalDate targetTimeZone = LocalDate.parse(copyCalMatcher2.group(4));

      List<Map<String, Object>> events = CalendarFactory.getModel().getEvents();

      if (!events.isEmpty()) {
        events = events.stream()
                .filter(e -> {

                  LocalDate start = (e.get(EventKeys.START_DATETIME) != null) ?
                          ((LocalDateTime) e.get(EventKeys.START_DATETIME)).toLocalDate() : null;
                  LocalDate end = (e.get(EventKeys.END_DATETIME) != null) ?
                          ((LocalDateTime) e.get(EventKeys.END_DATETIME)).toLocalDate() : null;

                  LocalDate allDayDate = (LocalDate) e.get(EventKeys.ALLDAY_DATE);
                  LocalDate repeatDate = (LocalDate) e.get(EventKeys.REPEAT_DATE);

                  boolean isAllDayDateValid = allDayDate != null && repeatDate != null;
                  boolean isStartEndDateValid = start != null && end != null;

                  boolean isWithinDateRange = false;

                  if (isAllDayDateValid) {
                    isWithinDateRange = (allDayDate.isAfter(on) && allDayDate.isBefore(to)) ||
                            (repeatDate.isAfter(on) && repeatDate.isBefore(to)) ||
                            (allDayDate.isEqual(on) || allDayDate.isEqual(to));
                  } else if (isStartEndDateValid) {
                    isWithinDateRange = (start.isAfter(on) && start.isBefore(to)) ||
                            (end.isAfter(on) && end.isBefore(to)) ||
                            (start.isEqual(on) || start.isEqual(to));
                  }

                  return isWithinDateRange;
                })
                .collect(Collectors.toList());

        for (Map<String, Object> event : events) {
          LocalDateTime startTime = (LocalDateTime) event.get(EventKeys.START_DATETIME);
          LocalDateTime endTime = (LocalDateTime) event.get(EventKeys.END_DATETIME);

          LocalDate allDayDate = (LocalDate) event.get(EventKeys.ALLDAY_DATE);
          LocalDate repeatDate = (LocalDate) event.get(EventKeys.REPEAT_DATE);

          if (startTime != null && endTime != null) {
            long eventDuration = ChronoUnit.DAYS.between(startTime.toLocalDate(),
                    endTime.toLocalDate());
            LocalDate shiftedEnd = targetTimeZone.plusDays(eventDuration);

            event.put(EventKeys.START_DATETIME, targetTimeZone.atStartOfDay());
            event.put(EventKeys.END_DATETIME, shiftedEnd.atStartOfDay());

          } else if (repeatDate != null && allDayDate != null) {
            long daysBetween = ChronoUnit.DAYS.between(allDayDate, repeatDate);
            LocalDate shiftedEnd = targetTimeZone.plusDays(daysBetween);

            event.put(EventKeys.ALLDAY_DATE, targetTimeZone);
            event.put(EventKeys.REPEAT_DATE, shiftedEnd);
          }

          CalendarFactory.getGroup().getCalendarEvent(cal2).addEvent(event);
        }
      }
    } else if (editCalMatcher.matches()) {

      String calName;
      String propName;
      String newValue;

      if (command.contains("timezone")) {
        propName = EventKeys.TIMEZONE;
        calName = editCalMatcher.group(1);
        newValue = editCalMatcher.group(2);
      } else if (command.contains("name")) {
        propName = EventKeys.CALENDAR_NAME;
        calName = editCalMatcher.group(3);
        newValue = editCalMatcher.group(4);
      } else {
        throw new RuntimeException("Unsupported command: " + editCalMatcher.group(1));
      }
      Map<String, Object> edit = new HashMap<>();
      edit.put(EventKeys.CALENDAR_NAME, calName);
      edit.put(EventKeys.PROPERTY, propName);
      edit.put(EventKeys.NEW_VALUE, newValue);

      CalendarFactory.getGroup().updateCalendar(edit);
    }

    else {
      throw new IllegalArgumentException("Invalid command: " + command);
    }
  }
}
