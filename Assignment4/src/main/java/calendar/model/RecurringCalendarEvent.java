package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class RecurringCalendarEvent extends CalendarEvent{

  @Override
  public void addEvent(Map<String, Object> eventDes) {
    String subject = (String) eventDes.get(EventKeys.SUBJECT);
    Event.EventBuilder Event = new Event.EventBuilder(subject);

    LocalDateTime start = null;
    LocalDateTime end = null;
    LocalDateTime allDateTime = null;
    LocalDate allDate = null;

    if(eventDes.containsKey(EventKeys.START_DATETIME)) {
      start = (LocalDateTime) eventDes.get(EventKeys.START_DATETIME);
      Event = Event.startDateTime(start);
    }
    if(eventDes.containsKey(EventKeys.END_DATETIME)) {
      end = (LocalDateTime) eventDes.get(EventKeys.END_DATETIME);
      Event = Event.endDateTime(end);
    }
    if(eventDes.containsKey(EventKeys.ALLDAY_DATE)){
      allDate = (LocalDate) eventDes.get(EventKeys.ALLDAY_DATE);
      Event = Event.allDate(allDate);
      Event = Event.allDay(true);
    }
    if(eventDes.containsKey(EventKeys.ALLDAY_DATETIME)) {
      allDateTime = (LocalDateTime) eventDes.get(EventKeys.ALLDAY_DATETIME);
      Event = Event.allDateTime(allDateTime);
      Event = Event.allDay(true);
    }


    boolean autoDecline = (boolean) eventDes.getOrDefault(EventKeys.AUTO_DECLINE,false);
    if(autoDecline && checkForDuplicates(start,end, allDateTime, allDate)) {
      throw new IllegalArgumentException("Calender shows busy!");
    }

    String location = (String) eventDes.getOrDefault(EventKeys.LOCATION, "Online");
    Event = Event.location(location);
    String description = (String) eventDes.getOrDefault(EventKeys.DESCRIPTION, "New event Description");
    Event = Event.description(description);
    int isPrivate = (int) eventDes.getOrDefault(EventKeys.PRIVATE, 0);
    Event = Event.privateEvent(isPrivate);

    if (eventDes.containsKey(EventKeys.OCCURRENCES)) {
      int occ = (int) eventDes.get(EventKeys.OCCURRENCES);
      Event = Event.occurrences(occ);
    }
    if (eventDes.containsKey(EventKeys.WEEKDAYS)) {
      String weekDays = (String) eventDes.get(EventKeys.WEEKDAYS);
      Event = Event.weekdays(weekDays);
    }
    if (eventDes.containsKey(EventKeys.REPEAT_DATETIME)) {
      LocalDateTime repeatDateTime = (LocalDateTime) eventDes.get(EventKeys.REPEAT_DATETIME);
      Event = Event.repeatDateTime(repeatDateTime);
    }
    if (eventDes.containsKey(EventKeys.REPEAT_DATE)) {
      LocalDate repeatDate = (LocalDate) eventDes.get(EventKeys.REPEAT_DATE);
      Event = Event.repeatDate(repeatDate);
    }


    this.addEvent(Event.build());

  }

}
