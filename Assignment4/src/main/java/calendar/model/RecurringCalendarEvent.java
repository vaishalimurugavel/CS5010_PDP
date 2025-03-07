package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class RecurringCalendarEvent extends CalendarEvent{

  @Override
  public void addEvent(Map<String, Object> eventDes) {
    String subject = (String) eventDes.get(EventKeys.SUBJECT);
    String location = (String) eventDes.getOrDefault(EventKeys.LOCATION, "Online");
    String description = (String) eventDes.getOrDefault(EventKeys.DESCRIPTION, "New event Description");
    int isPrivate = (int) eventDes.getOrDefault(EventKeys.PRIVATE, 0);
    boolean autoDecline = (boolean) eventDes.getOrDefault(EventKeys.AUTO_DECLINE,false);
    LocalDateTime start = null;
    LocalDateTime end = null;
    LocalDate allDAyEnd = null;
    LocalDateTime repeatDateTime =null;
    boolean allDay = false;
    int occ = 0;
    String weekDays = null;
    if (eventDes.containsKey(EventKeys.OCCURRENCES)) {
      occ = (int) eventDes.get(EventKeys.OCCURRENCES);
    }
    if (eventDes.containsKey(EventKeys.WEEKDAYS)) {
      weekDays = (String) eventDes.get(EventKeys.WEEKDAYS);
    }
    if (eventDes.containsKey(EventKeys.REPEAT_DATETIME)) {
      repeatDateTime = (LocalDateTime) eventDes.get(EventKeys.REPEAT_DATETIME);
    }
    if(eventDes.containsKey(EventKeys.START_DATETIME)) {
      start = (LocalDateTime) eventDes.get(EventKeys.START_DATETIME);
    }
    if(eventDes.containsKey(EventKeys.END_DATETIME)) {
      end = (LocalDateTime) eventDes.get(EventKeys.END_DATETIME);
    }
    if (Objects.requireNonNull((EventKeys.EventType) eventDes.get(EventKeys.EVENT_TYPE)) == EventKeys.EventType.ALL_DAY_RECURRING) {
      allDAyEnd = (LocalDate) eventDes.get(EventKeys.ALLDAY_DATETIME);
      allDay = true;
    }

    if(autoDecline && checkForDuplicates(start,end)){
      throw new IllegalArgumentException("Calender shows busy!");
    }
    Event recurringEvent = new RecurringEvent(subject,location,description,start,end,isPrivate,allDay,weekDays,occ,repeatDateTime,allDAyEnd);
    this.eventList.add(recurringEvent);

  }

}
