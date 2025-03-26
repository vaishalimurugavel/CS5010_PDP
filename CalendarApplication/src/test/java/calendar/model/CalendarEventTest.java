package calendar.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class CalendarEventTest {

  private CalendarEvent eventManager;
  private Map<String, Object> event1;
  private Map<String, Object> event2;

  @Before
  public void setUp() {
    eventManager = new CalenderEventManager();

    event1 = new HashMap<>();
    event1.put(EventKeys.SUBJECT, "Meeting");
    event1.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-03-21T10:00:00"));
    event1.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-03-21T11:00:00"));
    event1.put(EventKeys.LOCATION, "Room A");
    event1.put(EventKeys.DESCRIPTION, "Team meeting");
    event1.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);

    event2 = new HashMap<>();
    event2.put(EventKeys.SUBJECT, "Workshop");
    event2.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-03-21T11:30:00"));
    event2.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-03-21T13:00:00"));
    event2.put(EventKeys.LOCATION, "Room B");
    event2.put(EventKeys.DESCRIPTION, "Technical workshop");
    event2.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);
  }

  @Test
  public void testAddEvent() {
    eventManager.addEvent(event1);
    List<Map<String, Object>> events = eventManager.getEventsForDisplay();
    assertEquals(1, events.size());
    assertEquals("Meeting", events.get(0).get(EventKeys.SUBJECT));
    assertEquals("Team meeting", events.get(0).get(EventKeys.DESCRIPTION));
    assertEquals("false", events.get(0).get(EventKeys.PRIVATE));
  }

  @Test
  public void testCheckForDuplicates() {
    eventManager.addEvent(event1);
    Event duplicateEvent = new Event.EventBuilder("Meeting")
            .startDateTime(LocalDateTime.parse("2025-03-21T10:30:00"))
            .endDateTime(LocalDateTime.parse("2025-03-21T11:30:00"))
            .location("Room A")
            .description("Another meeting")
            .build();
    assertTrue(eventManager.checkForDuplicates(duplicateEvent));
  }

  @Test
  public void testCheckForDuplicates2() {
    eventManager.addEvent(event1);
    Event duplicateEvent = new Event.EventBuilder("Meeting")
            .startDateTime(LocalDateTime.parse("2025-03-21T10:30:00"))
            .endDateTime(LocalDateTime.parse("2025-03-21T11:30:00"))
            .description("Another meeting")
            .build();
    assertTrue(eventManager.checkForDuplicates(duplicateEvent));
  }

  @Test
  public void testCheckForDuplicates_False() {
    assertFalse(eventManager.checkForDuplicates(null));
  }

  @Test
  public void testUpdateEvent() {
    eventManager.addEvent(event1);
    Map<String, Object> updateMap = new HashMap<>();
    updateMap.put(EventKeys.SUBJECT, "Meeting");
    updateMap.put(EventKeys.PROPERTY, "location");
    updateMap.put(EventKeys.NEW_VALUE, "Room C");

    eventManager.updateEvent(updateMap);
    List<Map<String, Object>> events = eventManager.getEventsForDisplay();
    assertEquals("Room C", events.get(0).get(EventKeys.LOCATION));
    assertNull(events.get(0).get(EventKeys.START_DATETIME));
    assertNull(events.get(0).get(EventKeys.END_DATETIME));
  }

  @Test
  public void testRepeatDateTimeEvent() {
    Map<String, Object> event3;

    event3 = new HashMap<>();
    event3.put(EventKeys.SUBJECT, "Workshop");
    event3.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-03-21T11:30:00"));
    event3.put(EventKeys.REPEAT_DATETIME, LocalDateTime.parse("2025-03-21T13:00:00"));
    event3.put(EventKeys.LOCATION, "Room B");
    event3.put(EventKeys.DESCRIPTION, "Technical workshop");
    event3.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);

    eventManager.addEvent(event3);
    List<Map<String, Object>> events = eventManager.getEventsForDisplay();
    assertEquals(1, events.size());
    assertEquals("Workshop", events.get(0).get(EventKeys.SUBJECT));
    assertEquals(LocalDateTime.parse("2025-03-21T13:00:00"), events.get(0).get(EventKeys.END_DATETIME));

  }

  @Test
  public void testRepeatDateEvent() {
    Map<String, Object> event3;

    event3 = new HashMap<>();
    event3.put(EventKeys.SUBJECT, "Workshop");
    event3.put(EventKeys.ALLDAY_DATE, LocalDate.parse("2025-03-21"));
    event3.put(EventKeys.REPEAT_DATE, LocalDate.parse("2025-03-22"));
    event3.put(EventKeys.LOCATION, "Room B");
    event3.put(EventKeys.DESCRIPTION, "Technical workshop");
    event3.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);

    eventManager.addEvent(event3);
    List<Map<String, Object>> events = eventManager.getEventsForDisplay();
    assertEquals(1, events.size());
    assertEquals("Workshop", events.get(0).get(EventKeys.SUBJECT));
    assertEquals(LocalDate.parse("2025-03-22"), events.get(0).get(EventKeys.END_DATETIME));

  }

  @Test
  public void testAllDayEvent() {
    Map<String, Object> event3;

    event3 = new HashMap<>();
    event3.put(EventKeys.SUBJECT, "Workshop");
    event3.put(EventKeys.ALLDAY_DATE, LocalDate.parse("2025-03-21"));
    event3.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
    event3.put(EventKeys.LOCATION, "Room B");
    event3.put(EventKeys.DESCRIPTION, "Technical workshop");

    eventManager.addEvent(event3);
    List<Map<String, Object>> events = eventManager.getEventsForDisplay();
    assertEquals(1, events.size());
    assertEquals("Workshop", events.get(0).get(EventKeys.SUBJECT));
    assertEquals(LocalDate.parse("2025-03-21"), events.get(0).get(EventKeys.START_DATETIME));
    assertEquals("true", events.get(0).get(EventKeys.EVENT_TYPE));

  }

  @Test
  public void testUpdateEvent_Description() {
    eventManager.addEvent(event1);
    Map<String, Object> updateMap = new HashMap<>();
    updateMap.put(EventKeys.SUBJECT, "Meeting");
    updateMap.put(EventKeys.PROPERTY, "description");
    updateMap.put(EventKeys.NEW_VALUE, "Room C");

    eventManager.updateEvent(updateMap);
    List<Map<String, Object>> events = eventManager.getEventsForDisplay();
    assertEquals("Room C", events.get(0).get(EventKeys.DESCRIPTION));
    assertEquals("false", events.get(0).get(EventKeys.EVENT_TYPE));
  }

  @Test
  public void testGetEventForDisplayBySubject() {
    eventManager.addEvent(event1);
    eventManager.addEvent(event2);
    List<Map<String, Object>> result = eventManager.getEventForDisplay("Workshop");
    assertEquals(1, result.size());
    assertEquals("Workshop", result.get(0).get(EventKeys.SUBJECT));
  }

  @Test
  public void testGetEventForDisplayByTimeRange() {
    eventManager.addEvent(event1);
    eventManager.addEvent(event2);
    List<Map<String, Object>> result = eventManager.getEventForDisplay(
            LocalDateTime.parse("2025-03-21T10:00:00"), LocalDateTime.parse("2025-03-21T12:00:00"));
    assertEquals(2, result.size());
  }

  @Test
  public void testGetEventForDisplayByTimeRange_Null() {
    eventManager.addEvent(event1);
    List<Map<String, Object>> result = eventManager.getEventForDisplay((LocalDateTime) null,(LocalDateTime) null);
    assertEquals(null, result);
  }

  @Test
  public void testGetUserStatus() {
    eventManager.addEvent(event1);
    List<Map<String, Object>> status = eventManager.getUserStatus(LocalDateTime.parse("2025-03-21T10:30:00"));
    assertEquals(1, status.size());
    assertEquals("Meeting", status.get(0).get(EventKeys.SUBJECT));
  }

  @Test
  public void testAddDuplicateEvent() {
    CalendarEvent calendar = new CalenderEventManager();
    eventManager.addEvent(event1);

    // Creating a sample event map
    Map<String, Object> eventMap = new HashMap<>();
    eventMap.put(EventKeys.SUBJECT, "Meeting2");
    eventMap.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-03-21T10:00:00"));
    eventMap.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-03-21T11:00:00"));
    eventMap.put(EventKeys.LOCATION, "Room B");
    eventMap.put(EventKeys.DESCRIPTION, "Team meeting 2");
    eventMap.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);
    // Add first event
    calendar.addEvent(eventMap);

    // Add duplicate event and expect an exception
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      calendar.addEvent(eventMap);
    });

    assertEquals("Calender shows busy!", exception.getMessage());
  }

}
