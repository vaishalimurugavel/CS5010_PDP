package calendar.controller;


import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import calendar.model.CalendarEvent;
import calendar.model.CalenderEventManager;
import calendar.model.EventKeys;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ControllerExportCommandTest {

  private ExportControllerCommand exportController;
  private CalendarEvent model;
  private static final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


  @Before
  public void setUp() {
    System.setOut(new PrintStream(outputStream));
    exportController = new ExportControllerCommand();
    model = new CalenderEventManager();
    CalendarFactory.setModel(model);
  }

  @Test
  public void testValidExportCommand() throws IOException {
    // Create a temporary file path for testing
    File tempFile = new File("test_output1.csv");
    String validCommand = "export CalendarName " + tempFile.getAbsolutePath();

    Map<String, Object> event = new HashMap<>();
    event.put(EventKeys.SUBJECT, "Meeting");
    event.put(EventKeys.ALLDAY_DATE, LocalDate.parse("2025-03-26"));
    event.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
    model.addEvent(event);

    exportController.execute(validCommand);

    assertTrue(tempFile.exists());
    assertTrue(tempFile.length() > 0);
  }


  @Test
  public void testInvalidExportCommandFormat() {
    String invalidCommand = "export CalendarName"; // Missing file name

    Exception exception = assertThrows(RuntimeException.class, () -> {
      exportController.execute(invalidCommand);
    });

    assertTrue(exception.getMessage().contains("Invalid command"));
  }

  @Test
  public void testIllegalArgumentExceptionCommandHandling() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      exportController.execute(null);
    });

    assertTrue(exception.getMessage().contains("command is null or empty"));
  }

}
