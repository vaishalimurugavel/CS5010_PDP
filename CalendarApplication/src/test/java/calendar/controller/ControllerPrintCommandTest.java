package calendar.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import calendar.model.CalenderEventManager;
import calendar.view.CalendarSimpleView;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ControllerPrintCommandTest {

  private final PrintEventsCommand command = new PrintEventsCommand();
  ControllerCreateCommand controllerCreateCommand = new ControllerCreateCommand();
  private static final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

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

    String createCommand = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 location " +
            "room1 description meeting public";

    controllerCreateCommand.execute(createCommand);
    outputStream.reset();
    String validCommand = "print events from 2025-03-25T10:00 to 2025-03-25T12:00";
    command.execute(validCommand);
    assertTrue(outputStream.toString().contains("subject"));
  }

  @Test
  public void testPrintEventsFromTo_ValidDateTimeRange() {

    String createCommand = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 location " +
            "room1 description meeting public";

    controllerCreateCommand.execute(createCommand);
    createCommand = "create event subject2 from 2025-03-27T10:00 to 2025-03-28T12:00 location " +
            "room1 description meeting public";

    controllerCreateCommand.execute(createCommand);

    outputStream.reset();
    String validCommand = "print events from 2025-03-26T10:00 to 2025-03-29T12:00";

    assertDoesNotThrow(() -> command.execute(validCommand));
    String output = outputStream.toString();
    assertTrue(output.contains("subject"));
    assertTrue(output.contains("subject2"));

  }

  @Test
  public void testPrintEventsOn_InvalidDate() {
    outputStream.reset();
    String invalidCommand = "print events on 26-03-2025"; // Wrong format

    Exception exception = assertThrows(RuntimeException.class,
            () -> command.execute(invalidCommand));
  }

  @Test
  public void testPrintEventsFromTo_InvalidFormat() {
    outputStream.reset();
    String invalidCommand = "print events from 2025/03/26 10:00 to 2025/03/26 12:00"; // Wrong format

    Exception exception = assertThrows(RuntimeException.class,
            () -> command.execute(invalidCommand));

    assertTrue(exception.getMessage().contains("Invalid print command format"),
            "Expected error for incorrect command structure.");
  }

  @Test
  public void testPrintEvents_InvalidCommand() {
    outputStream.reset();
    String invalidCommand = "show events on 2025-03-26"; // Wrong keyword

    Exception exception = assertThrows(IllegalArgumentException.class,
            () -> command.execute(invalidCommand));

    assertTrue(exception.getMessage().contains("Invalid print command format"),
            "Expected error for incorrect command structure.");
  }

}
