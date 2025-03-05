package calendar.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecurringCalendarEvent extends CalendarEvent{

  @Override
  public boolean addEvent(Map<String, Object> eventDes) {
    String subject = (String)eventDes.get(RecurringEvent.EventKeys.SUBJECT);
    String location = (String)eventDes.get(RecurringEvent.EventKeys.LOCATION);
    String description = (String)eventDes.get(RecurringEvent.EventKeys.DESCRIPTION);
    LocalDateTime start = (LocalDateTime) eventDes.get(RecurringEvent.EventKeys.START_DATETIME);
    LocalDateTime end = (LocalDateTime) eventDes.get(RecurringEvent.EventKeys.END_DATETIME);
    int eventType = Integer.parseInt((String)eventDes.get(RecurringEvent.EventKeys.EVENT_TYPE));
    boolean autoDecline = (boolean) eventDes.get(RecurringEvent.EventKeys.AUTO_DECLINE);
    int[] rec = (int[]) eventDes.get(RecurringEvent.RecurringEventKeys.WEEKDAYS);
    int occ = (int) eventDes.get(RecurringEvent.RecurringEventKeys.OCCURRENCES);
    boolean allDay = (boolean) eventDes.get(RecurringEvent.EventKeys.ALL_DAY);

    if(autoDecline){
      boolean duplicate = checkForDuplicates((LocalDateTime) eventDes.get(Event.EventKeys.START_DATETIME), (LocalDateTime) eventDes.get(Event.EventKeys.END_DATETIME));
      if(duplicate){
        throw new IllegalArgumentException("Calender shows busy!");
      }
      Event single = new RecurringEvent(subject,location,description,start,end,eventType,allDay,rec,occ);
      this.eventList.add(single);
    }
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
        case Event.EventKeys.START_DATETIME:
          event.setStartDateTime(start);
          break;
        case Event.EventKeys.END_DATETIME:
          event.setEndDateTime(end);
          break;
        case Event.EventKeys.EVENT_TYPE:
          event.setEventType(Integer.parseInt((String) eventDes.get(Event.EventKeys.EVENT_TYPE)));
          break;
        case Event.EventKeys.SUBJECT:
          event.setSubject((String) eventDes.get(Event.EventKeys.SUBJECT));
          break;
        case Event.EventKeys.LOCATION:
          event.setLocation((String) eventDes.get(Event.EventKeys.LOCATION));
          break;
        case Event.EventKeys.DESCRIPTION:
          event.setDescription((String) eventDes.get(Event.EventKeys.DESCRIPTION));
          break;
      }
    }
    return true;
  }
}
