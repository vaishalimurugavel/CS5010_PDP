package calendar.controller;

import java.io.IOException;
import java.util.Map;

import calendar.model.CalendarEvent;
import calendar.view.CalendarView;

/**
 * Mock class for the Controller.
 */
public class MockController extends CalendarController {

  private CalendarEvent model;
  private CalendarView view;

  public MockController(CalendarEvent model, CalendarView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void processCommand(String command) throws IllegalArgumentException {
    if (command.equals("add event")) {
      Map<String, Object> newEvent = Map.of(
              "subject", "New Event",
              "startDateTime", "2025-03-22T10:00:00",
              "endDateTime", "2025-03-22T11:00:00",
              "location", "Room B",
              "description", "New event description",
              "private", false
      );
      model.addEvent(newEvent);
    } else if (command.equals("view events")) {
      try {
        view.displayOutput(model.getEventsForDisplay());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      try {
        view.displayOutput("Invalid command");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}