package calendar.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import calendar.model.Event;
import calendar.view.CalendarExport;
import calendar.view.CalendarView;


public class CalendarController {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
    if (!command.contains("from") || !command.contains("to")) {
      throw new IllegalArgumentException("Invalid create event syntax.");
    }

    String[] parts = command.split(" ");
    boolean autoDecline = Arrays.asList(parts).contains("--autoDecline");

    // Find indices of 'from' and 'to'
    int fromIndex = Arrays.asList(parts).indexOf("from");
    int toIndex = Arrays.asList(parts).indexOf("to");

    System.out.println(fromIndex + " " + toIndex + " " + parts.length + " " + Arrays.toString(parts));
    if (fromIndex == -1 || toIndex == -1 || fromIndex + 2 >= parts.length || toIndex + 2 >= parts.length) {
      throw new IllegalArgumentException("Invalid command format for event creation.");
    }

    int eventNameStartIndex = autoDecline ? 3 : 2;
    String eventName = String.join(" ", Arrays.copyOfRange(parts, eventNameStartIndex, fromIndex)).trim();

    // Parse dates
    String startDateTime = parts[fromIndex + 1] + " " + parts[fromIndex + 2];
    String endDateTime = parts[toIndex + 1] + " " + parts[toIndex + 2];

    LocalDateTime start = LocalDateTime.parse(startDateTime, DATE_TIME_FORMATTER);
    LocalDateTime end = LocalDateTime.parse(endDateTime, DATE_TIME_FORMATTER);

    if (autoDecline && hasConflict(start, end)) {
      System.out.println("Event conflict detected. Auto-declining event: " + eventName);
      System.out.println("Start time: " + start.format(DATE_TIME_FORMATTER));
      System.out.println("End time: " + end.format(DATE_TIME_FORMATTER));
      return;
    }
    Map<String, Object> eventMap = new HashMap<>();
    eventMap.put(Event.EventKeys.SUBJECT, eventName);
    eventMap.put(Event.EventKeys.DESCRIPTION, eventName);
    eventMap.put(Event.EventKeys.START_DATETIME, start);
    eventMap.put(Event.EventKeys.END_DATETIME, end);
    eventMap.put(Event.EventKeys.EVENT_TYPE,1);
    boolean res = CalendarFactory.getSingleCalender().addEvent(eventMap);
    System.out.println("Event created: " + eventName);
    System.out.println("Event Status: " + res);
  }

  private static boolean hasConflict(LocalDateTime start, LocalDateTime end) {
    return false;
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

        eventMap.put(Event.EventKeys.SUBJECT, eventName);
        eventMap.put(Event.EventKeys.START_DATETIME, startDateTime);
        eventMap.put(Event.EventKeys.END_DATETIME, endDateTime);
        eventMap.put("property", property);
        eventMap.put("value", newValue);

      } else {
        int fromIndex = Arrays.asList(parts).indexOf("from");
        int withIndex = Arrays.asList(parts).indexOf("with");

        if (fromIndex != -1 && withIndex != -1) {
          String eventName = String.join(" ", Arrays.copyOfRange(parts, nameStartIndex, fromIndex)).trim();
          String startDateTime = parts[fromIndex + 1] + " " + parts[fromIndex + 2];
          String newValue = String.join(" ", Arrays.copyOfRange(parts, withIndex + 1, parts.length)).trim();

          eventMap.put(Event.EventKeys.SUBJECT, eventName);
          eventMap.put(Event.EventKeys.START_DATETIME, startDateTime);
          eventMap.put("property", property);
          eventMap.put("newValue", newValue);

        } else if (fromIndex == -1 && withIndex == -1) {
          String eventName = String.join(" ", Arrays.copyOfRange(parts, nameStartIndex, parts.length - 1)).trim();
          String newValue = parts[parts.length - 1];

          eventMap.put(Event.EventKeys.SUBJECT, eventName);
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
