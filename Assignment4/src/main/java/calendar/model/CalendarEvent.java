package calendar.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

  abstract public boolean addEvent(Map<String, Object> eventDes);
  abstract public boolean editEvent(Map<String, Object> eventDes);
}
