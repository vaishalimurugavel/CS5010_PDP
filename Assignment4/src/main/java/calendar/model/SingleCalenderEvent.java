package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class SingleCalenderEvent extends CalendarEvent {

  @Override
  public void addEvent(Map<String, Object> eventDes) {
    String subject = (String) eventDes.get(EventKeys.SUBJECT);
    RecurringEvent.RecurringBuilder single = new RecurringEvent.RecurringBuilder(subject);

    LocalDateTime start = null;
    LocalDateTime end = null;
    LocalDateTime allDateTime = null;

    LocalDate allDate = null;
    if(eventDes.containsKey(EventKeys.START_DATETIME)) {
      start = (LocalDateTime) eventDes.get(EventKeys.START_DATETIME);
      single = (RecurringEvent.RecurringBuilder) single.startDateTime(start);
    }
    if(eventDes.containsKey(EventKeys.END_DATETIME)) {
      end = (LocalDateTime) eventDes.get(EventKeys.END_DATETIME);
      single = (RecurringEvent.RecurringBuilder) single.endDateTime(end);
    }
    if(eventDes.containsKey(EventKeys.ALLDAY_DATE)){
      allDate = (LocalDate) eventDes.get(EventKeys.ALLDAY_DATE);
      single = (RecurringEvent.RecurringBuilder) single.allDate(allDate);
      single = (RecurringEvent.RecurringBuilder) single.allDay(true);
    }
    if(eventDes.containsKey(EventKeys.ALLDAY_DATETIME)) {
      allDateTime = (LocalDateTime) eventDes.get(EventKeys.ALLDAY_DATETIME);
      single = (RecurringEvent.RecurringBuilder) single.allDateTime(allDateTime);
      single = (RecurringEvent.RecurringBuilder) single.allDay(true);
    }
    boolean autoDecline = (boolean) eventDes.getOrDefault(EventKeys.AUTO_DECLINE, false);
    if (autoDecline) {
      boolean duplicate = checkForDuplicates(start, end, allDateTime, allDate);
      if (duplicate) {
        throw new IllegalArgumentException("Calender shows busy!");
      }
    }

    String location = (String) eventDes.getOrDefault(EventKeys.LOCATION, "Online");
    single = (RecurringEvent.RecurringBuilder) single.location(location);
    String description = (String) eventDes.getOrDefault(EventKeys.DESCRIPTION, "New event Description");
    single = (RecurringEvent.RecurringBuilder) single.description(description);
    int isPrivate = (int) eventDes.getOrDefault(EventKeys.PRIVATE, 0);
    single.privateEvent(isPrivate);

    this.addEvent(single.build());
    System.out.println("Size of single: " + this.getEventList().size());
  }
}
