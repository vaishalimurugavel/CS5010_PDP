package calendar.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.view.CalendarExport;
import calendar.view.CalendarView;

/**
 * <p>
 * ExportControllerCommand handles exporting calendar data to a specified file.
 * It reads the export command, validates the file path, and performs the export operation.
 * </p>
 **/
class ExportControllerCommand implements ControllerCommand {
  @Override
  public void execute(String command) {
    if(command == null || command.isEmpty()){
      throw new IllegalArgumentException("command is null or empty");
    }
    CalendarView export = CalendarFactory.getExport();
    try {
      String path = null;
      String exportPattern = "export ([A-Za-z0-9_\\-]+) ([A-Za-z0-9_\\-\\\\:]+).csv";
      Pattern create = Pattern.compile(exportPattern);
      Matcher createMatcher = create.matcher(command);
      if(createMatcher.matches()) {
        String[] parts = command.split(" ");
        path = parts[parts.length - 1];
        if (path != null) {
          try {
            export = new CalendarExport(new FileOutputStream(path));
          } catch (IOException e) {
            throw new IOException("Unable to export data");
          }
        }
        export.displayOutput(CalendarFactory.getModel().getEventsForDisplay());
      }
      else {
        throw new IllegalArgumentException("Invalid command: " + command);
      }
    } catch (IOException e) {
      System.err.println("Unable to export data");
    }
  }
}
