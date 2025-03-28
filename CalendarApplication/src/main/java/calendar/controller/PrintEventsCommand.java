package calendar.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Command to handle the printing of events based on user input.
 * It supports two formats:
 * - print events on [date]
 * - print events from [start_date_time] to [end_date_time]
 * </p>
 **/
class PrintEventsCommand implements ControllerCommand {

  @Override
  public void execute(String command) {
    String pattern1 = "print events on (\\d{4}-\\d{2}-\\d{2})";
    String pattern2 = "print events from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to" +
            " (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})";

    Pattern p1 = Pattern.compile(pattern1);
    Matcher m1 = p1.matcher(command);

    Pattern p2 = Pattern.compile(pattern2);
    Matcher m2 = p2.matcher(command);

    if (m1.matches()) {
      try {
        LocalDate localDate = LocalDate.parse(m1.group(1), DateTimeFormatter.ISO_DATE);

        CalendarFactory.getView()
                .displayOutput(CalendarFactory.getModel().getEventForDisplay(localDate, null));
      } catch (IOException e) {
        throw new RuntimeException("Error occurred while trying to fetch and display events " +
                "for the date " + m1.group(1) + ": Unable to write data.", e);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid date format provided: " + m1.group(1), e);
      }
    } else if (m2.matches()) {
      try {
        LocalDateTime start = LocalDateTime.parse(m2.group(1), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime end = LocalDateTime.parse(m2.group(2), DateTimeFormatter.ISO_DATE_TIME);

        CalendarFactory.getView().displayOutput(CalendarFactory.getModel()
                .getEventForDisplay(start, end));
      } catch (IOException e) {
        throw new RuntimeException("Error occurred while trying to fetch " +
                "and display events between " + m2.group(1) + " and " + m2.group(2)
                + ": Unable to write data.", e);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid date format provided: " + m2.group(1) + " or "
                + m2.group(2), e);
      }
    } else {
      throw new IllegalArgumentException("Invalid print command format. Please use one" +
              " of the following formats: "
              + "'print events on YYYY-MM-DD' " +
              "or 'print events from YYYY-MM-DD HH:MM to YYYY-MM-DD HH:MM'.");
    }
  }
}
