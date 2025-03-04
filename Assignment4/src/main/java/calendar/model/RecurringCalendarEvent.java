package calendar.model;

import java.time.LocalDateTime;
import java.util.Map;

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

    if(autoDecline){
      boolean duplicate = checkForDuplicates((LocalDateTime) eventDes.get(Event.EventKeys.START_DATETIME), (LocalDateTime) eventDes.get(Event.EventKeys.END_DATETIME));
      if(duplicate){
        throw new IllegalArgumentException("Calender shows busy!");
      }
      Event single = new RecurringEvent(subject,location,description,start,end,eventType,rec,occ);
      this.eventList.add(single);
    }
    return true;
  }

  @Override
  public boolean editEvent(Map<String, Object> eventDes) {
    return false;
  }
}
