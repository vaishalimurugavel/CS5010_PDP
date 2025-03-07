package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class SingleCalenderEvent extends CalendarEvent {

  @Override
  public void addEvent(Map<String, Object> eventDes) {
    String subject = (String) eventDes.get(EventKeys.SUBJECT);
    String location = (String) eventDes.getOrDefault(EventKeys.LOCATION, "Online");
    String description = (String) eventDes.getOrDefault(EventKeys.DESCRIPTION, "New event Description");
    int isPrivate = (int) eventDes.getOrDefault(EventKeys.PRIVATE, 0);

    LocalDateTime start = null ;
    LocalDateTime end = null;
    LocalDate allDAyEnd = null;
    LocalDateTime allDateTime =null;
    boolean allDay = false;
    switch ((EventKeys.EventType)eventDes.get(EventKeys.EVENT_TYPE)){
      case SINGLE:
        start = (LocalDateTime) eventDes.getOrDefault(EventKeys.START_DATETIME, LocalDateTime.now());
        end = (LocalDateTime) eventDes.getOrDefault(EventKeys.END_DATETIME, LocalDateTime.now());
        break;
      case ALL_DAY:
        if (eventDes.containsKey(EventKeys.ALLDAY_DATE)) {
          allDAyEnd = (LocalDate) eventDes.getOrDefault(EventKeys.ALLDAY_DATETIME, LocalDate.now());
        } else if (eventDes.containsKey(EventKeys.ALLDAY_DATETIME)) {
          allDateTime = (LocalDateTime) eventDes.getOrDefault(EventKeys.ALLDAY_DATETIME, LocalDateTime.now());
        }
        allDay = true;

    }
    boolean autoDecline = (boolean) eventDes.getOrDefault(EventKeys.AUTO_DECLINE, false);
    if (autoDecline) {
      boolean duplicate = checkForDuplicates((LocalDateTime) eventDes.get(EventKeys.START_DATETIME), (LocalDateTime) eventDes.get(EventKeys.END_DATETIME));
      if (duplicate) {
        throw new IllegalArgumentException("Calender shows busy!");
      }
    }
    Event single = new Event(subject, location, description, start, end, isPrivate, allDay,allDAyEnd, allDateTime);
    this.eventList.add(single);
  }
}
