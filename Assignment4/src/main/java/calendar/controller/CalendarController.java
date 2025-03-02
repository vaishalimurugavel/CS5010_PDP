package calendar.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import calendar.model.Event;

public class CalendarController {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  private void processCommand(String command) {
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
    if (!command.contains("from") || !command.contains("to"))
      throw new IllegalArgumentException("Invalid create event syntax.");

    String[] parts = command.split(" ");
    boolean autoDecline = Arrays.asList(parts).contains("--autoDecline");
    String eventName = parts[parts.length - 5];
    LocalDateTime start = LocalDateTime.parse(parts[parts.length - 4] + " " + parts[parts.length - 3], DATE_TIME_FORMATTER);
    LocalDateTime end = LocalDateTime.parse(parts[parts.length - 2] + " " + parts[parts.length - 1], DATE_TIME_FORMATTER);

    if (autoDecline && hasConflict(start, end)) {
      System.out.println("Event conflict detected. Auto-declining event: " + eventName);
      return;
    }

    System.out.println("Event created: " + eventName);
  }

  private static boolean hasConflict(LocalDateTime start, LocalDateTime end) {
    return false;
  }

  private static void editEvent(String command) {
  }

  private static void printEvents(String command) {;
  }

  private static void exportCalendar(String command) {
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
      }
    } while (!input.equalsIgnoreCase("exit"));

    System.out.println("Exiting application.");
    scanner.close();
  }
}
