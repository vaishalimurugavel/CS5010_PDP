package calendar.model;

import java.util.Map;

/**
 * Created at 25-03-2025
 * Author Vaishali
 **/

public class MockGroup implements CalendarGroup{

  private String groupName;

  public MockGroup(String groupName) {
    this.groupName = groupName;
  }

  public String getGroupName() {
    return groupName;
  }

  @Override
  public boolean checkForDuplicates(String name) {
    return false;
  }

  @Override
  public void addCalendar(String name, String timezone) {

  }

  @Override
  public void updateCalendar(Map<String, Object> prop) {

  }

  @Override
  public CalendarEvent getCalendarEvent(String name) {
    return null;
  }

  @Override
  public Calendars getCalendar(String name) {
    return null;
  }
}