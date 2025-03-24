package calendar.controller;

import java.util.HashMap;
import java.util.Map;

import calendar.model.CalendarEvent;
import calendar.view.CalendarView;

/**
 * Created at 21-03-2025
 * Author Vaishali
 **/
public class CalendarController {
  CalendarEvent model;
  CalendarView view;
  CalendarView export;

  static Map<String, ControllerCommand> mapper = new HashMap<>();
  static {
    mapper.put("create", new ControllerCreateCommand());
    mapper.put("edit", new ControllerEditCommand());
    mapper.put("print", new PrintEventsCommand());
    mapper.put("show", new ShowStatusCommand());
    mapper.put("export", new ExportControllerCommand());
  }

  public CalendarController(CalendarEvent model, CalendarView view, CalendarView export) {
    this.model = model;
    this.view = view;
    this.export = export;
  }

  public void processCommand(String command) {
    String[] tokens = command.split(" ");
    if (tokens[0].equals("export")) {
      mapper.get(tokens[0]).execute(command, model, export);
    } else {
      mapper.get(tokens[0]).execute(command, model, view);
    }
  }

}
