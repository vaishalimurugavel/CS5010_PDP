package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecurringCalendarEvent extends CalendarEvent{

  @Override
  public boolean addEvent(Map<String, Object> eventDes) {
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
    switch ((EventKeys.EventType)eventDes.get(EventKeys.EVENT_TYPE)) {
      case ALL_DAY_RECURRING:
        allDAyEnd = (LocalDate) eventDes.get(EventKeys.ALLDAY_DATETIME);
        allDay = true;
    }

    if(autoDecline && checkForDuplicates(start,end)){
      throw new IllegalArgumentException("Calender shows busy!");
    }
    Event recurringEvent = new RecurringEvent(subject,location,description,start,end,isPrivate,allDay,weekDays,occ,repeatDateTime,allDAyEnd);
    this.eventList.add(recurringEvent);

    return true;
  }

  @Override
  public boolean editEvent(Map<String, Object> eventDes) {
    LocalDateTime start;
    LocalDateTime end;
    String eventName = (String) eventDes.get(EventKeys.SUBJECT);
    String property = (String) eventDes.get(EventKeys.PROPERTY);

    if (eventDes.containsKey(EventKeys.START_DATETIME)) {
      start = (LocalDateTime) eventDes.getOrDefault(EventKeys.START_DATETIME,null);
    } else {
      start = null;
    }
    if (eventDes.containsKey(EventKeys.END_DATETIME)) {
      end = (LocalDateTime) eventDes.getOrDefault(EventKeys.END_DATETIME,null);
    } else {
      end = null;
    }

    List<Event> filtered = null;
    if(start != null && end != null)
      filtered = eventList.stream()
              .filter(event -> event.getSubject().equals(eventName)
                      && event.getStartDateTime().equals(start)
                      && event.getEndDateTime().isBefore(end))
              .collect(Collectors.toList());
    else if(start != null && end == null) {
      filtered = eventList.stream()
              .filter(event -> event.getSubject().equals(eventName)
                      && event.getStartDateTime().equals(start))
              .collect(Collectors.toList());
    }
    else {
      filtered = eventList.stream()
              .filter(event -> event.getSubject().equals(eventName))
              .collect(Collectors.toList());
    }
    for (Event event : filtered) {
      switch (property){
        case EventKeys.START_DATETIME:
          event.setStartDateTime(start);
          break;
        case EventKeys.END_DATETIME:
          event.setEndDateTime(end);
          break;
        case EventKeys.PRIVATE:
          event.setEventType(Integer.parseInt((String) eventDes.get(EventKeys.PRIVATE)));
          break;
        case EventKeys.SUBJECT:
          event.setSubject((String) eventDes.get(EventKeys.SUBJECT));
          break;
        case EventKeys.LOCATION:
          event.setLocation((String) eventDes.get(EventKeys.LOCATION));
          break;
        case EventKeys.DESCRIPTION:
          event.setDescription((String) eventDes.get(EventKeys.DESCRIPTION));
          break;
      }
    }
    return true;
  }
}
