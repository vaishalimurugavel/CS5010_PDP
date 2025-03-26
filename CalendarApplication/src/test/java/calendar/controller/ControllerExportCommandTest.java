package calendar.controller;


import org.junit.Before;
import org.junit.Test;

import calendar.model.CalenderEventManager;
import calendar.model.EventKeys;
import calendar.view.CalendarExport;
import calendar.view.CalendarView;
import calendar.model.CalendarEvent;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerExportCommandTest {

  private ExportControllerCommand exportController;
  private CalendarView exportView;
  private CalendarEvent model;

  @Before
  public void setUp() {
    exportController = new ExportControllerCommand();
    exportView = new CalendarExport(System.out); // Using console output for testing
    model = new CalenderEventManager();

    // Set the factory components
    CalendarFactory.setExport(exportView);
    CalendarFactory.setModel(model);
  }

  @Test
  public void testValidExportCommand() throws IOException {
    // Create a temporary file path for testing
    File tempFile = new File("test_output.csv");
    String validCommand = "export CalendarName " + tempFile.getAbsolutePath();

    Map<String, Object> event = new HashMap<>();
    event.put(EventKeys.SUBJECT, "Meeting");
    event.put(EventKeys.ALLDAY_DATE, LocalDate.parse("2025-03-26"));
    event.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
    model.addEvent(event);

    exportController.execute(validCommand);

    assertTrue(tempFile.exists(), "Export file should be created");
    assertTrue(tempFile.length() > 0, "Export file should contain data");
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
