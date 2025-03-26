package calendar.controller;

import org.junit.Before;
import org.junit.Test;

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

    CalendarController.mapper.put("create", new TestCommand("create"));
    CalendarController.mapper.put("edit", new TestCommand("edit"));
    CalendarController.mapper.put("print", new TestCommand("print"));
    CalendarController.mapper.put("show", new TestCommand("show"));
    CalendarController.mapper.put("export", new TestCommand("export"));
    CalendarController.mapper.put("copy", new TestCommand("copy"));
  }

  @Test
  public void testProcessCommand_ValidEditCommand() {
    try{
      controller.processCommand("edit event");
    } catch (IllegalAccessException e) {
      assertEquals(IllegalArgumentException.class, e.getCause().getClass());
    }
  }

  @Test
  public void testProcessCommand_CopyCommand() {
    try{
      controller.processCommand("copy event");
    } catch (IllegalAccessException e) {
      assertEquals(IllegalArgumentException.class, e.getCause().getClass());
    }
  }

  @Test
  public void testProcessCommand_InvalidCommandThrowsException() {
    Exception exception = assertThrows(NullPointerException.class, () -> {
      controller.processCommand("invalid command");
    });
  }

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
