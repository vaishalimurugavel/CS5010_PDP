package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * RecurringCalendar event extends the CalendarEvent abstract class.
 * This class adds any recurring event to the CalenderEvent.
 * </p>
 */
public class RecurringCalendarEvent extends CalendarEvent {

  @Override
  public void addEvent(Map<String, Object> eventDes) {
    String subject = (String) eventDes.get(EventKeys.SUBJECT);
    Event.EventBuilder event = new Event.EventBuilder(subject);

    LocalDateTime start = null;
    LocalDateTime end = null;
    LocalDateTime allDateTime = null;
    LocalDate allDate = null;

    if (eventDes.containsKey(EventKeys.START_DATETIME)) {
      start = (LocalDateTime) eventDes.get(EventKeys.START_DATETIME);
      event = event.startDateTime(start);
    }
    if (eventDes.containsKey(EventKeys.END_DATETIME)) {
      end = (LocalDateTime) eventDes.get(EventKeys.END_DATETIME);
      event = event.endDateTime(end);
    }
    if (eventDes.containsKey(EventKeys.ALLDAY_DATE)) {
      allDate = (LocalDate) eventDes.get(EventKeys.ALLDAY_DATE);
      event = event.allDate(allDate);
      event = event.allDay(true);
    }
    if (eventDes.containsKey(EventKeys.ALLDAY_DATETIME)) {
      allDateTime = (LocalDateTime) eventDes.get(EventKeys.ALLDAY_DATETIME);
      event = event.allDateTime(allDateTime);
      event = event.allDay(true);
    }


    boolean autoDecline = (boolean) eventDes.getOrDefault(EventKeys.AUTO_DECLINE, false);
    if (autoDecline && checkForDuplicates(start, end, allDateTime, allDate)) {
      throw new IllegalArgumentException("Calender shows busy!");
    }

    String location = (String) eventDes.getOrDefault(EventKeys.LOCATION, "Online");
    event = event.location(location);
    String description = (String) eventDes.getOrDefault(EventKeys.DESCRIPTION,
            "New event Description");
    event = event.description(description);
    int isPrivate = (int) eventDes.getOrDefault(EventKeys.PRIVATE, 0);
    event = event.privateEvent(isPrivate);

    if (eventDes.containsKey(EventKeys.OCCURRENCES)) {
      int occ = (int) eventDes.get(EventKeys.OCCURRENCES);
      event = event.occurrences(occ);
    }
    if (eventDes.containsKey(EventKeys.WEEKDAYS)) {
      String weekDays = (String) eventDes.get(EventKeys.WEEKDAYS);
      event = event.weekdays(weekDays);
    }
    if (eventDes.containsKey(EventKeys.REPEAT_DATETIME)) {
      LocalDateTime repeatDateTime = (LocalDateTime) eventDes.get(EventKeys.REPEAT_DATETIME);
      event = event.repeatDateTime(repeatDateTime);
    }
    if (eventDes.containsKey(EventKeys.REPEAT_DATE)) {
      LocalDate repeatDate = (LocalDate) eventDes.get(EventKeys.REPEAT_DATE);
      event = event.repeatDate(repeatDate);
    }


    this.addEvent(event.build());

  }

}
