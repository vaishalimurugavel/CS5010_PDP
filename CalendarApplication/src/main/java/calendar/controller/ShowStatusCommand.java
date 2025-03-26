package calendar.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Command to handle the "show status" feature of the calendar.
 * This command checks whether the user is busy or not at a specified date and time.
 * The format for the command is: show status on [date_time]
 * </p>
 **/
class ShowStatusCommand implements ControllerCommand {

  @Override
  public void execute(String command) {
    String showPattern = "show status on (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})";
    Pattern p = Pattern.compile(showPattern);
    Matcher m = p.matcher(command);
    String comment = null;

    if (m.matches()) {
      LocalDateTime start = LocalDateTime.parse(m.group(1), DateTimeFormatter.ISO_DATE_TIME);
      try {
        List<Map<String, Object >> res = CalendarFactory.getModel().getUserStatus(start);
        if(!res.isEmpty()) {
          comment = "User is BUSY!\n";
        }
        else {
          comment = "User is not BUSY!\n";
        }
        CalendarFactory.getView().displayOutput(comment);
      } catch (IOException e) {
        throw new RuntimeException("Unable to write data");
      }
    } else {
      throw new IllegalArgumentException("Invalid show command format. " +
              "Please use: 'show status on YYYY-MM-DD HH:MM'");
    }
  }
}
