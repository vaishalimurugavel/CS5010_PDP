package calendar.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CalendarEvent {

  List<Event> eventList;

  public CalendarEvent() {
    eventList = new ArrayList<Event>();
  }
  protected boolean checkForDuplicates(LocalDateTime start, LocalDateTime end){
    for(Event e: this.eventList){
      LocalDateTime start2 = e.getStartDateTime();
      LocalDateTime end2 = e.getEndDateTime();
      if((start.isBefore(end2) && start2.isBefore(end))){
        return true;
      }
    }
    return false;
  }

  public List<Event> getEventList(){
    return this.eventList;
  }

  abstract public void addEvent(Map<String, Object> eventDes);

  public void editEvent(Map<String, Object> eventDes){
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
    else if(start != null) {
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
  }

}
