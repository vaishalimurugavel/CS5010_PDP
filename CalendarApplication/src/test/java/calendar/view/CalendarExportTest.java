package calendar.view;


import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import calendar.model.EventKeys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CalendarExportTest {

  private ByteArrayOutputStream output;
  private CalendarExport calendarExport;
  private List<Map<String, Object>> eventList;

  @Before
  public void setUp() {
    output = new ByteArrayOutputStream();
    calendarExport = new CalendarExport(output);
    eventList = new ArrayList<>();

    Map<String, Object> event = new HashMap<>();
    event.put(EventKeys.SUBJECT, "Meeting");
    event.put(EventKeys.START_DATETIME, LocalDateTime.parse("2025-03-25T09:00"));
    event.put(EventKeys.END_DATETIME, LocalDateTime.parse("2025-03-25T10:00"));
    event.put(EventKeys.EVENT_TYPE, "Work");
    event.put(EventKeys.DESCRIPTION, "Team meeting");
    event.put(EventKeys.LOCATION, "Conference Room");
    event.put(EventKeys.PRIVATE, "No");

    eventList.add(event);
  }

  @Test
  public void testDisplayOutput_withEvents() throws IOException {
    calendarExport.displayOutput(eventList);
    String result = output.toString();

    assertTrue(result.contains("Meeting"));
    assertTrue(result.contains("2025-03-25"));
    assertTrue(result.contains("09:00"));
    assertTrue(result.contains("2025-03-25"));
    assertTrue(result.contains("10:00"));
    assertTrue(result.contains("Work"));
    assertTrue(result.contains("Team meeting"));
    assertTrue(result.contains("Conference Room"));
    assertTrue(result.contains("No"));
  }

  @Test
  public void testDisplayOutput_emptyEventList() throws IOException {
    calendarExport.displayOutput(Collections.emptyList());
    String result = output.toString();

    assertTrue(result.isEmpty());
  }

  @Test
  public void testDisplayOutput_nullEventList() throws IOException {
    calendarExport.displayOutput((List<Map<String, Object>>) null);
    String result = output.toString();

    assertTrue(result.isEmpty());
  }

  @Test
  public void testDisplayOutput_withExceptionHandling() throws IOException {
    CalendarView calendarExport2 = new CalendarExport(output);

    eventList = new ArrayList<>();

    Map<String, Object> event = new HashMap<>();
    event.put(EventKeys.SUBJECT, "Meeting2");
    event.put(EventKeys.START_DATETIME, LocalDate.parse("2025-03-25"));
    event.put(EventKeys.END_DATETIME, LocalDate.parse("2025-03-25"));
    event.put(EventKeys.EVENT_TYPE, "Work2");
    event.put(EventKeys.DESCRIPTION, "Team meeting");
    event.put(EventKeys.LOCATION, "Conference Room 2");
    event.put(EventKeys.PRIVATE, "No");

    eventList.add(event);

    calendarExport2.displayOutput(eventList);
    String result = output.toString();

    assertTrue(result.contains("Meeting"));
    assertTrue(result.contains("2025-03-25"));
    assertFalse(result.contains("09:00"));
    assertTrue(result.contains("2025-03-25"));
    assertFalse(result.contains("10:00"));
    assertTrue(result.contains("Work"));
    assertTrue(result.contains("Team meeting"));
    assertTrue(result.contains("Conference Room"));
    assertTrue(result.contains("No"));
  }

  @Test
  public void testDisplayOutput_withoutEndDate() throws IOException {
    CalendarView calendarExport2 = new CalendarExport(output);

    eventList = new ArrayList<>();

    Map<String, Object> event = new HashMap<>();
    event.put(EventKeys.SUBJECT, "Meeting2");
    event.put(EventKeys.START_DATETIME, LocalDate.parse("2025-03-25"));
    event.put(EventKeys.ALLDAY_DATE, LocalDate.parse("2025-03-25"));
    event.put(EventKeys.EVENT_TYPE, "Work2");
    event.put(EventKeys.DESCRIPTION, "Team meeting");
    event.put(EventKeys.LOCATION, "Conference Room 2");
    event.put(EventKeys.PRIVATE, "No");

    eventList.add(event);

    calendarExport2.displayOutput(eventList);
    String result = output.toString();

    assertTrue(result.contains("Meeting"));
    assertTrue(result.contains("2025-03-25"));
    assertFalse(result.contains("09:00"));
    assertTrue(result.contains("2025-03-25"));
    assertFalse(result.contains("10:00"));
    assertTrue(result.contains("Work"));
    assertTrue(result.contains("Team meeting"));
    assertTrue(result.contains("Conference Room"));
    assertTrue(result.contains("No"));
  }

  @Test
  public void testDisplayOutput_String() throws IOException {

    calendarExport.displayOutput("String Input");
    String result = output.toString();

    assertEquals("Export class!", result);
  }

  class FailingOutputStream extends OutputStream {
    @Override
    public void write(int b) throws IOException {
    }

    @Override
    public void close() throws IOException {
      throw new IOException("Simulated IO exception on close");
    }
  }

  @Test
  public void testCloseNormal() throws IOException {
    OutputStream outputStream = new FailingOutputStream() {
      @Override
      public void close() throws IOException {
        //simulation
      }
    };
    CalendarExport calendarExport = new CalendarExport(outputStream);
    calendarExport.close();
  }
  @Test
  public void testCloseThrowsIOException() {
    OutputStream outputStream = new FailingOutputStream();
    CalendarExport calendarExport = new CalendarExport(outputStream);

    assertThrows(IOException.class, () -> calendarExport.close());
  }

}
