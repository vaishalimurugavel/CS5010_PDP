package calendar.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import calendar.model.CalenderEventManager;
import calendar.view.CalendarSimpleView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CalendarShowCommandTest {

  private final ShowStatusCommand command = new ShowStatusCommand();
  ControllerCreateCommand controllerCreateCommand = new ControllerCreateCommand();
  private static ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    System.setOut(new PrintStream(outputStream));

    CalendarFactory.setView(new CalendarSimpleView(outputStream));
    CalendarFactory.setModel(new CalenderEventManager());
  }


  @Before
  public void clearOutput() {
    outputStream.reset();
  }

  @Test
  public void testValidCommand_UserIsBusy() {

    String createCommand = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 location " +
            "room1 description meeting public";

    controllerCreateCommand.execute(createCommand);
    outputStream.reset();

    String validCommand = "show status on 2025-03-25T10:00";
    command.execute(validCommand);
    assertEquals("User is BUSY!", outputStream.toString().trim());
  }

  @Test
  public void testValidCommand_UserIsNotBusy() {

    String createCommand = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 location " +
            "room1 description meeting public";

    controllerCreateCommand.execute(createCommand);
    outputStream.reset();
    String validCommand = "show status on 2025-03-26T10:00";
    command.execute(validCommand);
    assertEquals("User is not BUSY!", outputStream.toString().trim());
  }

  @Test
  public void testInvalidCommand_WrongFormat() {

    String createCommand = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 location " +
            "room1 description meeting public";

    controllerCreateCommand.execute(createCommand);
    outputStream.reset();

    String invalidCommand = "status on 2025-03-25T10:00";
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      command.execute(invalidCommand);
    });
    assertTrue(exception.getMessage().contains("Invalid show command format"));
  }

  @Test
  public void testInvalidCommand_WrongDateFormat() {

    String createCommand = "create event subject from 2025-03-25T10:00 to 2025-03-25T12:00 location " +
            "room1 description meeting public";

    controllerCreateCommand.execute(createCommand);
    outputStream.reset();

    String invalidCommand = "show status on 25-03-2025 10:00";
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      command.execute(invalidCommand);
    });
    assertTrue(exception.getMessage().contains("Invalid show command format"));
  }

}
