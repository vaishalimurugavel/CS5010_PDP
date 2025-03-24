package calendar.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.model.CalendarEvent;
import calendar.view.CalendarView;

/**
 * Created at 23-03-2025
 * Author Vaishali
 **/
class PrintEventsCommand implements ControllerCommand {
  DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  @Override
  public void execute(String command, CalendarEvent calendarEvent, CalendarView calendarView) {
    String pattern1 = "print events on (\\d{4}-\\d{2}-\\d{2})";
    Pattern p1 = Pattern.compile(pattern1);
    Matcher m1 = p1.matcher(command);

    String pattern2 = "print events from (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) "
            + "to (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})";
    Pattern p2 = Pattern.compile(pattern2);
    Matcher m2 = p2.matcher(command);

    if (m1.matches()) {
      try {
        LocalDate localDate = LocalDate.parse(m1.group(1),dateFormatter);
        calendarView.displayOutput(calendarEvent.getEventForDisplay(localDate, null));
      } catch (IOException e) {
        throw new RuntimeException("Unable to write data");
      }
    } else if (m2.matches()) {
      LocalDateTime start = LocalDateTime.parse(m2.group(1),dateTimeFormatter);
      LocalDateTime end = LocalDateTime.parse(m2.group(2),dateTimeFormatter);
      try {
        calendarView.displayOutput(calendarEvent.getEventForDisplay(start, end));
      } catch (IOException e) {
        throw new RuntimeException("Unable to write data");
      }
    } else {
      throw new IllegalArgumentException("Invalid print command format.");
    }
  }
}
