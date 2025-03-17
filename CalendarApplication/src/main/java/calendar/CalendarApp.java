package calendar;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Scanner;

import calendar.controller.CalendarFactory;


/**
 * Main class of the CalendarApp application.
 **/
public class CalendarApp {

  /**
   * Main method.
   * @param args  Arguments
   */
  public static void main(String[] args) {
    if (args.length < 2 || !args[0].equalsIgnoreCase("--mode")) {
      System.err.println("Usage:");
      System.err.println("  java CalendarApp --mode interactive");
      System.err.println("  java CalendarApp --mode headless <filename>");
      return;
    }

    String mode = args[1].toLowerCase(Locale.ROOT);
    Readable reader;
    boolean isInteractive = false;

    switch (mode) {
      case "interactive":
        System.out.println("Entering interactive mode. Type 'exit' to quit.\nEnter commands:");
        reader = new InputStreamReader(System.in);
        isInteractive = true;
        break;
      case "headless":
        if (args.length < 3) {
          System.err.println("Error: Missing filename for headless mode.");
          return;
        }
        try {
          reader = new FileReader(args[2]);
        } catch (FileNotFoundException e) {
          System.err.println("Error: File not found " + args[2]);
          return;
        }
        break;
      default:
        System.err.println("Invalid mode. Use 'interactive' or 'headless'.");
        return;
    }

    processInput(reader, isInteractive);
  }

  private static void processInput(Readable reader, boolean isInteractive) {
    Scanner scanner = new Scanner(reader);

    while (scanner.hasNextLine()) {
      String command = scanner.nextLine().trim();
      System.out.println("Executing command: " + command);
      if (command.equalsIgnoreCase("exit")) {
        System.out.println("Exiting...");
        break;
      }
      CalendarFactory.getCalendarController().processCommand(command);
    }

    scanner.close();
  }
}

