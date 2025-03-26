package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Mock class for CalendarEvent.
 */
public class MockModel implements CalendarEvent {

  private List<Map<String, Object>> events;

  private String name;

  public MockModel(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public MockModel() {
    events = new ArrayList<>();
    // Adding mock data
    Map<String, Object> event = new HashMap<>();
    event.put(EventKeys.SUBJECT, "Meeting");
    event.put(EventKeys.START_DATETIME, "2025-03-21T10:00:00");
    event.put(EventKeys.END_DATETIME, "2025-03-21T11:00:00");
    event.put(EventKeys.LOCATION, "Room A");
    event.put(EventKeys.DESCRIPTION, "Team meeting");
    event.put(EventKeys.PRIVATE, false);
    events.add(event);
  }

  @Override
  public boolean checkForDuplicates(Event event) {
    return false;
  }

  @Override
  public void addEvent(Map<String, Object> event) {
    events.add(event);
  }

  @Override
  public void updateEvent(Map<String, Object> eventsMap) {

  }

  @Override
  public List<Map<String, Object>> getEventsForDisplay() {
    return events;
  }


  @Override
  public List<Map<String, Object>> getEventForDisplay(String subject) {
    return getEventsForDisplay();
  }

  @Override
  public List<Map<String, Object>> getEventForDisplay(LocalDateTime startTime, LocalDateTime endTime) {
    return getEventsForDisplay();
  }

  @Override
  public List<Map<String, Object>> getEventForDisplay(LocalDate startDate, LocalDate endDate) {
    return getEventsForDisplay();
  }

  @Override
  public List<Map<String, Object>> getUserStatus(LocalDateTime date) {
    return getEventsForDisplay();
  }

}
