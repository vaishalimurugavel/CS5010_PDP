package calendar.controller;


import calendar.model.CalendarEvent;
import calendar.view.CalendarView;

public interface ControllerCommand {
  void execute(String command, CalendarEvent event, CalendarView view);
}
