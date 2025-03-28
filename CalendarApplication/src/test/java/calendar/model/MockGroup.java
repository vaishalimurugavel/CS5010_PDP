package calendar.model;

import java.util.Map;

/**
 * Mock class for the CalendarGroup.
 **/
public class MockGroup implements CalendarGroup {

  private String groupName;

  /**
   * Constructor for MockGroup.
   * @param groupName name
   */
  public MockGroup(String groupName) {
    this.groupName = groupName;
  }

  /**
   * Getter class for name.
   * @return String
   */
  public String getGroupName() {
    return groupName;
  }

  @Override
  public boolean checkForDuplicates(String name) {
    return false;
  }

  @Override
  public void addCalendar(String name, String timezone) {
      //Do nothing
  }

  @Override
  public void updateCalendar(Map<String, Object> prop) {
      //Do nothing
  }

  @Override
  public CalendarEvent getCalendarEvent(String name) {
    return null;
  }

  @Override
  public Calendars getCalendar(String name) {
    return null;
  }

  @Override
  public Calendars setCurrentCalendar(String name) {
    return null;
  }

  @Override
  public Calendars getCurrentCalendar() {
    return null;
  }
}