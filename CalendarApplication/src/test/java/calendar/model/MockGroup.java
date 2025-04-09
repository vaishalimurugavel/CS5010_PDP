package calendar.model;

import java.time.LocalDate;
import java.util.Map;

public class MockGroup implements CalendarGroup {

  @Override
  public boolean checkForDuplicates(String name) {
    // Mock implementation
    return false;
  }

  @Override
  public void addCalendar(String name, String timezone) {
    // Mock implementation
  }

  @Override
  public void updateCalendar(Map<String, Object> prop) {
    // Mock implementation
  }

  @Override
  public CalendarEvent getCalendarEvent(String name) {
    // Mock implementation
    return null;
  }

  @Override
  public Calendars getCalendar(String name) {
    // Mock implementation
    return null;
  }

  @Override
  public Calendars setCurrentCalendar(String name) {
    // Mock implementation
    return null;
  }

  @Override
  public Calendars getCurrentCalendar() {
    
    return null;
  }

  @Override
  public String[] getCalendarNames() {

    return new String[] { "MockCalendar1", "MockCalendar2" };
  }

  @Override
  public void copyEvent(String sourceCalendar, String eventName, String eventStartTime, String targetCalendar) {

    System.out.println("Mock copyEvent called: sourceCalendar=" + sourceCalendar +
            ", eventName=" + eventName + ", eventStartTime=" + eventStartTime +
            ", targetCalendar=" + targetCalendar);
  }
  @Override
  public void copyEventsInRange(String sourceCalendar, LocalDate fromDate, LocalDate toDate, String targetCalendar) {

    System.out.println("Mock copyEventsInRange called: sourceCalendar=" + sourceCalendar +
            ", fromDate=" + fromDate + ", toDate=" + toDate + ", targetCalendar=" + targetCalendar);
  }
}