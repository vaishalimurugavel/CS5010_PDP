package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SingleCalenderEvent extends CalendarEvent {

  public SingleCalenderEvent() {
    super();
  }

  @Override
  public boolean addEvent(Map<String, Object> eventDes) {
    String subject = (String) eventDes.get(EventKeys.SUBJECT);
    String location = (String) eventDes.getOrDefault(EventKeys.LOCATION, "Online");
    String description = (String) eventDes.getOrDefault(EventKeys.DESCRIPTION, "New event Description");
    int isPrivate = (int) eventDes.getOrDefault(EventKeys.PRIVATE, 0);

    LocalDateTime start = null ;
    LocalDateTime end = null;
    LocalDate allDAyEnd = null;
    boolean allDay = false;
    switch ((EventKeys.EventType)eventDes.get(EventKeys.EVENT_TYPE)){
      case SINGLE:
        start = (LocalDateTime) eventDes.getOrDefault(EventKeys.START_DATETIME, LocalDateTime.now());
        end = (LocalDateTime) eventDes.getOrDefault(EventKeys.END_DATETIME, LocalDateTime.now());
        break;
      case ALL_DAY:
        allDAyEnd = (LocalDate) eventDes.getOrDefault(EventKeys.ALLDAY_DATETIME, LocalDate.now());
        allDay = true;

    }
    boolean autoDecline = (boolean) eventDes.getOrDefault(EventKeys.AUTO_DECLINE, false);
    if (autoDecline) {
      boolean duplicate = checkForDuplicates((LocalDateTime) eventDes.get(EventKeys.START_DATETIME), (LocalDateTime) eventDes.get(EventKeys.END_DATETIME));
      if (duplicate) {
        throw new IllegalArgumentException("Calender shows busy!");
      }
    }
    Event single = new Event(subject, location, description, start, end, isPrivate, allDay,allDAyEnd);
    this.eventList.add(single);
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
    
    List<Event> filtered = eventList.stream()
            .filter(event -> event.getSubject().equals(eventName))
            .filter(event -> start == null || event.getStartDateTime().equals(start))
            .filter(event -> end == null || event.getEndDateTime().equals(end))
            .collect(Collectors.toList());

    if (filtered.isEmpty()) {
      return false; 
    }
    
    for (Event event : filtered) {
      switch (property){
        case EventKeys.START_DATETIME:
          LocalDateTime newStart = (LocalDateTime) eventDes.get(EventKeys.NEW_VALUE);
          event.setStartDateTime(newStart);
          break;
        case EventKeys.END_DATETIME:
          LocalDateTime newEnd = (LocalDateTime) eventDes.get(EventKeys.NEW_VALUE);
          event.setEndDateTime(newEnd);
          break;
        case EventKeys.PRIVATE:
          int eventType = (int) eventDes.get(EventKeys.NEW_VALUE);
          event.setEventType(eventType);
          break;
        case EventKeys.SUBJECT:
          String newSubject = (String) eventDes.get(EventKeys.NEW_VALUE);
          event.setSubject(newSubject);
          break;
        case EventKeys.LOCATION:
          String newLocation = (String) eventDes.get(EventKeys.NEW_VALUE);
          event.setLocation(newLocation);
          break;
        case EventKeys.DESCRIPTION:
          String newDescription = (String) eventDes.get(EventKeys.NEW_VALUE);
          event.setDescription(newDescription);
          break;
      }
    }
    return true;
  }
}
