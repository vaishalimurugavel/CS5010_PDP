package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * Created at 22-03-2025
 * Author Vaishali
 **/

public interface CalendarEvent {

  boolean checkForDuplicates(Event event);
  void addEvent(Map<String, Object> eventsMap);
  void updateEvent(Map<String, Object> eventsMap);
  List<Map<String,String>> getEventsForDisplay();
  List<Map<String,String>> getEventForDisplay(Event event);
  List<Map<String,String>> getEventForDisplay(String subject);
  List<Map<String,String>> getEventForDisplay(LocalDateTime startTime, LocalDateTime endTime);
  List<Map<String,String>> getEventForDisplay(LocalDate startDate, LocalDate endDate);
  List<Map<String,String>> getUserStatus(LocalDateTime date);

}
