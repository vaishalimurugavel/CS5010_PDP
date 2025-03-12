import org.junit.Before;
import org.junit.Test;

import calendar.controller.CalendarControllerImpl;
import calendar.controller.CalendarFactory;
import calendar.model.*;
import java.lang.reflect.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;


public class CalendarControllerManualTest {
  private ManualMockEvent singleEvent;
  private ManualMockEvent recurringEvent;
  private ManualMockView mockView;
  private CalendarControllerImpl controller;

  static class ManualMockEvent extends CalendarEvent {
    public List<Map<String, Object>> events = new ArrayList<>();
    public boolean addCalled = false;

    @Override
    public void addEvent(Map<String, Object> details) {
      addCalled = true;
      events.add(new HashMap<>(details));
    }
  }

  static class ManualMockView {
    public LocalDate lastPrintedDate;
    public String exportedFile;

    public void printEvents(LocalDate date) {
      lastPrintedDate = date;
    }

    public void exportCalendar(String filename) {
      exportedFile = filename;
    }
  }

  @Before
  public void injectMocks() throws Exception {
    singleEvent = new ManualMockEvent();
    recurringEvent = new ManualMockEvent();
    mockView = new ManualMockView();

    // Inject through reflection
    setFinalStatic(CalendarFactory.class.getDeclaredField("singleCalender"), singleEvent);
    setFinalStatic(CalendarFactory.class.getDeclaredField("recurringCalender"), recurringEvent);
    setFinalStatic(CalendarFactory.class.getDeclaredField("calendarView"), mockView);

    controller = (CalendarControllerImpl) CalendarFactory.getCalendarController();
  }

  private void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiers = Field.class.getDeclaredField("modifiers");
    modifiers.setAccessible(true);
    modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }

  // Test for checkValidDate edge cases
  @Test
  public void testInvalidDateScenarios() {
    // Test invalid month (13th month)
    String invalidCmd = "create event Error from 2025-13-01 10:00 to 2025-13-01 11:00";
    assertThrows(DateTimeParseException.class, () -> controller.createEvent(invalidCmd));

    // Test end before start
    String reverseDateCmd = "create event Conflict from 2025-03-20 12:00 to 2025-03-20 11:00";
    IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
            () -> controller.createEvent(reverseDateCmd));
    assertEquals("Invalid date format.", iae.getMessage());
  }

  // Test conditional paths in command parsing
  @Test
  public void testAllConditionalBranches() {
    // Test autoDecline flag
    String autoDeclineCmd = "create event --autoDecline Meeting from 2025-03-15 10:00 to 2025-03-15 11:00";
    controller.processCommand(autoDeclineCmd);
    assertTrue(singleEvent.events.get(0).get(EventKeys.AUTO_DECLINE).equals(true));

    // Test private event flag
    String privateCmd = "create event Secret on 2025-03-15 private";
    controller.processCommand(privateCmd);
    assertEquals(1, singleEvent.events.get(0).get(EventKeys.PRIVATE));

    // Test optional description
    String descCmd = "create event WithDesc on 2025-03-15 description Important meeting";
    controller.processCommand(descCmd);
    assertEquals("Important meeting", singleEvent.events.get(0).get(EventKeys.DESCRIPTION));

    // Test missing required fields
    String invalidCmd = "create event MissingFields";
    assertThrows(IllegalArgumentException.class, () -> controller.processCommand(invalidCmd));
  }

  // Test date validation paths
  @Test
  public void testDateValidationPaths() {
    // Valid date range
    String validCmd = "create event Valid from 2025-03-15 10:00 to 2025-03-15 11:00";
     controller.processCommand(validCmd);

    // Equal start/end times
    String equalTimeCmd = "create event Equal from 2025-03-15 10:00 to 2025-03-15 10:00";
    controller.processCommand(equalTimeCmd);

    // Invalid date pattern
    String badFormatCmd = "create event BadFormat from 2025-03-15 to 2025-03-16";
    assertThrows(IllegalArgumentException.class, () -> controller.processCommand(badFormatCmd));
  }

  // Test view interactions
  @Test
  public void testViewInteractions() {
    String printCmd = "print events 2025-03-15";
    assertThrows(UnsupportedOperationException.class,
            () -> controller.processCommand(printCmd));

    String exportCmd = "export calendar to test.ics";
    assertThrows(UnsupportedOperationException.class,
            () -> controller.processCommand(exportCmd));
  }
}
