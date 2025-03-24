package calendar.controller;

import java.io.IOException;

import calendar.model.CalendarEvent;
import calendar.view.CalendarView;

/**
 * Created at 23-03-2025
 * Author Vaishali
 **/
class ExportControllerCommand implements ControllerCommand {
  @Override
  public void execute(String command, CalendarEvent calendarEvent, CalendarView calendarView) {
    try {
      calendarView.displayOutput(calendarEvent.getEventsForDisplay());
    } catch (IOException e) {
      throw new RuntimeException("Unable to export data");
    }
  }
}
