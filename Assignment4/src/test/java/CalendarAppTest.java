import calendar.model.CalendarEvent;
import calendar.model.Event;
import calendar.controller.CalendarFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarAppTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
    // Clear the event list after each test
    List<Event> events = CalendarEvent.getEventList();
    events.clear();
  }

  @Test
  public void testCreateSingleEvent() {
    // Test creating a single event
    String command = "create event Meeting from 2025-03-15 14:00 to 2025-03-15 15:00 location Conference Room description Weekly sync";

    CalendarFactory.getCalendarController().processCommand(command);

    List<Event> events = CalendarEvent.getEventList();
    assertEquals(1, events.size());

    Event event = events.get(0);
    assertEquals("Meeting", event.getSubject());
    assertEquals("Conference Room", event.getLocation());
    assertEquals("Weekly sync", event.getDescription());
    assertEquals(LocalDateTime.parse("2025-03-15 14:00", DATE_TIME_FORMATTER), event.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-03-15 15:00", DATE_TIME_FORMATTER), event.getEndDateTime());
    assertEquals(0, event.getEventType()); // Not private
    assertFalse(event.isAllDay());
  }

  @Test
  public void testCreateAllDayEvent() {
    // Test creating an all-day event
    String command = "create event Annual Review on 2025-04-01 description Performance review private";

    CalendarFactory.getCalendarController().processCommand(command);

    List<Event> events = CalendarEvent.getEventList();
    assertEquals(1, events.size());

    Event event = events.get(0);
    assertEquals("Annual Review", event.getSubject());
    assertEquals("Online", event.getLocation()); // Default location
    assertEquals("Performance review", event.getDescription());
    assertEquals(LocalDate.parse("2025-04-01"), event.getAllDate());
    assertEquals(1, event.getEventType()); // Private
    assertTrue(event.isAllDay());
  }

  @Test
  public void testCreateRecurringEvent() {
    // Test creating a recurring event
    String command = "create event Team Standup from 2025-03-15 09:00 to 2025-03-15 09:30 repeats MWF for 10 times";

    CalendarFactory.getCalendarController().processCommand(command);

    List<Event> events = CalendarEvent.getEventList();
    assertEquals(1, events.size());

    Event event = events.get(0);
    assertEquals("Team Standup", event.getSubject());
    assertEquals(LocalDateTime.parse("2025-03-15 09:00", DATE_TIME_FORMATTER), event.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-03-15 09:30", DATE_TIME_FORMATTER), event.getEndDateTime());
    assertEquals("MWF", event.getWeekdays());
    assertEquals(10, event.getOccurrences());
  }

  @Test
  public void testCreateRecurringEventUntil() {
    // Test creating a recurring event with an end date
    String command = "create event Weekly 1:1 from 2025-03-15 15:00 to 2025-03-15 16:00 repeats T until 2025-04-15 16:00";

    CalendarFactory.getCalendarController().processCommand(command);

    List<Event> events = CalendarEvent.getEventList();
    assertEquals(1, events.size());

    Event event = events.get(0);
    assertEquals("Weekly 1:1", event.getSubject());
    assertEquals(LocalDateTime.parse("2025-03-15 15:00", DATE_TIME_FORMATTER), event.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-03-15 16:00", DATE_TIME_FORMATTER), event.getEndDateTime());
    assertEquals("T", event.getWeekdays());
    assertEquals(LocalDateTime.parse("2025-04-15 16:00", DATE_TIME_FORMATTER), event.getRepeatDateTime());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventWithInvalidTimeRange() {
    // End time before start time
    String command = "create event Invalid Meeting from 2025-03-15 15:00 to 2025-03-15 14:00";
    CalendarFactory.getCalendarController().processCommand(command);
  }

  @Test
  public void testEditEventSubject() {
    // First create an event
    String createCommand = "create event Old Subject from 2025-03-15 14:00 to 2025-03-15 15:00";
    CalendarFactory.getCalendarController().processCommand(createCommand);

    // Edit the event's subject
    String editCommand = "edit event subject Old Subject from 2025-03-15 14:00 to 2025-03-15 15:00 with New Subject";
    CalendarFactory.getCalendarController().processCommand(editCommand);

    List<Event> events = CalendarEvent.getEventList();
    assertEquals(1, events.size());
    assertEquals("New Subject", events.get(0).getSubject());
  }

  @Test
  public void testEditEventLocation() {
    // First create an event
    String createCommand = "create event Meeting from 2025-03-15 14:00 to 2025-03-15 15:00";
    CalendarFactory.getCalendarController().processCommand(createCommand);

    // Edit the event's location
    String editCommand = "edit event location Meeting from 2025-03-15 14:00 to 2025-03-15 15:00 with Conference Room A";
    CalendarFactory.getCalendarController().processCommand(editCommand);

    List<Event> events = CalendarEvent.getEventList();
    assertEquals(1, events.size());
    assertEquals("Conference Room A", events.get(0).getLocation());
  }

  @Test
  public void testPrintEventsOn() {
    // Create two events on different days
    String command1 = "create event Event 1 from 2025-03-15 14:00 to 2025-03-15 15:00";
    String command2 = "create event Event 2 from 2025-03-16 10:00 to 2025-03-16 11:00";
    CalendarFactory.getCalendarController().processCommand(command1);
    CalendarFactory.getCalendarController().processCommand(command2);

    outContent.reset(); // Clear previous output

    // Print events on a specific day
    String printCommand = "print events on 2025-03-15";
    CalendarFactory.getCalendarController().processCommand(printCommand);

    String output = outContent.toString();
    assertTrue(output.contains("Event 1"));
    assertFalse(output.contains("Event 2"));
  }

  @Test
  public void testPrintEventsInTimeRange() {
    // Create events
    String command1 = "create event Early Event from 2025-03-15 09:00 to 2025-03-15 10:00";
    String command2 = "create event Mid Event from 2025-03-15 12:00 to 2025-03-15 13:00";
    String command3 = "create event Late Event from 2025-03-15 16:00 to 2025-03-15 17:00";

    CalendarFactory.getCalendarController().processCommand(command1);
    CalendarFactory.getCalendarController().processCommand(command2);
    CalendarFactory.getCalendarController().processCommand(command3);

    outContent.reset(); // Clear previous output

    // Print events in a specific time range
    String printCommand = "print events from 2025-03-15 11:00 to 2025-03-15 14:00";
    CalendarFactory.getCalendarController().processCommand(printCommand);

    String output = outContent.toString();
    assertFalse(output.contains("Early Event"));
    assertTrue(output.contains("Mid Event"));
    assertFalse(output.contains("Late Event"));
  }

  @Test
  public void testShowStatusBusy() {
    // Create an event
    String command = "create event Meeting from 2025-03-15 14:00 to 2025-03-15 15:00";
    CalendarFactory.getCalendarController().processCommand(command);

    outContent.reset(); // Clear previous output

    // Check status during the event
    String statusCommand = "show status on 2025-03-15 14:30";
    CalendarFactory.getCalendarController().processCommand(statusCommand);

    String output = outContent.toString();
    assertTrue(output.contains("Status: Busy"));
  }

  @Test
  public void testShowStatusAvailable() {
    // Create an event
    String command = "create event Meeting from 2025-03-15 14:00 to 2025-03-15 15:00";
    CalendarFactory.getCalendarController().processCommand(command);

    outContent.reset(); // Clear previous output

    // Check status outside the event time
    String statusCommand = "show status on 2025-03-15 16:00";
    CalendarFactory.getCalendarController().processCommand(statusCommand);

    String output = outContent.toString();
    assertTrue(output.contains("Status: Available"));
  }

  @Test
  public void testExportCalendar() {
    // Create an event
    String createCommand = "create event Export Test from 2025-03-15 14:00 to 2025-03-15 15:00";
    CalendarFactory.getCalendarController().processCommand(createCommand);

    // Export to a temporary file
    String exportFile = "test_export.csv";
    String exportCommand = "export " + exportFile;
    CalendarFactory.getCalendarController().processCommand(exportCommand);

    // Verify the file was created
    File file = new File(exportFile);
    assertTrue(file.exists());

    // Clean up
    file.delete();
  }

  @Test
  public void testAutoDeclineFeature() {
    // Create an event
    String command1 = "create event --autoDecline Meeting 1 from 2025-03-15 14:00 to 2025-03-15 15:00";
    CalendarFactory.getCalendarController().processCommand(command1);

    // Try to create an overlapping event with autoDecline
    String command2 = "create event --autoDecline Meeting 2 from 2025-03-15 14:30 to 2025-03-15 15:30";

    try {
      CalendarFactory.getCalendarController().processCommand(command2);
      fail("Expected IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Calender shows busy!", e.getMessage());
    }

    // Verify only one event exists
    List<Event> events = CalendarEvent.getEventList();
    assertEquals(1, events.size());
  }

  @Test
  public void testCreateAllDayRecurringEvent() {
    String command = "create event Daily Standup on 2025-03-15 repeats MTWRF for 5 times";

    CalendarFactory.getCalendarController().processCommand(command);

    List<Event> events = CalendarEvent.getEventList();
    assertEquals(1, events.size());

    Event event = events.get(0);
    assertEquals("Daily Standup", event.getSubject());
    assertEquals(LocalDate.parse("2025-03-15"), event.getAllDate());
    assertEquals("MTWRF", event.getWeekdays());
    assertEquals(5, event.getOccurrences());
    assertTrue(event.isAllDay());
  }

  // Helper method to access Event.weekdays - not available in original code but needed for tests
  private String getRepeatDays(Event event) {
    try {
      java.lang.reflect.Field field = Event.class.getDeclaredField("weekdays");
      field.setAccessible(true);
      return (String) field.get(event);
    } catch (Exception e) {
      return null;
    }
  }

  // Helper method to access Event.occurrences - not available in original code but needed for tests
  private int getOccurrences(Event event) {
    try {
      java.lang.reflect.Field field = Event.class.getDeclaredField("occurrences");
      field.setAccessible(true);
      return (int) field.get(event);
    } catch (Exception e) {
      return 0;
    }
  }
}