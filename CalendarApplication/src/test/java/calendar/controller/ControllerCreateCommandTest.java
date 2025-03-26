package calendar.controller;
import org.junit.Before;
import org.junit.Test;

import calendar.model.CalenderEventManager;
import calendar.model.EventKeys;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerCreateCommandTest {

  private ControllerCreateCommand controllerCreateCommand;

  @Before
  public void setUp() {
    controllerCreateCommand = new ControllerCreateCommand();
    CalendarFactory.setModel(new CalenderEventManager());
  }

  @Test
  public void testSingleEventCreation() {
    String command = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 location room1 description meeting public";

    // Call the method to execute the command
    controllerCreateCommand.execute(command);

    // Retrieve the event details (assuming the event has been added to the calendar model)
    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    // Check if the event type is correct
    assertEquals("false", eventDetails.get(EventKeys.EVENT_TYPE));

    // Validate event details
    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertEquals("meeting", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("false", eventDetails.get(EventKeys.PRIVATE));
  }

  @Test
  public void testAllDayEventCreation() {
    String command = "create event subject on 2025-03-25 location room1 description meeting public";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("true", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertEquals("meeting", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("false", eventDetails.get(EventKeys.PRIVATE));
  }

  @Test
  public void testRecurringEventCreation() {
    String command = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 repeats MTWRFSU for 5 times location room1 description meeting public";

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
    String command = "create event subject from 2025-03-25T10:00 to 2025-03-24T12:00 location room1 description meeting public";

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      controllerCreateCommand.execute(command);
    });

    assertEquals("Invalid date format.", thrown.getMessage());
  }

  @Test
  public void testAllDayRecurringEventCreation() {
    String command = "create event subject on 2025-03-25 repeats MTWRFSU for 5 times location room1 description meeting public";

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
    String command = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 location room1 public";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("false", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertNull(eventDetails.get(EventKeys.DESCRIPTION));
  }

  @Test
  public void testEventWithPrivateFlag() {
    String command = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 location room1 description meeting private";

    controllerCreateCommand.execute(command);

    Map<String, Object> eventDetails = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("false", eventDetails.get(EventKeys.EVENT_TYPE));
    assertEquals("subject", eventDetails.get(EventKeys.SUBJECT));
    assertEquals("room1", eventDetails.get(EventKeys.LOCATION));
    assertEquals("meeting", eventDetails.get(EventKeys.DESCRIPTION));
    assertEquals("true", eventDetails.get(EventKeys.PRIVATE));
  }
}
