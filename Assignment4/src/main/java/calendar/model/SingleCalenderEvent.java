package calendar.model;

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
    String subject = (String) eventDes.getOrDefault(Event.EventKeys.SUBJECT, "New Event_" + this.eventList.size());
    String location = (String) eventDes.getOrDefault(Event.EventKeys.LOCATION, "Online");
    String description = (String) eventDes.getOrDefault(Event.EventKeys.DESCRIPTION, "New event Description");
    LocalDateTime start = (LocalDateTime) eventDes.getOrDefault(Event.EventKeys.START_DATETIME, LocalDateTime.now());
    LocalDateTime end = (LocalDateTime) eventDes.getOrDefault(Event.EventKeys.END_DATETIME, LocalDateTime.now());
    int eventType = (int) eventDes.getOrDefault(Event.EventKeys.EVENT_TYPE, 0);
    boolean allDay = (boolean) eventDes.getOrDefault(RecurringEvent.EventKeys.ALL_DAY, Boolean.FALSE);
    boolean autoDecline = (boolean) eventDes.getOrDefault(Event.EventKeys.AUTO_DECLINE, false);
    if (autoDecline) {
      boolean duplicate = checkForDuplicates((LocalDateTime) eventDes.get(Event.EventKeys.START_DATETIME), (LocalDateTime) eventDes.get(Event.EventKeys.END_DATETIME));
      if (duplicate) {
        throw new IllegalArgumentException("Calender shows busy!");
      }
    }
    Event single = new Event(subject, location, description, start, end, eventType, allDay);
    this.eventList.add(single);
    return true;
  }

  @Override
  public boolean editEvent(Map<String, Object> eventDes) {
    LocalDateTime start;
    LocalDateTime end;
    String eventName = (String) eventDes.get(Event.EventKeys.SUBJECT);
    String property = (String) eventDes.get(Event.EventKeys.PROPERTY);

    if (eventDes.containsKey(Event.EventKeys.START_DATETIME)) {
      start = (LocalDateTime) eventDes.getOrDefault(Event.EventKeys.START_DATETIME,null);
    } else {
      start = null;
    }
    if (eventDes.containsKey(Event.EventKeys.END_DATETIME)) {
      end = (LocalDateTime) eventDes.getOrDefault(Event.EventKeys.END_DATETIME,null);
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
        case Event.EventKeys.START_DATETIME:
          LocalDateTime newStart = (LocalDateTime) eventDes.get(Event.EventKeys.NEW_VALUE);
          event.setStartDateTime(newStart);
          break;
        case Event.EventKeys.END_DATETIME:
          LocalDateTime newEnd = (LocalDateTime) eventDes.get(Event.EventKeys.NEW_VALUE);
          event.setEndDateTime(newEnd);
          break;
        case Event.EventKeys.EVENT_TYPE:
          int eventType = (int) eventDes.get(Event.EventKeys.NEW_VALUE);
          event.setEventType(eventType);
          break;
        case Event.EventKeys.SUBJECT:
          String newSubject = (String) eventDes.get(Event.EventKeys.NEW_VALUE);
          event.setSubject(newSubject);
          break;
        case Event.EventKeys.LOCATION:
          String newLocation = (String) eventDes.get(Event.EventKeys.NEW_VALUE);
          event.setLocation(newLocation);
          break;
        case Event.EventKeys.DESCRIPTION:
          String newDescription = (String) eventDes.get(Event.EventKeys.NEW_VALUE);
          event.setDescription(newDescription);
          break;
      }
    }
    return true;
  }
}
