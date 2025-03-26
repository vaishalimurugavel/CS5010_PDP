package calendar.controller;
import org.junit.Before;
import org.junit.Test;

import calendar.model.CalenderEventManager;
import calendar.model.EventKeys;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerEditCommandTest {

  private ControllerEditCommand controllerEditCommand;
  private ControllerCreateCommand controllerCreateCommand;

  @Before
  public void setUp() {
    controllerEditCommand = new ControllerEditCommand();
    controllerCreateCommand = new ControllerCreateCommand();
    CalendarFactory.setModel(new CalenderEventManager());
  }

  @Test
  public void testExecute_editSingleEvent() {
    String create = "create event Team Meeting from 2025-03-25T10:00 to 2025-03-25T12:00 location Old Location";
    controllerCreateCommand.execute(create);
    Map<String, Object> eventDes = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("Team Meeting", eventDes.get(EventKeys.SUBJECT));
    assertEquals("Old Location", eventDes.get(EventKeys.LOCATION));

    String command = "edit event location Event2 from 2025-03-25T10:00 to 2025-03-25T12:00 with New Location";

    controllerEditCommand.execute(command);

     eventDes = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("Team Meeting", eventDes.get(EventKeys.SUBJECT));
    assertEquals("New Location", eventDes.get(EventKeys.LOCATION));
  }

  @Test
  public void testExecute_editMultipleEventsWithStartTime() {
    String create = "create event title Conference from 2025-03-25T14:00 to 2025-03-25T17:00 location Old Location";
    controllerCreateCommand.execute(create);
    Map<String, Object> eventDes = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("title Conference", eventDes.get(EventKeys.SUBJECT));
    assertEquals("Old Location", eventDes.get(EventKeys.LOCATION));

    String command = "edit events location title Conference from 2025-03-25T14:00 with New Location";

    controllerEditCommand.execute(command);

    eventDes = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("title Conference", eventDes.get(EventKeys.SUBJECT));
    assertEquals("New Location", eventDes.get(EventKeys.LOCATION));
  }

  @Test
  public void testExecute_editMultipleEventsWithoutTime() {
    String create = "create event title1 from 2025-03-25T14:00 to 2025-03-25T17:00 location Old Location";
    controllerCreateCommand.execute(create);

    create = "create event title1 from 2025-03-26T16:00 to 2025-03-27T17:00 location Old Location";
    controllerCreateCommand.execute(create);

    String command = "edit events subject title1 title";
    controllerEditCommand.execute(command);

    Map<String, Object> eventDes = CalendarFactory.getModel().getEventsForDisplay().get(0);

    assertEquals("title", eventDes.get(EventKeys.SUBJECT));
  }

  @Test
  public void testExecute_invalidCommand() {

    String create = "create event title1 from 2025-03-25T14:00 to 2025-03-25T17:00 location Old Location";
    controllerCreateCommand.execute(create);

    String command = "edit event title Team Meeting from 2025-03-25T10:00 with";

    assertThrows(IllegalArgumentException.class, () -> controllerEditCommand.execute(command));
  }

  @Test
  public void testExecute_invalidDateTimeFormat() {

    String create = "create event title1 from 2025-03-25T14:00 to 2025-03-25T17:00 location Old Location";
    controllerCreateCommand.execute(create);

    String command = "edit event title Team Meeting from 2025-03-25T10:00 to invalid-date with New Location";

    assertThrows(IllegalArgumentException.class, () -> controllerEditCommand.execute(command));
  }

}
