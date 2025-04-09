package calendar.model;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for the CalendarGroupManager class. Verifies all the Calendar related operations.
 */
public class CalendarGroupManagerTest {

  @Test
  public void testAddCalendar() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();

    calendarGroupManager.addCalendar("Group11", "America/New_York");

    Calendars group = calendarGroupManager.getCalendar("Group11");
    assertNotNull("Calendar should be added", group);
    assertEquals("America/New_York", group.getZoneName());
  }

  @Test
  public void testAddDuplicateCalendar() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();

    calendarGroupManager.addCalendar("Group19", "America/New_York");

    assertThrows(IllegalArgumentException.class,
            () -> calendarGroupManager.addCalendar("Group19", "America/Los_Angeles"));

  }

  @Test
  public void testGetCalendarEvent() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();

    calendarGroupManager.addCalendar("Group13", "America/New_York");

    CalendarEvent event = calendarGroupManager.getCalendarEvent("Group13");
    assertNotNull("Calendar event should be returned", event);
  }

  @Test
  public void testGetNonExistingCalendarEvent() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();
    try {
      calendarGroupManager.getCalendarEvent("NonExistingGroup");
      fail("Expected IllegalArgumentException for non-existing group");
    } catch (IllegalArgumentException e) {
      assertEquals("No Calendar available with the name NonExistingGroup", e.getMessage());
    }
  }

  @Test
  public void testSetCurrentCalendar() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();

    calendarGroupManager.addCalendar("Group18", "America/New_York");

    Calendars currentCalendar = calendarGroupManager.setCurrentCalendar("Group18");

    assertNotNull("Current calendar should be set", currentCalendar);
    assertEquals("Group18", currentCalendar.getTitle());
  }

  @Test
  public void testGetCurrentCalendar() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();

    calendarGroupManager.addCalendar("Group14", "America/New_York");
    calendarGroupManager.setCurrentCalendar("Group14");

    Calendars currentCalendar = calendarGroupManager.getCurrentCalendar();
    assertNotNull("Current calendar should not be null", currentCalendar);
    assertEquals("Group14", currentCalendar.getTitle());
  }

  @Test
  public void testGetCurrentCalendarWithoutSetting() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();

    Calendars currentCalendar = calendarGroupManager.getCurrentCalendar();
    assertNull("Current calendar should be null", currentCalendar);
  }

  @Test
  public void testCheckForDuplicates() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();

    calendarGroupManager.addCalendar("Group1", "America/New_York");

    assertTrue("Calendar group should exist",
            calendarGroupManager.checkForDuplicates("Group1"));
    assertFalse("Calendar group should not exist",
            calendarGroupManager.checkForDuplicates("NonExistingGroup"));
  }

  @Test
  public void testUpdateCalendar() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();

    calendarGroupManager.addCalendar("Group12", "America/New_York");

    Map<String, Object> updateProps = new HashMap<>();
    updateProps.put(EventKeys.CALENDAR_NAME, "Group12");
    updateProps.put(EventKeys.PROPERTY, EventKeys.CALENDAR_NAME);
    updateProps.put(EventKeys.NEW_VALUE, "UpdatedGroup");

    calendarGroupManager.updateCalendar(updateProps);

    Calendars updatedGroup = calendarGroupManager.getCalendar("UpdatedGroup");
    assertNotNull("Updated calendar should exist", updatedGroup);
    assertEquals("UpdatedGroup", updatedGroup.getTitle());
  }

  @Test
  public void testUpdateCalendarTimezone() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();

    calendarGroupManager.addCalendar("Group16", "America/New_York");

    Map<String, Object> updateProps = new HashMap<>();
    updateProps.put(EventKeys.CALENDAR_NAME, "Group16");
    updateProps.put(EventKeys.PROPERTY, EventKeys.TIMEZONE);
    updateProps.put(EventKeys.NEW_VALUE, "America/Los_Angeles");
    calendarGroupManager.updateCalendar(updateProps);

    Calendars updatedGroup = calendarGroupManager.getCalendar("Group16");
    assertNotNull("Updated calendar should exist", updatedGroup);
    assertEquals("America/Los_Angeles", updatedGroup.getZoneName());
  }

  @Test
  public void testUpdateNonExistingCalendar() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();

    Map<String, Object> updateProps = new HashMap<>();
    updateProps.put(EventKeys.CALENDAR_NAME, "NonExistingGroup");
    updateProps.put(EventKeys.PROPERTY, EventKeys.CALENDAR_NAME);
    updateProps.put(EventKeys.NEW_VALUE, "UpdatedGroup");

    try {
      calendarGroupManager.updateCalendar(updateProps);
      fail("Expected IllegalArgumentException for non-existing group");
    } catch (IllegalArgumentException e) {
      assertEquals("No Calendar available with the name NonExistingGroup", e.getMessage());
    }
  }

  @Test
  public void testRecurringEventConflict() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();


    calendarGroupManager.addCalendar("TestCalendar", "America/New_York");


    Map<String, Object> existingEvent = new HashMap<>();
    existingEvent.put(EventKeys.SUBJECT, "Existing Meeting");
    existingEvent.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-04-10T10:00:00"));
    existingEvent.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-04-10T11:00:00"));
    existingEvent.put(EventKeys.DESCRIPTION, "Team meeting");
    existingEvent.put(EventKeys.LOCATION, "Conference Room");
    existingEvent.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);

    calendarGroupManager.getCalendarEvent("TestCalendar").addEvent(existingEvent);


    Map<String, Object> recurringEvent = new HashMap<>();
    recurringEvent.put(EventKeys.SUBJECT, "Recurring Workshop");
    recurringEvent.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-04-10T10:30:00"));
    recurringEvent.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-04-10T12:00:00"));
    recurringEvent.put(EventKeys.DESCRIPTION, "Technical workshop");
    recurringEvent.put(EventKeys.LOCATION, "Room B");
    recurringEvent.put(EventKeys.EVENT_TYPE, EventKeys.EventType.RECURRING);
    recurringEvent.put(EventKeys.WEEKDAYS, "MTWRF");
    recurringEvent.put(EventKeys.OCCURRENCES, 5);


    assertThrows(IllegalArgumentException.class, () -> {
      calendarGroupManager.getCalendarEvent("TestCalendar").addEvent(recurringEvent);
    });
  }
  @Test
  public void testAutoDeclineFlagBehavior() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();


    calendarGroupManager.addCalendar("TestCalendar", "America/New_York");


    Map<String, Object> existingEvent = new HashMap<>();
    existingEvent.put(EventKeys.SUBJECT, "Existing Meeting");
    existingEvent.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-04-10T10:00:00"));
    existingEvent.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-04-10T11:00:00"));
    existingEvent.put(EventKeys.DESCRIPTION, "Team meeting");
    existingEvent.put(EventKeys.LOCATION, "Conference Room");
    existingEvent.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);
    calendarGroupManager.getCalendarEvent("TestCalendar").addEvent(existingEvent);


    Map<String, Object> conflictingEvent = new HashMap<>();
    conflictingEvent.put(EventKeys.SUBJECT, "Conflicting Meeting");
    conflictingEvent.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-04-10T10:30:00"));
    conflictingEvent.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-04-10T12:00:00"));
    conflictingEvent.put(EventKeys.DESCRIPTION, "Another meeting");
    conflictingEvent.put(EventKeys.LOCATION, "Room B");
    conflictingEvent.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);
    conflictingEvent.put(EventKeys.AUTO_DECLINE, true);


    assertThrows(IllegalArgumentException.class, () -> {
      calendarGroupManager.getCalendarEvent("TestCalendar").addEvent(conflictingEvent);
    });

    Map<String, Object> nonConflictingEvent = new HashMap<>();
    nonConflictingEvent.put(EventKeys.SUBJECT, "Non-conflicting Meeting");
    nonConflictingEvent.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-04-10T12:30:00"));
    nonConflictingEvent.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-04-10T13:30:00"));
    nonConflictingEvent.put(EventKeys.DESCRIPTION, "Team discussion");
    nonConflictingEvent.put(EventKeys.LOCATION, "Room C");
    nonConflictingEvent.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);
    nonConflictingEvent.put(EventKeys.AUTO_DECLINE, true);
    calendarGroupManager.getCalendarEvent("TestCalendar").addEvent(nonConflictingEvent);


    List<Map<String, Object>> events = calendarGroupManager.getCalendarEvent("TestCalendar").getEventsForDisplay();
    assertEquals(2, events.size()); // Existing event + non-conflicting event
  }
  @Test
  public void testCopyEventsWithinRangeBetweenCalendarsWithDifferentTimezones() {
    CalendarGroupManager calendarGroupManager = new CalendarGroupManager();


    calendarGroupManager.addCalendar("SourceCalendar", "America/New_York");
    calendarGroupManager.addCalendar("TargetCalendar", "America/Los_Angeles");

    Map<String, Object> event1 = new HashMap<>();
    event1.put(EventKeys.SUBJECT, "Morning Meeting");
    event1.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-04-10T09:00:00")); // 9:00 AM in New York
    event1.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-04-10T10:00:00"));   // 10:00 AM in New York
    event1.put(EventKeys.DESCRIPTION, "Morning team meeting");
    event1.put(EventKeys.LOCATION, "Conference Room A");
    event1.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);

    Map<String, Object> event2 = new HashMap<>();
    event2.put(EventKeys.SUBJECT, "Lunch Break");
    event2.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-04-10T12:00:00")); // 12:00 PM in New York
    event2.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-04-10T13:00:00"));   // 1:00 PM in New York
    event2.put(EventKeys.DESCRIPTION, "Lunch break");
    event2.put(EventKeys.LOCATION, "Cafeteria");
    event2.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);

    calendarGroupManager.getCalendarEvent("SourceCalendar").addEvent(event1);
    calendarGroupManager.getCalendarEvent("SourceCalendar").addEvent(event2);


    calendarGroupManager.copyEventsInRange("SourceCalendar", LocalDate.parse("2025-04-10"), LocalDate.parse("2025-04-11"), "TargetCalendar");


    List<Map<String, Object>> copiedEvents = calendarGroupManager.getCalendarEvent("TargetCalendar").getEventsForDisplay();


    assertEquals(2, copiedEvents.size()); // Two events should be copied

    Map<String, Object> copiedEvent1 = copiedEvents.get(0);
    assertEquals("Morning Meeting", copiedEvent1.get(EventKeys.SUBJECT));
    assertEquals(LocalDateTime.parse("2025-04-10T06:00:00"), copiedEvent1.get(EventKeys.START_DATETIME)); // 6:00 AM in Los Angeles
    assertEquals(LocalDateTime.parse("2025-04-10T07:00:00"), copiedEvent1.get(EventKeys.END_DATETIME));   // 7:00 AM in Los Angeles
    assertEquals("Morning team meeting", copiedEvent1.get(EventKeys.DESCRIPTION));

    Map<String, Object> copiedEvent2 = copiedEvents.get(1);
    assertEquals("Lunch Break", copiedEvent2.get(EventKeys.SUBJECT));
    assertEquals(LocalDateTime.parse("2025-04-10T09:00:00"), copiedEvent2.get(EventKeys.START_DATETIME)); // 9:00 AM in Los Angeles
    assertEquals(LocalDateTime.parse("2025-04-10T10:00:00"), copiedEvent2.get(EventKeys.END_DATETIME));   // 10:00 AM in Los Angeles
    assertEquals("Lunch break", copiedEvent2.get(EventKeys.DESCRIPTION));
  }

}