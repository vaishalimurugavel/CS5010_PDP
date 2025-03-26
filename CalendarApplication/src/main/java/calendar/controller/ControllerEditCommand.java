package calendar.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.model.EventKeys;


/**
 * ControllerEditCommand class is responsible for handling the logic of editing events in the calendar.
 * It implements the ControllerCommand interface and overrides the execute method to process various
 * types of edit commands related to calendar events.
 * <p>
 * The class uses regular expressions to parse the input command string and extract relevant details
 * like event properties, start and end times, and new values for the events. It then passes these details
 * to the model for updating the event.
 * </p>
 * <p>
 * The class supports three types of edit commands:
 * 1. Editing a single event with a start and end time.
 * 2. Editing multiple events with a start time but no end time.
 * 3. Editing multiple events with no time details, just updating properties.
 * </p>
 * <p>
 * Each pattern is compiled, matched against the input command, and relevant data is extracted to update
 * the event properties accordingly.
 * </p>
 **/
public class ControllerEditCommand implements ControllerCommand {

  
  @Override
  public void execute(String command) {
    String pattern1 = "edit event (\\w+) (.+?) from " + "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})" 
            + " to (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) with (.+)";
    String pattern2 = "edit events (\\w+) (.+?) from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) " 
            + "with (.+)";
    String pattern3 = "edit events (\\w+) (.+?) (.+)";

    Pattern p1 = Pattern.compile(pattern1);
    Pattern p2 = Pattern.compile(pattern2);
    Pattern p3 = Pattern.compile(pattern3);

    Matcher m1 = p1.matcher(command);
    Matcher m2 = p2.matcher(command);
    Matcher m3 = p3.matcher(command);

    Map<String, Object> eventDes = new HashMap<>();
    if (m1.matches()) {
      String property = m1.group(1);
      String eventName = m1.group(2);
      LocalDateTime startDateTime = LocalDateTime.parse(m1.group(3), DateTimeFormatter.ISO_DATE_TIME);
      LocalDateTime endDateTime = LocalDateTime.parse(m1.group(4), DateTimeFormatter.ISO_DATE_TIME);
      String newValue = m1.group(5);

      eventDes.put(EventKeys.PROPERTY, property);
      eventDes.put(EventKeys.SUBJECT, eventName);
      eventDes.put(EventKeys.START_DATETIME, startDateTime);
      eventDes.put(EventKeys.END_DATETIME, endDateTime);
      eventDes.put(EventKeys.NEW_VALUE, newValue);

      CalendarFactory.getModel().updateEvent(eventDes);
    } else if (m2.matches()) {
      String property = m2.group(1);
      String eventName = m2.group(2);
      LocalDateTime startDateTime = LocalDateTime.parse(m2.group(3), DateTimeFormatter.ISO_DATE_TIME);
      String newValue = m2.group(4);

      eventDes.put(EventKeys.PROPERTY, property);
      eventDes.put(EventKeys.SUBJECT, eventName);
      eventDes.put(EventKeys.START_DATETIME, startDateTime);
      eventDes.put(EventKeys.NEW_VALUE, newValue);
      CalendarFactory.getModel().updateEvent(eventDes);
    } else if (m3.matches()) {
      String property = m3.group(1);
      String eventName = m3.group(2);
      String newValue = m3.group(3);

      eventDes.put(EventKeys.PROPERTY, property);
      eventDes.put(EventKeys.SUBJECT, eventName);
      eventDes.put(EventKeys.NEW_VALUE, newValue);
      CalendarFactory.getModel().updateEvent(eventDes);
    } else {
      throw new IllegalArgumentException("Invalid edit command format.");
    }
  }
}
