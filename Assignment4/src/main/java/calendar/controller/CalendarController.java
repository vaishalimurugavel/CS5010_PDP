package calendar.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.model.EventKeys;
import calendar.view.CalendarExport;
import calendar.view.CalendarView;


public class CalendarController {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private static void processCommand(String command) {
    String[] tokens = command.split(" ");
    if (tokens.length < 2) throw new IllegalArgumentException("Invalid command format.");

    switch (tokens[0]) {
      case "create":
        createEvent(command);
        break;
      case "edit":
        editEvent(command);
        break;
      case "print":
        printEvents(command);
        break;
      case "export":
        exportCalendar(command);
        break;
      case "show":
        showStatus(command);
        break;
      default:
        throw new IllegalArgumentException("Unknown command: " + tokens[0]);
    }
    
  }
  
  private static void createEvent(String command) {

    Map<String, Object> eventDetails = new HashMap<>();
    
    // Regex patterns for different cases
    String basePattern = "create event( --autoDecline)? (.+?)";
    String singleAllDay = basePattern + " on (\\d{4}-\\d{2}-\\d{2})";
    String singleEventPattern = basePattern + " from (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})";
    String recurringForNTimesPattern = singleEventPattern + " repeats ([MTWRFSU]+) for (\\d+) times";
    String recurringUntilPattern = singleEventPattern + " repeats ([MTWRFSU]+) until (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})";
    String allDayEventPattern = basePattern + " on (\\d{4}-\\d{2}-\\d{2})";
    String allDayRecurringForNTimesPattern = allDayEventPattern + " repeats ([MTWRFSU]+) for (\\d+) times";
    String allDayRecurringUntilPattern = allDayEventPattern + " repeats ([MTWRFSU]+) until (\\d{4}-\\d{2}-\\d{2})";

    Pattern pSingle = Pattern.compile(singleEventPattern);
    Pattern pSingleAllDay = Pattern.compile(singleAllDay);
    Pattern pRecurringForN = Pattern.compile(recurringForNTimesPattern);
    Pattern pRecurringUntil = Pattern.compile(recurringUntilPattern);
    Pattern pAllDay = Pattern.compile(allDayEventPattern);
    Pattern pAllDayRecurringForN = Pattern.compile(allDayRecurringForNTimesPattern);
    Pattern pAllDayRecurringUntil = Pattern.compile(allDayRecurringUntilPattern);

    Matcher mSingle = pSingle.matcher(command);
    Matcher mSingleAllDay = pSingleAllDay.matcher(command);
    Matcher mRecurringForN = pRecurringForN.matcher(command);
    Matcher mRecurringUntil = pRecurringUntil.matcher(command);
    Matcher mAllDay = pAllDay.matcher(command);
    Matcher mAllDayRecurringForN = pAllDayRecurringForN.matcher(command);
    Matcher mAllDayRecurringUntil = pAllDayRecurringUntil.matcher(command);

    if (mRecurringForN.matches()) {
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.RECURRING);
      eventDetails.put(EventKeys.AUTO_DECLINE, mRecurringForN.group(1) != null);
      eventDetails.put(EventKeys.SUBJECT, mRecurringForN.group(2));
      eventDetails.put(EventKeys.START_DATETIME, LocalDateTime.parse(mRecurringForN.group(3), DATE_TIME_FORMATTER));
      eventDetails.put(EventKeys.END_DATETIME, LocalDateTime.parse(mRecurringForN.group(4), DATE_TIME_FORMATTER));
      eventDetails.put(EventKeys.WEEKDAYS, mRecurringForN.group(5));
      eventDetails.put(EventKeys.OCCURRENCES, Integer.parseInt(mRecurringForN.group(6)));
      CalendarFactory.getRecurringCalender().addEvent(eventDetails);
    } else if (mRecurringUntil.matches()) {
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.RECURRING);
      eventDetails.put(EventKeys.AUTO_DECLINE, mRecurringUntil.group(1) != null);
      eventDetails.put(EventKeys.SUBJECT, mRecurringUntil.group(2));
      eventDetails.put(EventKeys.START_DATETIME, LocalDateTime.parse(mRecurringUntil.group(3), DATE_TIME_FORMATTER));
      eventDetails.put(EventKeys.END_DATETIME, LocalDateTime.parse(mRecurringUntil.group(4), DATE_TIME_FORMATTER));
      eventDetails.put(EventKeys.WEEKDAYS, mRecurringUntil.group(5));
      eventDetails.put(EventKeys.REPEAT_DATETIME, LocalDateTime.parse(mRecurringUntil.group(6), DATE_TIME_FORMATTER));
      CalendarFactory.getRecurringCalender().addEvent(eventDetails);
    } else if (mSingle.matches()) {
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);
      eventDetails.put(EventKeys.AUTO_DECLINE, mSingle.group(1) != null);
      eventDetails.put(EventKeys.SUBJECT, mSingle.group(2));
      eventDetails.put(EventKeys.START_DATETIME, LocalDateTime.parse(mSingle.group(3), DATE_TIME_FORMATTER));
      eventDetails.put(EventKeys.END_DATETIME, LocalDateTime.parse(mSingle.group(4), DATE_TIME_FORMATTER));
      CalendarFactory.getSingleCalender().addEvent(eventDetails);
    } else if (mSingleAllDay.matches()) {
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
      eventDetails.put(EventKeys.AUTO_DECLINE, mSingle.group(1) != null);
      eventDetails.put(EventKeys.SUBJECT, mSingle.group(2));
      eventDetails.put(EventKeys.ALLDAY_DATETIME, LocalDateTime.parse(mSingle.group(3), DATE_FORMATTER));
      CalendarFactory.getSingleCalender().addEvent(eventDetails);
    } else if (mAllDayRecurringForN.matches()) {
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY_RECURRING);
      eventDetails.put(EventKeys.SUBJECT, mAllDayRecurringForN.group(2));
      eventDetails.put(EventKeys.ALLDAY_DATETIME, LocalDate.parse(mAllDayRecurringForN.group(3), DATE_FORMATTER));
      eventDetails.put(EventKeys.WEEKDAYS, mAllDayRecurringForN.group(4));
      eventDetails.put(EventKeys.OCCURRENCES, Integer.parseInt(mAllDayRecurringForN.group(5)));
      CalendarFactory.getRecurringCalender().addEvent(eventDetails);
    } else if (mAllDayRecurringUntil.matches()) {
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY_RECURRING);
      eventDetails.put(EventKeys.SUBJECT, mAllDayRecurringUntil.group(2));
      eventDetails.put(EventKeys.ALLDAY_DATETIME, LocalDate.parse(mAllDayRecurringUntil.group(3), DATE_FORMATTER));
      eventDetails.put(EventKeys.WEEKDAYS, mAllDayRecurringUntil.group(4));
      eventDetails.put(EventKeys.REPEAT_DATETIME, LocalDate.parse(mAllDayRecurringUntil.group(5), DATE_FORMATTER));
      CalendarFactory.getRecurringCalender().addEvent(eventDetails);
    } else if (mAllDay.matches()) {
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
      eventDetails.put(EventKeys.SUBJECT, mAllDay.group(2));
      eventDetails.put(EventKeys.ALLDAY_DATETIME, LocalDate.parse(mAllDay.group(3), DATE_FORMATTER));
      CalendarFactory.getRecurringCalender().addEvent(eventDetails);
    } else {
      throw new IllegalArgumentException("Invalid create event syntax.");
    }

  }

  private static boolean hasConflict(LocalDateTime start, LocalDateTime end) {
    return false;
  }

  private static void editEventCommand(String command) {
    String pattern1 = "edit event (\\w+) (.+?) from (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) with (.+)";

    String pattern2 = "edit events (\\w+) (.+?) from (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) with (.+)";

    String pattern3 = "edit events (\\w+) (.+?) (.+)";

    Pattern p1 = Pattern.compile(pattern1);
    Pattern p2 = Pattern.compile(pattern2);
    Pattern p3 = Pattern.compile(pattern3);

    Matcher m1 = p1.matcher(command);
    Matcher m2 = p2.matcher(command);
    Matcher m3 = p3.matcher(command);

    if (m1.matches()) {
      // Extract values for Case 1
      String property = m1.group(1);
      String eventName = m1.group(2);
      LocalDateTime startDateTime = LocalDateTime.parse(m1.group(3), DATE_TIME_FORMATTER);
      LocalDateTime endDateTime = LocalDateTime.parse(m1.group(4), DATE_TIME_FORMATTER);
      String newValue = m1.group(5);

      System.out.println("Editing single event:");
      System.out.println("Property: " + property);
      System.out.println("Event Name: " + eventName);
      System.out.println("Start Time: " + startDateTime);
      System.out.println("End Time: " + endDateTime);
      System.out.println("New Value: " + newValue);
    } else if (m2.matches()) {
      // Extract values for Case 2
      String property = m2.group(1);
      String eventName = m2.group(2);
      LocalDateTime startDateTime = LocalDateTime.parse(m2.group(3), DATE_TIME_FORMATTER);
      String newValue = m2.group(4);

      System.out.println("Editing multiple events (by date & name):");
      System.out.println("Property: " + property);
      System.out.println("Event Name: " + eventName);
      System.out.println("Start Time: " + startDateTime);
      System.out.println("New Value: " + newValue);
    } else if (m3.matches()) {
      // Extract values for Case 3
      String property = m3.group(1);
      String eventName = m3.group(2);
      String newValue = m3.group(3);

      System.out.println("Editing all events with same name:");
      System.out.println("Property: " + property);
      System.out.println("Event Name: " + eventName);
      System.out.println("New Value: " + newValue);
    } else {
      throw new IllegalArgumentException("Invalid edit command format.");
    }
  }
  private static void editEvent(String command) {
      String[] parts = command.split(" ");
      Map<String, Object> eventMap = new HashMap<>();

      if (parts.length < 4) {
        throw new IllegalArgumentException("Invalid edit command format.");
      }

      boolean isMultiple = parts[1].equals("events");
      String property = parts[2];
      int nameStartIndex = 3;

      if (!isMultiple) {
        int fromIndex = Arrays.asList(parts).indexOf("from");
        int toIndex = Arrays.asList(parts).indexOf("to");
        int withIndex = Arrays.asList(parts).indexOf("with");

        if (fromIndex == -1 || toIndex == -1 || withIndex == -1 || fromIndex >= toIndex || toIndex >= withIndex) {
          throw new IllegalArgumentException("Invalid 'edit event' command format.");
        }

        String eventName = String.join(" ", Arrays.copyOfRange(parts, nameStartIndex, fromIndex)).trim();
        String startDateTime = parts[fromIndex + 1] + " " + parts[fromIndex + 2];
        String endDateTime = parts[toIndex + 1] + " " + parts[toIndex + 2];
        String newValue = String.join(" ", Arrays.copyOfRange(parts, withIndex + 1, parts.length)).trim();

        eventMap.put(EventKeys.SUBJECT, eventName);
        eventMap.put(EventKeys.START_DATETIME, startDateTime);
        eventMap.put(EventKeys.END_DATETIME, endDateTime);
        eventMap.put("property", property);
        eventMap.put("value", newValue);

      } else {
        int fromIndex = Arrays.asList(parts).indexOf("from");
        int withIndex = Arrays.asList(parts).indexOf("with");

        if (fromIndex != -1 && withIndex != -1) {
          String eventName = String.join(" ", Arrays.copyOfRange(parts, nameStartIndex, fromIndex)).trim();
          String startDateTime = parts[fromIndex + 1] + " " + parts[fromIndex + 2];
          String newValue = String.join(" ", Arrays.copyOfRange(parts, withIndex + 1, parts.length)).trim();

          eventMap.put(EventKeys.SUBJECT, eventName);
          eventMap.put(EventKeys.START_DATETIME, startDateTime);
          eventMap.put("property", property);
          eventMap.put("newValue", newValue);

        } else if (fromIndex == -1 && withIndex == -1) {
          String eventName = String.join(" ", Arrays.copyOfRange(parts, nameStartIndex, parts.length - 1)).trim();
          String newValue = parts[parts.length - 1];

          eventMap.put(EventKeys.SUBJECT, eventName);
          eventMap.put("property", property);
          eventMap.put("newValue", newValue);

        } else {
          throw new IllegalArgumentException("Invalid 'edit events' command format.");
        }
      }
    }

  private static void printEvents(String command) {
    CalendarView cv = new CalendarView();
    cv.printSingleCalenderEvent();
  }

  private static void exportCalendar(String command) {
    String[] parts  = command.split(" ");
    CalendarExport.generateCSV(parts[parts.length -1]);
  }

  private static void showStatus(String command) {
  }

public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input;

    do {
      System.out.println("Enter calender command (or 'exit' to quit):");
      input = scanner.nextLine();

      if (!input.equalsIgnoreCase("exit")) {
        System.out.println("Executing command: " + input);
        processCommand(input);
      }
    } while (!input.equalsIgnoreCase("exit"));

    System.out.println("Exiting application.");
    scanner.close();
  }
}
