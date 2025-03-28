package calendar.controller;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import calendar.model.CalenderEventManager;
import calendar.model.EventKeys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * <p>
 *  Test class for CalendarCreateCommand. Tests if the create event command is processed
 *  as intended.
 *  </p>
 */
public class ControllerCreateCommandTest {

  private ControllerCreateCommand controllerCreateCommand;

  @Before
  public void setUp() {
    controllerCreateCommand = new ControllerCreateCommand();
    CalendarFactory.setModel(new CalenderEventManager());
  }

  @Test
  public void testSingleEventCreation() {
    String command = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 location " +
            "room1 description meeting public";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("false", eventDetails.get(EventKeys.EVENT_TYPE));

    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertEquals("meeting", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("false", eventDetails.get(EventKeys.PRIVATE));
  }

  @Test
  public void testAllDayEventCreation() {
    String command = "create event subject on 2025-03-25 location room1" +
            " description meeting public";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("true", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertEquals("meeting", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("false", eventDetails.get(EventKeys.PRIVATE));
  }

  @Test
  public void testsingleAllDayEventCreation() {
    String command = "create event subject on 2025-03-25T10:00 location room1" +
            " description meeting private";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("true", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertEquals("meeting", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("true", eventDetails.get(EventKeys.PRIVATE));
  }


  @Test
  public void testrecurringAllDayEventCreation() {
    String command = "create event subject from 2025-03-25T10:00 to 2025-03-25T14:00 repeats " +
            "MTWRF until 2025-03-26T13:00 location room1 description meeting private";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("false", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertEquals("meeting", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("true", eventDetails.get(EventKeys.PRIVATE));
  }


  @Test
  public void testRecurringEventCreation() {
    String command = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 repeats " +
            "MTWRFSU for 5 times location room1 description meeting public";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("false", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertEquals("meeting", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("false", eventDetails.get(EventKeys.PRIVATE));
  }

  @Test
  public void testInvalidDateFormat() {
    String command = "create event subject from 2025-03-25T10:00 to 2025-03-24T12:00 location" +
            " room1 description meeting public";

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      controllerCreateCommand.execute(command);
    });

    assertEquals("Invalid date format.", thrown.getMessage());
  }

  @Test
  public void testAllDayRecurringEventCreation() {
    String command = "create event subject on 2025-03-25 repeats MTWRFSU for 5 times location" +
            " room1 description meeting public";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("true", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertEquals("meeting", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("false", eventDetails.get(EventKeys.PRIVATE));
  }

  @Test
  public void testInvalidCreateCommand() {
    String command = "create event subject on 2025-03-25 invalidCommand";

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      controllerCreateCommand.execute(command);
    });

    assertEquals("Invalid create event syntax.", thrown.getMessage());
  }

  @Test
  public void testAllDayEventWithNoLocation() {
    CalendarFactory.setModel(new CalenderEventManager());
    String command = "create event subject on 2025-03-25 description meeting public";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("true", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertNull(eventDetails.get(EventKeys.LOCATION));
    assertEquals("meeting", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("false", eventDetails.get(EventKeys.PRIVATE));
  }

  @Test
  public void testEventWithoutDescription() {
    String command = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00" +
            " location room1 public";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("false", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertNull(eventDetails.get(EventKeys.DESCRIPTION));
  }

  @Test
  public void testEventWithPrivateFlag() {
    String command = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 location " +
            "room1 description meeting private";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("false", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertEquals("meeting", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("true", eventDetails.get(EventKeys.PRIVATE));
  }

  @Test
  public void testEmptyCommand() {
    String command = "";

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      controllerCreateCommand.execute(command);
    });

    assertEquals("Invalid create event syntax.", thrown.getMessage());
  }

  @Test
  public void testMissingSubject() {
    String command = "create event from 2025-03-25T10:00 to 2025-03-25T12:00 location room1" +
            " description meeting public";

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      controllerCreateCommand.execute(command);
    });

    assertEquals("Invalid create event syntax.", thrown.getMessage());
  }

  @Test
  public void testOverlappingEvents() {
    String command1 = "create event Meeting1 from 2025-03-25T10:00 to 2025-03-25T12:00 " +
            "location room1 public";
    String command2 = "create event Meeting2 from 2025-03-25T11:00 to 2025-03-25T13:00 " +
            "location room1 public";

    controllerCreateCommand.execute(command1);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      controllerCreateCommand.execute(command2);
    });

    assertEquals("Calender shows busy!", thrown.getMessage());
  }

  @Test
  public void testInvalidRecurrencePattern() {
    String command = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 repeats XYZ" +
            " for 5 times location room1 description meeting public";

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      controllerCreateCommand.execute(command);
    });

    assertEquals("Invalid create event syntax.", thrown.getMessage());
  }

  @Test
  public void testMissingStartTime() {
    String command = "create event subject to 2025-03-25T12:00 location room1 " +
            "description meeting public";

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      controllerCreateCommand.execute(command);
    });

    assertEquals("Invalid create event syntax.", thrown.getMessage());
  }

  @Test
  public void testVeryLongSubject() {
    String longSubject = "a".repeat(300);
    String command = "create event " + longSubject + " from 2025-03-25T10:00 to 2025-03-25T12:00" +
            " location room1 description meeting public";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals(longSubject, eventDetails.get(EventKeys.SUBJECT));
  }

  @Test
  public void testEventWithOnlyTime() {
    String command = "create event subject from 10:00 to 12:00 location room1 description" +
            " meeting public";

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      controllerCreateCommand.execute(command);
    });

    assertEquals("Invalid create event syntax.", thrown.getMessage());
  }

  @Test
  public void testAllDayEventWithPrivateFlag() {
    String command = "create event meeting on 2025-04-15 description discussion private";

    controllerCreateCommand.execute(command);
    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);
    assertEquals("true", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("meeting", eventDetails.get(EventKeys.SUBJECT));
    assertEquals(LocalDate.parse("2025-04-15", DateTimeFormatter.ISO_DATE),
            eventDetails.get(EventKeys.START_DATETIME));
    assertNull(eventDetails.get(EventKeys.LOCATION));
    assertEquals("discussion", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("true", eventDetails.get(EventKeys.PRIVATE));

  }

  @Test
  public void testAllDayRecurringEventCreation_test() {
    String command = "create event meeting on 2025-04-15 repeats MTWRFSU until 2025-05-01" +
            " location room1 description discussion public";

    controllerCreateCommand.execute(command);
    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);
    assertEquals("true", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("meeting", eventDetails.get(EventKeys.SUBJECT));
    assertEquals(LocalDate.parse("2025-04-15", DateTimeFormatter.ISO_DATE),
            eventDetails.get(EventKeys.START_DATETIME));
    assertEquals(LocalDate.parse("2025-05-01", DateTimeFormatter.ISO_DATE),
            eventDetails.get(EventKeys.END_DATETIME));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertEquals("discussion", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("false",eventDetails.get(EventKeys.PRIVATE));
  }

  @Test
  public void testAllDayEventCreation_test() {
    String command = "create event meeting on 2025-04-15" +
            " location room1 description discussion public";

    controllerCreateCommand.execute(command);
    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);
    assertEquals("true", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("meeting", eventDetails.get(EventKeys.SUBJECT));
    assertEquals(LocalDate.parse("2025-04-15", DateTimeFormatter.ISO_DATE),
            eventDetails.get(EventKeys.START_DATETIME));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertEquals("discussion", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("false",eventDetails.get(EventKeys.PRIVATE));
  }

}
