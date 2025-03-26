package calendar.controller;

import org.junit.Before;
import org.junit.Test;

import calendar.controller.CalendarController;
import calendar.controller.ControllerCommand;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CalendarControllerTest {

  private CalendarController controller;

  @Before
  public void setUp() {
    controller = new CalendarController();

    // Replace the static mapper with real implementations for testing
    CalendarController.mapper.put("create", new TestCommand("create"));
    CalendarController.mapper.put("edit", new TestCommand("edit"));
    CalendarController.mapper.put("print", new TestCommand("print"));
    CalendarController.mapper.put("show", new TestCommand("show"));
    CalendarController.mapper.put("export", new TestCommand("export"));
    CalendarController.mapper.put("copy", new TestCommand("copy"));
  }

  public void testProcessCommand_ValidCreateCommand() {
    NullPointerException thrown = assertThrows(NullPointerException.class,
            () -> controller.processCommand
                    ("create event --autoDecline Event4 on 2025-03-14 10:20"));
  }

  @Test
  public void testProcessCommand_ValidEditCommand() {
    assertDoesNotThrow(() -> controller.processCommand("edit event"));
  }

  @Test
  public void testProcessCommand_CopyCommand() {
    assertDoesNotThrow(() -> controller.processCommand("copy event"));
  }

  @Test
  public void testProcessCommand_InvalidCommandThrowsException() {
    Exception exception = assertThrows(NullPointerException.class, () -> {
      controller.processCommand("invalid command");
    });
  }


  /**
   * A simple test implementation of ControllerCommand for validation.
   */
  static class TestCommand implements ControllerCommand {
    private final String name;

    TestCommand(String name) {
      this.name = name;
    }

    @Override
    public void execute(String command) {
      System.out.println(name + " command executed: " + command);
    }
  }

}
