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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for CalendarEvent class. All the Events operations are verified here.
 */
public class CalendarEventTest {

  Map<String, Object> event4;
  Map<String, Object> event5;
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

    event4 = new HashMap<>();
    event4.put(EventKeys.SUBJECT, "Recurring Workshop");
    event4.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-03-25T11:30:00"));
    event4.put(EventKeys.REPEAT_DATETIME, LocalDateTime.parse("2025-03-27T13:00:00"));
    event4.put(EventKeys.LOCATION, "Room B");
    event4.put(EventKeys.DESCRIPTION, "Technical workshop");
    event4.put(EventKeys.EVENT_TYPE, EventKeys.EventType.RECURRING);

    event5 = new HashMap<>();
    event5.put(EventKeys.SUBJECT, "All-Day Workshop");
    event5.put(EventKeys.ALLDAY_DATE, LocalDate.parse("2025-03-29"));
    event5.put(EventKeys.REPEAT_DATE, LocalDate.parse("2025-03-30"));
    event5.put(EventKeys.LOCATION, "Room B");
    event5.put(EventKeys.DESCRIPTION, "Technical workshop");
    event5.put(EventKeys.EVENT_TYPE, EventKeys.EventType.RECURRING);

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
  public void testCheckForDuplicates_Recurring() {
    eventManager.addEvent(event4);
    Event duplicateEvent = new Event.EventBuilder("Workshop All Day")
            .startDateTime(LocalDateTime.parse("2025-03-25T11:30:00"))
            .repeatDateTime(LocalDateTime.parse("2025-03-27T13:00:00"))
            .allDay(true)
            .location("Room A")
            .description("Another meeting")
            .build();
    assertTrue(eventManager.checkForDuplicates(duplicateEvent));
  }

  @Test
  public void testCheckForDuplicates_endStart() {
    eventManager.addEvent(event4);
    Event duplicateEvent = new Event.EventBuilder("Workshop All Day1")
            .startDateTime(LocalDateTime.parse("2025-03-27T15:00:00"))
            .repeatDateTime(LocalDateTime.parse("2025-03-27T19:00:00"))
            .allDay(true)
            .location("Room A")
            .description("Another meeting")
            .build();
    assertFalse(eventManager.checkForDuplicates(duplicateEvent));
  }


  @Test
  public void testCheckForDuplicates_RecurringRepeat() {
    eventManager.addEvent(event4);
    Event duplicateEvent = new Event.EventBuilder("Workshop All Day")
            .startDateTime(LocalDateTime.parse("2025-03-27T11:30:00"))
            .repeatDateTime(LocalDateTime.parse("2025-03-27T13:00:00"))
            .allDay(true)
            .location("Room A")
            .description("Another meeting")
            .build();
    assertTrue(eventManager.checkForDuplicates(duplicateEvent));
  }

  @Test
  public void testCheckForDuplicates_allDay() {
    eventManager.addEvent(event4);
    Event duplicateEvent = new Event.EventBuilder("Workshop All Day")
            .allDate(LocalDate.parse("2025-03-25"))
            .repeatDate(LocalDate.parse("2025-03-27"))
            .allDay(true)
            .location("Room A")
            .description("Another meeting")
            .build();
    assertTrue(eventManager.checkForDuplicates(duplicateEvent));
  }

  @Test
  public void testCheckForDuplicates_allDay2() {
    eventManager.addEvent(event5);
    Event duplicateEvent = new Event.EventBuilder("Workshop All Day2")
            .allDate(LocalDate.parse("2025-03-29"))
            .repeatDate(LocalDate.parse("2025-03-30"))
            .allDay(true)
            .location("Room A")
            .description("Another meeting")
            .build();
    assertTrue(eventManager.checkForDuplicates(duplicateEvent));
  }

  @Test
  public void testCheckForDuplicates_allDayRepeat() {
    eventManager.addEvent(event5);
    Event duplicateEvent = new Event.EventBuilder("Workshop All Day2")
            .allDate(LocalDate.parse("2025-03-10"))
            .repeatDate(LocalDate.parse("2025-03-30"))
            .allDay(true)
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
    assertEquals(LocalDateTime.parse("2025-03-21T13:00:00"),
            events.get(0).get(EventKeys.END_DATETIME));

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
    List<Map<String, Object>> result = eventManager.getEventForDisplay("Meeting");
    assertEquals(1, result.size());
    assertEquals("Meeting", result.get(0).get(EventKeys.SUBJECT));
  }

  @Test
  public void testGetEventForDisplayFromTo() {
    eventManager.addEvent(event1);
    eventManager.addEvent(event2);
    List<Map<String, Object>> result = eventManager.getEventForDisplay(
            LocalDateTime.parse("2025-03-21T10:00:00"),
            LocalDateTime.parse("2025-03-21T13:00:00"));
    assertEquals(2, result.size());
  }

  @Test
  public void testGetEventForDisplayByTimeRange() {
    eventManager.addEvent(event1);
    eventManager.addEvent(event2);
    List<Map<String, Object>> result = eventManager.getEventForDisplay(
            LocalDateTime.parse("2025-03-21T10:00:00"),
            LocalDateTime.parse("2025-03-21T12:00:00"));
    assertEquals(1, result.size());
  }

  @Test
  public void testGetEventForDisplayByTimeRange_Null() {
    eventManager.addEvent(event1);
    List<Map<String, Object>> result = eventManager.getEventForDisplay((LocalDateTime)
            null, (LocalDateTime) null);
    assertEquals(null, result);
  }

  @Test
  public void testGetUserStatus() {
    eventManager.addEvent(event1);
    List<Map<String, Object>> status = eventManager.getUserStatus(
            LocalDateTime.parse("2025-03-21T10:30:00"));
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

  @Test
  public void testCheckForDuplicates_BoundaryStartEndTime() {
    eventManager.addEvent(event1);
    Event duplicateEvent = new Event.EventBuilder("Boundary Meeting")
            .startDateTime(LocalDateTime.parse("2025-03-21T11:00:00"))
            .endDateTime(LocalDateTime.parse("2025-03-21T12:00:00"))
            .location("Room A")
            .description("Boundary overlap")
            .build();
    assertTrue(eventManager.checkForDuplicates(duplicateEvent));
  }

  @Test
  public void testCheckForDuplicates_ExactMatch() {
    eventManager.addEvent(event1);
    Event duplicateEvent = new Event.EventBuilder("Meeting")
            .startDateTime(LocalDateTime.parse("2025-03-21T10:00:00"))
            .endDateTime(LocalDateTime.parse("2025-03-21T11:00:00"))
            .location("Room A")
            .description("Exact duplicate")
            .build();
    assertTrue(eventManager.checkForDuplicates(duplicateEvent));
  }

  @Test
  public void testCheckForDuplicates_SpanMultipleDays() {
    eventManager.addEvent(event4);
    Event spanningEvent = new Event.EventBuilder("Multi-Day Event")
            .startDateTime(LocalDateTime.parse("2025-03-24T23:00:00"))
            .repeatDateTime(LocalDateTime.parse("2025-03-28T02:00:00"))
            .location("Room C")
            .description("Spanning multiple days")
            .build();
    assertTrue(eventManager.checkForDuplicates(spanningEvent));
  }

  @Test
  public void testCheckForDuplicates_StartsBeforeEndsAfter() {
    eventManager.addEvent(event2);
    Event overlappingEvent = new Event.EventBuilder("Overlapping Event")
            .startDateTime(LocalDateTime.parse("2025-03-21T10:30:00"))
            .endDateTime(LocalDateTime.parse("2025-03-21T13:30:00"))
            .location("Room B")
            .description("Overlaps existing event")
            .build();
    assertTrue(eventManager.checkForDuplicates(overlappingEvent));
  }

  @Test
  public void testCheckForDuplicates_NoOverlap() {
    eventManager.addEvent(event1);
    Event nonOverlappingEvent = new Event.EventBuilder("No Overlap Event")
            .startDateTime(LocalDateTime.parse("2025-03-21T12:00:00"))
            .endDateTime(LocalDateTime.parse("2025-03-21T13:00:00"))
            .location("Room C")
            .description("Does not overlap")
            .build();
    assertFalse(eventManager.checkForDuplicates(nonOverlappingEvent));
  }

  @Test
  public void testGetEventForDisplay_ExactMatchOfRecurringDate() {
    eventManager.addEvent(event5);  // "Workshop All Day" from 2025-03-29 to 2025-03-30

    // Query for exact match on recurring date
    List<Map<String, Object>> result = eventManager.getEventForDisplay(
            LocalDateTime.parse("2025-03-29T00:00:00"),
            LocalDateTime.parse("2025-03-29T23:59:59"));
    assertEquals(1, result.size());  // Expecting "Workshop All Day" on 2025-03-29
  }

  @Test
  public void testEventOverlapDifferentTimeZone() {
    eventManager.addEvent(event1);
    Map<String, Object> event3 = new HashMap<>();
    event3.put(EventKeys.SUBJECT, "Workshop");
    event3.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-03-21T10:30:00"));
    event3.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-03-21T11:30:00"));
    event3.put(EventKeys.LOCATION, "Room A");
    event3.put(EventKeys.DESCRIPTION, "Technical workshop");
    event3.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);

    assertThrows(IllegalArgumentException.class, () -> eventManager.addEvent(event3));

  }

  @Test
  public void testAddEventMissingRequiredFields() {
    Map<String, Object> eventInvalid = new HashMap<>();
    eventInvalid.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-03-21T10:00:00"));
    eventInvalid.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-03-21T11:00:00"));
    eventInvalid.put(EventKeys.LOCATION, "Room A");
    eventInvalid.put(EventKeys.DESCRIPTION, "Team meeting");
    eventInvalid.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);

    assertThrows(NullPointerException.class, () -> eventManager.addEvent(eventInvalid));
  }

  @Test
  public void testRecurringEventMultipleOccurrences() {
    eventManager.addEvent(event4);

    List<Map<String, Object>> events = eventManager.getEventForDisplay(LocalDateTime.parse(
                    "2025-03-27T13:00:00"),
            LocalDateTime.parse("2025-03-27T15:00:00"));
    assertEquals(1, events.size());
  }


  @Test
  public void testAddEventWithDifferentEventTypes() {
    eventManager.addEvent(event1);
    List<Map<String, Object>> events = eventManager.getEventsForDisplay();
    assertEquals(1, events.size());

    eventManager.addEvent(event4);
    events = eventManager.getEventsForDisplay();
    assertEquals(2, events.size());

    eventManager.addEvent(event5);
    events = eventManager.getEventsForDisplay();
    assertEquals(3, events.size());
  }


  @Test
  public void testGetEventForDisplayByName() {
    eventManager.addEvent(event1);
    eventManager.addEvent(event2);

    List<Map<String, Object>> result = eventManager.getEventForDisplay("Meeting");
    assertEquals(1, result.size());
  }

  @Test
  public void testAddMultipleEventsOnSameDay() {
    eventManager.addEvent(event1);
    eventManager.addEvent(event2);

    List<Map<String, Object>> events = eventManager.getEventsForDisplay();
    assertEquals(2, events.size()); // Two events should be added on the same day
  }


  @Test
  public void testUpdateEventDescription() {
    eventManager.addEvent(event1);

    // Update event description
    Map<String, Object> updateMap = new HashMap<>();
    updateMap.put(EventKeys.PROPERTY, EventKeys.DESCRIPTION);
    updateMap.put(EventKeys.SUBJECT, "Meeting");
    updateMap.put(EventKeys.NEW_VALUE, "Updated event description");

    eventManager.updateEvent(updateMap);
    List<Map<String, Object>> events = eventManager.getEventForDisplay("Meeting");
    assertEquals("Updated event description", events.get(0).get(EventKeys.DESCRIPTION));
  }


  @Test
  public void testUpdateMultipleFieldsInEvent() {
    eventManager.addEvent(event1);

    Map<String, Object> updateMap = new HashMap<>();
    updateMap.put(EventKeys.SUBJECT, "Meeting");
    updateMap.put(EventKeys.PROPERTY, EventKeys.SUBJECT);
    updateMap.put(EventKeys.NEW_VALUE, "Multiple Fields Update");

    eventManager.updateEvent(updateMap);

    updateMap.put(EventKeys.PROPERTY, EventKeys.LOCATION);
    updateMap.put(EventKeys.NEW_VALUE, "Room B");
    updateMap.put(EventKeys.SUBJECT, "Multiple Fields Update");
    eventManager.updateEvent(updateMap);

    updateMap.put(EventKeys.PROPERTY, EventKeys.DESCRIPTION);
    updateMap.put(EventKeys.SUBJECT, "Multiple Fields Update");
    updateMap.put(EventKeys.NEW_VALUE, "Updated event description after multiple field change");
    eventManager.updateEvent(updateMap);

    List<Map<String, Object>> events = eventManager.getEventForDisplay(
            "Multiple Fields Update");
    assertEquals("Multiple Fields Update", events.get(0).get(EventKeys.SUBJECT));
    assertEquals("Room B", events.get(0).get(EventKeys.LOCATION));
    assertEquals("Updated event description after multiple field change",
            events.get(0).get(EventKeys.DESCRIPTION));
  }


}