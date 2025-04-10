package calendar.model;

import java.util.Map;

/**
 * Mock group class.
 */
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

}