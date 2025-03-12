package calendar;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Map;

import calendar.model.CalendarEvent;
import calendar.model.Event;

public class CalendarEventTest {

  private CalendarEvent testCalendarEvent;

  @Before
  public void setUp() {
    testCalendarEvent = new TestCalendarEvent();
  }

  @Test
  public void testAddEvent() {
    Event event = new Event.EventBuilder("Meeting")
            .startDateTime(LocalDateTime.of(2025, 3, 12, 10, 0))
            .endDateTime(LocalDateTime.of(2025, 3, 12, 11, 0))
            .build();

    testCalendarEvent.addEvent(event);
    assertTrue(CalendarEvent.getEventList().contains(event));
  }

//  @Test
//  public void testCheckForDuplicateEvent() {
//    Event event1 = new Event.EventBuilder("Meeting")
//            .startDateTime(LocalDateTime.of(2025, 3, 12, 10, 0))
//            .endDateTime(LocalDateTime.of(2025, 3, 12, 11, 0))
//            .build();
//    testCalendarEvent.addEvent(event1);
//
//    // Attempting to add overlapping event
//    boolean isDuplicate = testCalendarEvent.checkForDuplicates(
//            LocalDateTime.of(2025, 3, 12, 10, 30),
//            LocalDateTime.of(2025, 3, 12, 11, 30),
//            null,
//            null
//    );
//
//    assertTrue(isDuplicate);
//  }

  // Concrete subclass for testing abstract CalendarEvent
  private static class TestCalendarEvent extends CalendarEvent {
    @Override
    public void addEvent(Map<String, Object> eventDes) {
      Event e = new Event.EventBuilder((String) eventDes.get("subject")).build();
      addEvent(e);
    }
  }
}
