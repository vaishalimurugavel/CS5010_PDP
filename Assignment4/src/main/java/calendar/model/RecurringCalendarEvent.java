package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class RecurringCalendarEvent extends CalendarEvent{

  @Override
  public void addEvent(Map<String, Object> eventDes) {
    String subject = (String) eventDes.get(EventKeys.SUBJECT);
    RecurringEvent.RecurringBuilder recurringEvent = new RecurringEvent.RecurringBuilder(subject);

    LocalDateTime start = null;
    LocalDateTime end = null;
    LocalDateTime allDateTime = null;
    LocalDate allDate = null;

    if(eventDes.containsKey(EventKeys.START_DATETIME)) {
      start = (LocalDateTime) eventDes.get(EventKeys.START_DATETIME);
      recurringEvent = (RecurringEvent.RecurringBuilder) recurringEvent.startDateTime(start);
    }
    if(eventDes.containsKey(EventKeys.END_DATETIME)) {
      end = (LocalDateTime) eventDes.get(EventKeys.END_DATETIME);
      recurringEvent = (RecurringEvent.RecurringBuilder) recurringEvent.endDateTime(end);
    }
    if(eventDes.containsKey(EventKeys.ALLDAY_DATE)){
      allDate = (LocalDate) eventDes.get(EventKeys.ALLDAY_DATE);
      recurringEvent = (RecurringEvent.RecurringBuilder) recurringEvent.allDate(allDate);
      recurringEvent = (RecurringEvent.RecurringBuilder) recurringEvent.allDay(true);
    }
    if(eventDes.containsKey(EventKeys.ALLDAY_DATETIME)) {
      allDateTime = (LocalDateTime) eventDes.get(EventKeys.ALLDAY_DATETIME);
      recurringEvent = (RecurringEvent.RecurringBuilder) recurringEvent.allDateTime(allDateTime);
      recurringEvent = (RecurringEvent.RecurringBuilder) recurringEvent.allDay(true);
    }


    boolean autoDecline = (boolean) eventDes.getOrDefault(EventKeys.AUTO_DECLINE,false);
    if(autoDecline && checkForDuplicates(start,end, allDateTime, allDate)) {
      throw new IllegalArgumentException("Calender shows busy!");
    }

    String location = (String) eventDes.getOrDefault(EventKeys.LOCATION, "Online");
    recurringEvent = (RecurringEvent.RecurringBuilder) recurringEvent.location(location);
    String description = (String) eventDes.getOrDefault(EventKeys.DESCRIPTION, "New event Description");
    recurringEvent = (RecurringEvent.RecurringBuilder) recurringEvent.description(description);
    int isPrivate = (int) eventDes.getOrDefault(EventKeys.PRIVATE, 0);
    recurringEvent = (RecurringEvent.RecurringBuilder) recurringEvent.privateEvent(isPrivate);

    if (eventDes.containsKey(EventKeys.OCCURRENCES)) {
      int occ = (int) eventDes.get(EventKeys.OCCURRENCES);
      recurringEvent = recurringEvent.occurrences(occ);
    }
    if (eventDes.containsKey(EventKeys.WEEKDAYS)) {
      String weekDays = (String) eventDes.get(EventKeys.WEEKDAYS);
      recurringEvent = recurringEvent.weekdays(weekDays);
    }
    if (eventDes.containsKey(EventKeys.REPEAT_DATETIME)) {
      LocalDateTime repeatDateTime = (LocalDateTime) eventDes.get(EventKeys.REPEAT_DATETIME);
      recurringEvent = recurringEvent.repeatDateTime(repeatDateTime);
    }
    if (eventDes.containsKey(EventKeys.REPEAT_DATE)) {
      LocalDate repeatDate = (LocalDate) eventDes.get(EventKeys.REPEAT_DATE);
      recurringEvent = recurringEvent.repeatDate(repeatDate);
    }


    this.addEvent(recurringEvent.build());
    System.out.println("Size of Recurring: " + this.getEventList().size());

  }

}
