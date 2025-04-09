package calendar.controller;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import calendar.model.CalenderEventManager;
import calendar.model.EventKeys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for CalendarController. Used to test if the command is invoked correctly.
 */
public class CalendarControllerTest {
  private CalendarController controller;


  @Before
  public void setUp() {
    controller = new CalendarController();
    CalendarFactory.setModel(new CalenderEventManager());
  }

  @Test
  public void testProcessCommand_Invalidcommand() {
    String command = "create event subject from 2025-03-25 to 2025-03-25T12:00 " +
            "location room1 description meeting public";

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      controller.processCommand(command);
    });
    assertEquals("Error while processing command create event subject from 2025-03-25" +
                    " to 2025-03-25T12:00 location room1 description meeting public",
            thrown.getMessage());


  }

  @Test
  public void testProcessCommand_InvalidEditCommand() {
    String command = "edit event location title Conference from 2025-03-25T14:00 with " +
            "New Location";

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      controller.processCommand(command);
    });

    assertEquals("Error while processing command edit event location title Conference " +
                    "from 2025-03-25T14:00 with New Location",
            thrown.getMessage());

  }

  @Test
  public void testProcessCommand_InvalidGroupCommand() {
    String command = "create calendar --name WorkCalendar7 --timezone ";

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      controller.processCommand(command);
    });
    assertEquals("Error while processing command create calendar " +
            "--name WorkCalendar7 --timezone ", thrown.getMessage());


  }

  @Test
  public void testProcessCommand_ValidGroupCommand() {
    String command = "create calendar --name WorkCalendar7 --timezone America/New_York";
    controller.processCommand(command);
    assertEquals("WorkCalendar7", CalendarFactory.getGroup().
            getCalendar("WorkCalendar7").getTitle());

    command = "use calendar --name WorkCalendar7";
    controller.processCommand(command);

    assertEquals("WorkCalendar7", CalendarFactory.getGroup().
            getCurrentCalendar().getTitle());

    command = "edit calendar --name WorkCalendar7 --property timezone America/Los_Angeles";
    controller.processCommand(command);

    assertEquals("America/Los_Angeles", CalendarFactory.getGroup().
            getCurrentCalendar().getZoneName());

    command = "create calendar --name WorkCalendar73 --timezone America/New_York";
    controller.processCommand(command);

    command = "create event Event5 on 2025-03-30 repeats MTW for 6 times";
    controller.processCommand(command);

    command = "create event --autoDecline Event2 from 2025-03-27T10:30 to 2025-03-28T10:00" +
            " repeats MTW for 3 times location Ryder hall description Event2";
    controller.processCommand(command);

    command = "create event Event6 on 2025-03-21 repeats TRF until 2025-03-23";
    controller.processCommand(command);

    command = "copy events between 2025-03-21 and 2025-03-24 --target WorkCalendar73 to " +
            "2025-03-30";
    controller.processCommand(command);

    command = "use calendar --name WorkCalendar73";
    controller.processCommand(command);

    Map<String, Object> res = CalendarFactory.getModel().getEventsForDisplay().get(0);
    assertEquals("Event6", res.get(EventKeys.SUBJECT));
  }

}