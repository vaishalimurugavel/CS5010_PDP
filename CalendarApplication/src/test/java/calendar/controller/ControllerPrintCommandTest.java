package calendar.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import calendar.model.CalenderEventManager;
import calendar.view.CalendarSimpleView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CalendarPrintCommand. Tests if the print event command is processed as intended.
 */
public class ControllerPrintCommandTest {

  private static final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final PrintEventsCommand command = new PrintEventsCommand();
  ControllerCreateCommand controllerCreateCommand = new ControllerCreateCommand();

  @Before
  public void setUp() {
    System.setOut(new PrintStream(outputStream));

    controllerCreateCommand = new ControllerCreateCommand();
    CalendarFactory.setModel(new CalenderEventManager());
    CalendarFactory.setView(new CalendarSimpleView(outputStream));
    CalendarFactory.setModel(new CalenderEventManager());
  }

  @After
  public void tearDown() {
    System.setOut(System.out);
  }


  @Test
  public void testPrintEventsOn_ValidDate() {

    String createCommand = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 " +
            "location room1 description meeting public";

    controllerCreateCommand.execute(createCommand);
    outputStream.reset();
    String validCommand = "print events from 2025-03-25T10:00 to 2025-03-25T12:00";
    command.execute(validCommand);
    assertTrue(outputStream.toString().contains("subject"));
  }

  @Test
  public void testPrintEventsOn_validDate() {
    outputStream.reset();

    String createCommand = "create event subject on 2025-03-25 " +
            "location room1 description meeting public";

    controllerCreateCommand.execute(createCommand);
    String validCommand = "print events on 2025-03-25";
    command.execute(validCommand);
    assertTrue(outputStream.toString().contains("subject"));
  }

  @Test
  public void testPrintEventsFromTo_ValidDateTimeRange() {

    String createCommand = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 " +
            "location room1 description meeting public";

    controllerCreateCommand.execute(createCommand);
    createCommand = "create event subject2 from 2025-03-27T10:00 to 2025-03-28T12:00 location " +
            "room1 description meeting public";

    controllerCreateCommand.execute(createCommand);

    outputStream.reset();
    String validCommand = "print events from 2025-03-26T10:00 to 2025-03-29T12:00";

    command.execute(validCommand);
    String output = outputStream.toString();
    assertTrue(output.contains("subject"));
    assertTrue(output.contains("subject2"));

  }

  @Test
  public void testPrintEventsOn_InvalidDate() {
    outputStream.reset();
    String invalidCommand = "print events on 26-03-2025";

    Exception exception = assertThrows(RuntimeException.class,
            () -> command.execute(invalidCommand));
    assertEquals("Invalid print command format. Please use one of the " +
            "following formats: 'print events on YYYY-MM-DD' or 'print events " +
            "from YYYY-MM-DD HH:MM to YYYY-MM-DD HH:MM'." , exception.getMessage());
  }

  @Test
  public void testPrintEventsFromTo_InvalidFormat() {
    outputStream.reset();
    String invalidCommand = "print events from 2025/03/26 10:00 to 2025/03/26 12:00";

    Exception exception = assertThrows(RuntimeException.class,
            () -> command.execute(invalidCommand));

    assertTrue(exception.getMessage().contains("Invalid print command format"));
  }

  @Test
  public void testPrintEvents_InvalidCommand() {
    outputStream.reset();
    String invalidCommand = "show events on 2025-03-26"; // Wrong keyword

    Exception exception = assertThrows(IllegalArgumentException.class,
            () -> command.execute(invalidCommand));

    assertTrue(exception.getMessage().contains("Invalid print command format"));
  }

}