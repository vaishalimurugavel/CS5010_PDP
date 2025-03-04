package calendar.model;

import java.time.LocalDateTime;
import java.util.Map;

public class SingleCalenderEvent extends CalendarEvent{

  public SingleCalenderEvent(){
    super();
  }
  @Override
  public boolean addEvent(Map<String, Object> eventDes) {
    String subject = (String)eventDes.getOrDefault(Event.EventKeys.SUBJECT,"New Event_" + this.eventList.size());
    String location = (String)eventDes.getOrDefault(Event.EventKeys.LOCATION,"Online");
    String description = (String)eventDes.getOrDefault(Event.EventKeys.DESCRIPTION,"New event Description");
    LocalDateTime start = (LocalDateTime) eventDes.getOrDefault(Event.EventKeys.START_DATETIME,LocalDateTime.now());
    LocalDateTime end = (LocalDateTime) eventDes.getOrDefault(Event.EventKeys.END_DATETIME,LocalDateTime.now());
    int eventType = (int)eventDes.getOrDefault(Event.EventKeys.EVENT_TYPE,0);
    boolean autoDecline = (boolean) eventDes.getOrDefault(Event.EventKeys.AUTO_DECLINE,false);
    if(autoDecline){
      boolean duplicate = checkForDuplicates((LocalDateTime) eventDes.get(Event.EventKeys.START_DATETIME), (LocalDateTime) eventDes.get(Event.EventKeys.END_DATETIME));
      if(duplicate){
        throw new IllegalArgumentException("Calender shows busy!");
      }
    }
    Event single = new Event(subject,location,description,start,end,eventType);
    this.eventList.add(single);
    return true;
  }

  @Override
  public boolean editEvent(Map<String, Object> eventDes) {


    return false;
  }
}
