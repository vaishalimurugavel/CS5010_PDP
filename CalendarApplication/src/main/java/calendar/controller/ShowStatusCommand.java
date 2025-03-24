package calendar.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.model.CalendarEvent;
import calendar.view.CalendarView;

/**
 * Created at 23-03-2025
 * Author Vaishali
 **/
class ShowStatusCommand implements ControllerCommand {
  DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  @Override
  public void execute(String command, CalendarEvent calendarEvent, CalendarView calendarView) {
    String showPattern = "show status on (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})";
    Pattern p = Pattern.compile(showPattern);
    Matcher m = p.matcher(command);
    String comment = null;

    if (m.matches()) {
      LocalDateTime start = LocalDateTime.parse(m.group(1), dateTimeFormatter);
      try {
        List<Map<String, String >> res = calendarEvent.getUserStatus(start);
        if(res.size() > 0) {
          comment = "User is BUSY!\n";
        }
        else {
          comment = "User is not BUSY!\n";
        }
        calendarView.displayOutput(comment);
      } catch (IOException e) {
        throw new RuntimeException("Unable to write data");
      }
    } else {
      throw new IllegalArgumentException("Invalid show command format.");
    }
  }
}
