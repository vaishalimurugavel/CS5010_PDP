package calendar.controller;


import org.junit.Before;
import org.junit.Test;

import calendar.model.CalendarGroupManager;
import calendar.model.Calendars;
import calendar.model.CalenderEventManager;
import calendar.model.EventKeys;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;


/**
 * Test class for CalendarGroupCommand. Tests if the Calendar operations are processed as intended.
 */
public class ControllerGroupCommandTest {

  private ControllerGroupCommand controllerGroupCommand;
  private ControllerCreateCommand controllerCreateCommand;

  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    controllerGroupCommand = new ControllerGroupCommand();
    controllerCreateCommand = new ControllerCreateCommand();
    CalendarFactory.setModel(new CalenderEventManager());
    CalendarFactory.setGroup(new CalendarGroupManager());
    System.setOut(new PrintStream(outputStream));
  }

  @Test
  public void testExecute_createCalendar() throws IllegalAccessException {

    String command = "create calendar --name WorkCalendar6 --timezone America/New_York";

    controllerGroupCommand.execute(command);

    assertNotNull(CalendarFactory.getGroup().getCalendarEvent("WorkCalendar6"));

    command = "use calendar --name WorkCalendar6";

    controllerGroupCommand.execute(command);

    String output = outputStream.toString().trim();
    assertEquals("Using Calendar Event: WorkCalendar6", output);

  }

  @Test
  public void testExecute_copyEventWithStartEndTimes() throws IllegalAccessException {

    String createCommand = "create calendar --name WorkCalendar --timezone America/New_York";
    controllerGroupCommand.execute(createCommand);

    createCommand = "create calendar --name PersonalCalendar --timezone America/New_York";
    controllerGroupCommand.execute(createCommand);

    createCommand = "use calendar --name PersonalCalendar";
    controllerGroupCommand.execute(createCommand);

    String output = outputStream.toString().trim();
    assertEquals("Using Calendar Event: PersonalCalendar", output);

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

    createCommand = "create calendar --name PersonalCalendar1 --timezone America/New_York";
    controllerGroupCommand.execute(createCommand);

    createCommand = "use calendar --name WorkCalendar1";
    controllerGroupCommand.execute(createCommand);

    String eventCommand = "create event --autoDecline Meeting from 2025-03-10T10:00 to 2025-03-11T10:00";
    controllerCreateCommand.execute(eventCommand);

    String command = "copy event Meeting on 2025-03-10 --target PersonalCalendar1 to 2025-03-25";

    controllerGroupCommand.execute(command);

    assertNotNull(CalendarFactory.getGroup().getCalendarEvent("PersonalCalendar1"));
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

    String command = "copy events between 2025-03-24 and 2025-03-25 --target PersonalCalendar2 to 2025-03-26";
    controllerGroupCommand.execute(command);

    createCommand = "use calendar --name PersonalCalendar2";
    controllerGroupCommand.execute(createCommand);

    String output = outputStream.toString().trim();
    System.out.println(output);
    System.out.flush();

    Map<String, Object> res = CalendarFactory.getModel().getEventsForDisplay().get(0);
    assertEquals("Meeting", res.get(EventKeys.SUBJECT));
    assertEquals(LocalDateTime.parse("2025-03-26T00:00"),res.get(EventKeys.START_DATETIME));
  }

  @Test
  public void testExecute_editCalendarProperty() throws IllegalAccessException {
    String createCommand = "create calendar --name WorkCalendar7 --timezone America/New_York";
    controllerGroupCommand.execute(createCommand);

    String command = "edit calendar --name WorkCalendar7 --property timezone America/Los_Angeles";

    controllerGroupCommand.execute(command);

    Calendars calendar = CalendarFactory.getGroup().getCalendar("WorkCalendar7");
    assertEquals("America/Los_Angeles", calendar.getZoneName());
  }

  @Test
  public void testExecute_editCalendarName() throws IllegalAccessException {
    String createCommand = "create calendar --name WorkCalendar8 --timezone America/New_York";
    controllerGroupCommand.execute(createCommand);

    String command = "edit calendar --name WorkCalendar8 --property name WorkCalendar8Edited";

    controllerGroupCommand.execute(command);

    Calendars calendar = CalendarFactory.getGroup().getCalendar("WorkCalendar8Edited");
    assertNotNull(calendar);
  }

  @Test
  public void testExecute_invalidCommand_createCalendar() {
    String command = "create calendar --name WorkCalendar --timezone Invalid/Timezone";

    assertThrows(IllegalArgumentException.class, () -> controllerGroupCommand.execute(command));
  }

  @Test
  public void testExecute_invalidCommand_copyEvent() {
    String command = "copy event Meeting on 2025-03-24T12:00 --target InvalidCalendar to 2025-03-25T12:00";

    assertThrows(NullPointerException.class, () -> controllerGroupCommand.execute(command));
  }

  @Test
  public void testExecute_invalidCommand_editCalendar() {
    String command = "edit calendar --name Work_Calendar --property timezone America/New_York";

    assertThrows(IllegalArgumentException.class, () -> controllerGroupCommand.execute(command));
  }
}
