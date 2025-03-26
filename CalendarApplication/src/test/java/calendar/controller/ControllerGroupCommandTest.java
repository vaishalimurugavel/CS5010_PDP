package calendar.controller;


import calendar.model.CalendarEvent;
import calendar.model.CalendarGroupManager;
import calendar.model.Calendars;
import calendar.model.CalenderEventManager;
import calendar.model.EventKeys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerGroupCommandTest {

  private ControllerGroupCommand controllerGroupCommand;
  private ControllerCreateCommand controllerCreateCommand;

  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @BeforeEach
  public void setUp() {
    controllerGroupCommand = new ControllerGroupCommand();
    controllerCreateCommand = new ControllerCreateCommand();
    CalendarFactory.setModel(new CalenderEventManager());
    CalendarFactory.setGroup(new CalendarGroupManager());
    System.setOut(new PrintStream(outputStream));
  }

  @Test
  public void testExecute_createCalendar() throws IllegalAccessException {

    String command = "create calendar --name WorkCalendar3 --timezone America/New_York";

    controllerGroupCommand.execute(command);

    assertNotNull(CalendarFactory.getGroup().getCalendarEvent("WorkCalendar3"));

    command = "use calendar --name WorkCalendar3";

    controllerGroupCommand.execute(command);

    String output = outputStream.toString().trim();
    assertEquals("Using Calendar Event: WorkCalendar", output);

  }

  @Test
  public void testExecute_copyEventWithStartEndTimes() throws IllegalAccessException {

    String createCommand = "create calendar --name WorkCalendar --timezone America/New_York";
    controllerGroupCommand.execute(createCommand);

    createCommand = "create calendar --name PersonalCalendar --timezone America/New_York";
    controllerGroupCommand.execute(createCommand);


    createCommand = "use calendar --name WorkCalendar";
    controllerGroupCommand.execute(createCommand);

    String eventCommand = "create event Meeting from 2025-03-24T12:00 to 2025-03-25T12:00";
    controllerCreateCommand.execute(eventCommand);

    String command = "copy event Meeting on 2025-03-24T12:00 --target PersonalCalendar to 2025-03-25T12:00";
    controllerGroupCommand.execute(command);

    assertNotNull(CalendarFactory.getGroup().getCalendarEvent("PersonalCalendar"));
  }

  @Test
  public void testExecute_copyEventOnDateOnly() throws IllegalAccessException {
    String createCommand = "create calendar --name WorkCalendar1 --timezone America/New_York";
    controllerGroupCommand.execute(createCommand);

    String eventCommand = "create event Meeting on 2025-03-24T12:00 to 2025-03-25T12:00 in WorkCalendar1";
    controllerGroupCommand.execute(eventCommand);

    String command = "copy event Meeting on 2025-03-25 --target PersonalCalendar to 2025-03-25";

    controllerGroupCommand.execute(command);

    assertNotNull(CalendarFactory.getGroup().getCalendarEvent("PersonalCalendar"));
  }

  @Test
  public void testExecute_copyEventsBetweenDates() throws IllegalAccessException {

    String createCommand = "create calendar --name WorkCalendar2 --timezone America/New_York";
    controllerGroupCommand.execute(createCommand);

    createCommand = "create calendar --name PersonalCalendar2 --timezone America/New_York";
    controllerGroupCommand.execute(createCommand);

    createCommand = "use calendar --name WorkCalendar2";
    controllerGroupCommand.execute(createCommand);

    String eventCommand = "create event Meeting from 2025-03-24T12:00 to 2025-03-25T12:00";
    controllerCreateCommand.execute(eventCommand);

    String command = "copy events between 2025-03-25 and 2025-03-26 --target PersonalCalendar2 to 2025-03-25";
    controllerGroupCommand.execute(command);

    createCommand = "use calendar --name PersonalCalendar2";
    controllerGroupCommand.execute(createCommand);

    Map<String, Object> res = CalendarFactory.getModel().getEventsForDisplay().get(0);
    assertEquals("Meeting", res.get(EventKeys.SUBJECT));
  }

  @Test
  public void testExecute_editCalendarProperty() throws IllegalAccessException {
    // Create a calendar
    String createCommand = "create calendar --name WorkCalendar --timezone America/New_York";
    controllerGroupCommand.execute(createCommand);

    // Given a command to edit a calendar's property
    String command = "edit calendar --name WorkCalendar --property timezone America/Los_Angeles";

    // Execute the command
    controllerGroupCommand.execute(command);

    // Verify that the property was updated (we assume getCalendarEvent returns the calendar with the updated property)
    Calendars calendar = CalendarFactory.getGroup().getCalendar("WorkCalendar");
    assertEquals("America/Los_Angeles", calendar.getZoneName());
  }

  @Test
  public void testExecute_invalidCommand_createCalendar() {
    // Given an invalid command for creating a calendar
    String command = "create calendar --name WorkCalendar --timezone Invalid/Timezone";

    // Execute the command and expect an exception
    assertThrows(IllegalArgumentException.class, () -> controllerGroupCommand.execute(command));
  }

  @Test
  public void testExecute_invalidCommand_copyEvent() {
    // Given an invalid command for copying an event
    String command = "copy event Meeting on 2025-03-24T12:00 --target InvalidCalendar to 2025-03-25T12:00";

    // Execute the command and expect an exception
    assertThrows(IllegalArgumentException.class, () -> controllerGroupCommand.execute(command));
  }

  @Test
  public void testExecute_invalidCommand_editCalendar() {
    String command = "edit calendar --name Work_Calendar --property timezone America/New_York";

    assertThrows(IllegalArgumentException.class, () -> controllerGroupCommand.execute(command));
  }
}
