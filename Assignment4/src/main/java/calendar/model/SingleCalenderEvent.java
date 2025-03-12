package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * SingleCalendar event extends the CalendarEvent abstract class.
 * This class adds any single event to the CalenderEvent.
 * </p>
 */
public class SingleCalenderEvent extends CalendarEvent {

  @Override
  public void addEvent(Map<String, Object> eventDes) {
    String subject = (String) eventDes.get(EventKeys.SUBJECT);
    Event.EventBuilder single = new Event.EventBuilder(subject);

    LocalDateTime start = null;
    LocalDateTime end = null;
    LocalDateTime allDateTime = null;

    LocalDate allDate = null;
    if (eventDes.containsKey(EventKeys.START_DATETIME)) {
      start = (LocalDateTime) eventDes.get(EventKeys.START_DATETIME);
      single = single.startDateTime(start);
    }
    if (eventDes.containsKey(EventKeys.END_DATETIME)) {
      end = (LocalDateTime) eventDes.get(EventKeys.END_DATETIME);
      single = single.endDateTime(end);
    }
    if (eventDes.containsKey(EventKeys.ALLDAY_DATE)) {
      allDate = (LocalDate) eventDes.get(EventKeys.ALLDAY_DATE);
      single = single.allDate(allDate);
      single = single.allDay(true);
    }
    if (eventDes.containsKey(EventKeys.ALLDAY_DATETIME)) {
      allDateTime = (LocalDateTime) eventDes.get(EventKeys.ALLDAY_DATETIME);
      single = single.allDateTime(allDateTime);
      single = single.allDay(true);
    }
    boolean autoDecline = (boolean) eventDes.getOrDefault(EventKeys.AUTO_DECLINE, false);
    if (autoDecline) {
      boolean duplicate = checkForDuplicates(start, end, allDateTime, allDate);
      if (duplicate) {
        throw new IllegalArgumentException("Calender shows busy!");
      }
    }

    String location = (String) eventDes.getOrDefault(EventKeys.LOCATION, "Online");
    single = single.location(location);
    String description = (String) eventDes.getOrDefault(EventKeys.DESCRIPTION, "New event Description");
    single = single.description(description);
    int isPrivate = (int) eventDes.getOrDefault(EventKeys.PRIVATE, 0);
    single.privateEvent(isPrivate);

    this.addEvent(single.build());
  }
}
