package calendar.controller;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calendar.model.CalendarEvent;
import calendar.model.CalendarGroupManager;
import calendar.model.CalenderEventManager;
import calendar.model.EventKeys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * Test class for CalendarGUICalendar. To test intergration.
 */
public class CalendarGUICalendarTest {

  private CalendarGUICalendar controller;
  private MockCalendarGroup group;
  private MockCalendarModel model;

  @Before
  public void setUp() {
    controller = new CalendarGUICalendar();
    group = new MockCalendarGroup();
    model = new MockCalendarModel();

    CalendarFactory.setGroup(group);
    CalendarFactory.setModel(model);
  }

  @Test
  public void testAddCalendar() {
    controller.addCalendar("Work", "America/New_York");
    assertEquals("Work", group.addedCalendarName);
    assertEquals("America/New_York", group.addedTimeZone);
  }

  @Test
  public void testEditCalendar() {
    Map<String, String> props = new HashMap<>();
    props.put(EventKeys.CALENDAR_NAME, "Personal");
    props.put(EventKeys.PROPERTY, "color");
    props.put(EventKeys.NEW_VALUE, "blue");

    controller.editCalendar(props);
    assertEquals("Personal", group.updatedDetails.get(EventKeys.CALENDAR_NAME));
  }

  @Test
  public void testAddSingleEvent() {
    Map<String, String> props = new HashMap<>();
    props.put(EventKeys.SUBJECT, "Meeting");
    props.put(EventKeys.LOCATION, "Zoom");
    props.put(EventKeys.DESCRIPTION, "Team sync");
    props.put(EventKeys.EVENT_TYPE, "Single AllDay");
    props.put(EventKeys.START_DATETIME, "2025-04-10T10:00");

    controller.addEvent(props);
    assertEquals("Meeting", model.lastAddedEvent.get(EventKeys.SUBJECT));
    assertEquals(EventKeys.EventType.ALL_DAY, model.lastAddedEvent.get(EventKeys.EVENT_TYPE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddEventThrowsException() {
    model.shouldThrow = true;
    controller.addEvent(new HashMap<>());
  }

  @Test
  public void testEditEventSubject() {
    Map<String, String> props = new HashMap<>();
    props.put(EventKeys.SUBJECT, "Old Title");
    props.put(EventKeys.NEW_VALUE, "New Title");

    controller.editEvent(props);
    assertEquals("New Title", model.updatedEvent.get(EventKeys.NEW_VALUE));
  }

  @Test
  public void testEditEventWithLocation() {
    Map<String, String> props = new HashMap<>();
    props.put(EventKeys.SUBJECT, "Meeting");
    props.put(EventKeys.LOCATION, "Room 101");

    controller.editEvent(props);
    assertEquals(EventKeys.DESCRIPTION, model.updatedEvent.get(EventKeys.PROPERTY));
    assertEquals("Room 101", model.updatedEvent.get(EventKeys.NEW_VALUE));
  }

  @Test
  public void testEditEventWithDescription() {
    Map<String, String> props = new HashMap<>();
    props.put(EventKeys.SUBJECT, "Team Sync");
    props.put(EventKeys.DESCRIPTION, "Weekly sync-up");

    controller.editEvent(props);
    assertEquals(EventKeys.DESCRIPTION, model.updatedEvent.get(EventKeys.PROPERTY));
    assertEquals("Weekly sync-up", model.updatedEvent.get(EventKeys.NEW_VALUE));
  }

  @Test
  public void testEditEventWithStartAndEndDateTime() {
    Map<String, String> props = new HashMap<>();
    props.put(EventKeys.SUBJECT, "Workshop");
    props.put(EventKeys.START_DATETIME, "2025-04-10T10:00");
    props.put(EventKeys.END_DATETIME, "2025-04-10T12:00");

    controller.editEvent(props);

    assertNull("UpdateEvent shouldn't be called just with start/end datetime", model.updatedEvent);
  }

  @Test
  public void testEditEventWithNewSubject() {
    Map<String, String> props = new HashMap<>();
    props.put(EventKeys.SUBJECT, "OldTitle");
    props.put(EventKeys.NEW_VALUE, "NewTitle");

    controller.editEvent(props);
    assertEquals(EventKeys.SUBJECT, model.updatedEvent.get(EventKeys.PROPERTY));
    assertEquals("NewTitle", model.updatedEvent.get(EventKeys.NEW_VALUE));
  }


  @Test
  public void testSetCalendar() {
    CalendarEvent mockEvent = new CalenderEventManager();
    group.calendarEvent = mockEvent;

    controller.setCalendar("Work");
    assertEquals(mockEvent, CalendarFactory.getModel());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImportCalendarFailure() {
    model.shouldThrow = true;
    controller.importCalendar(new HashMap<>());
  }

  @Test
  public void testImportCalendarSuccess() {
    Map<String, Object> props = new HashMap<>();
    props.put(EventKeys.SUBJECT, "Imported Event");

    controller.importCalendar(props);
    assertEquals("Imported Event", model.lastAddedEvent.get(EventKeys.SUBJECT));
  }

  // ======== Mock Classes ========

  private static class MockCalendarGroup extends CalendarGroupManager {
    String addedCalendarName;
    String addedTimeZone;
    Map<String, Object> updatedDetails;
    CalendarEvent calendarEvent;

    @Override
    public void addCalendar(String name, String timeZone) {
      this.addedCalendarName = name;
      this.addedTimeZone = timeZone;
    }

    @Override
    public void updateCalendar(Map<String, Object> details) {
      this.updatedDetails = details;
    }

    @Override
    public CalendarEvent getCalendarEvent(String name) {
      return calendarEvent;
    }
  }

  private static class MockCalendarModel extends CalenderEventManager {
    boolean shouldThrow = false;
    Map<String, Object> lastAddedEvent;
    Map<String, Object> updatedEvent;

    @Override
    public void addEvent(Map<String, Object> eventDetails) {
      if (shouldThrow) {
        throw new IllegalArgumentException("Mock exception");
      }
      this.lastAddedEvent = eventDetails;
    }

    @Override
    public void updateEvent(Map<String, Object> props) {
      this.updatedEvent = props;
    }

    @Override
    public List<Map<String, Object>> getEventsForDisplay() {
      List<Map<String, Object>> data = new ArrayList<>();
      Map<String, Object> details = new HashMap<>();
      details.put("event", "Mock Event");
      data.add(details);
      return data;
    }
  }
}
