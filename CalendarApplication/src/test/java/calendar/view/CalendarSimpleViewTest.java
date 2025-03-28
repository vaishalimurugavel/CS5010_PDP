package calendar.view;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calendar.model.EventKeys;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Test class to test CalendarView.
 */
public class CalendarSimpleViewTest {

  private ByteArrayOutputStream output;
  private CalendarSimpleView calendarSimpleView;
  private List<Map<String, Object>> eventList;

  @Before
  public void setUp() {
    output = new ByteArrayOutputStream();
    calendarSimpleView = new CalendarSimpleView(output);
    eventList = new ArrayList<>();

    // Setup a sample event
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
    calendarSimpleView.displayOutput(eventList);
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
    calendarSimpleView.displayOutput(Collections.emptyList());
    String result = output.toString();

    assertTrue(result.contains("No calendar event!"));
  }

  @Test
  public void testDisplayOutput_nullEventList() throws IOException {
    calendarSimpleView.displayOutput((List<Map<String, Object>>) null);
    String result = output.toString();

    assertTrue(result.contains("No calendar event!"));
  }

  @Test
  public void testDisplayOutput_withExceptionHandling() throws IOException {
    output = new ByteArrayOutputStream();
    CalendarView calendarSimpleView2 = new CalendarSimpleView(output);

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

    calendarSimpleView2.displayOutput(eventList);
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
    output = new ByteArrayOutputStream();
    CalendarView calendarSimpleView2 = new CalendarSimpleView(output);

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

    calendarSimpleView2.displayOutput(eventList);
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

    calendarSimpleView.displayOutput("String Input");
    String result = output.toString();

    assertEquals("String Input", result);
  }
}
