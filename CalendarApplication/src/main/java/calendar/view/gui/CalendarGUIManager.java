package calendar.view.gui;

import java.io.IOException;
import java.util.Map;

import calendar.controller.CalendarFeatures;
import calendar.controller.CalendarGUICalendar;

public class CalendarGUIManager implements CalendarFeatures {
  CalendarFeatures calendarFeatures = new CalendarGUICalendar();
  @Override
  public void addCalendar(String name, String timeZone) {
    calendarFeatures.addCalendar(name, timeZone);
  }

  @Override
  public void editCalendar(Map<String, String> properties) {
    calendarFeatures.editCalendar(properties);
  }

  @Override
  public void addEvent(Map<String, String> details) {
    calendarFeatures.addEvent(details);
  }

  @Override
  public void editEvent(Map<String, String> details) {
    calendarFeatures.editEvent(details);
  }

  @Override
  public void exportCalendar(String name) throws IOException {
    calendarFeatures.exportCalendar(name);
  }
}
