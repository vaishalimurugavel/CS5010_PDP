package calendar;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Scanner;

import calendar.controller.CalendarController;


/**
 * <p>
 * Main class of the CalendarApp application.
 * This class serves as the entry point for the Calendar application,
 * allowing it to run in two different modes: interactive and headless.
 * </p>
 **/
public class CalendarApp {

  /**
   * <p>
   * Main method.
   * This method processes command-line arguments to decide the mode
   * of operation (interactive or headless).
   * </p>
   *
   * @param args  Arguments passed from the command line.
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

    switch (mode) {
      case "interactive":
        System.out.println("Entering interactive mode. Type 'exit' to quit.\nEnter command:");
        reader = new InputStreamReader(System.in);
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
    CalendarController controller = new CalendarController();
    processInput(reader,controller);
  }

  /**
   * <p>
   * Process the input commands.
   * This method continuously reads commands from the given reader (interactive or file)
   * and delegates processing of the commands to the CalendarController.
   * </p>
   *
   * @param reader      The source of input (either console or file).
   * @param controller  The controller that processes the commands.
   */
  private static void processInput(Readable reader, CalendarController controller) {
    Scanner scanner = new Scanner(reader);
    while (scanner.hasNextLine()) {
      String command = scanner.nextLine().trim();
      System.out.println("Executing command: " + command);

      if (command.equalsIgnoreCase("exit")) {
        System.out.println("Exiting...");
        break;
      }
      try {
        controller.processCommand(command);
      } catch (IllegalAccessException e) {
        System.err.println("Error! Could not process command: " + command + "\n" + e);
        scanner.close();
        System.exit(1);
      }
      System.out.println("Enter command: ");
    }

    scanner.close();
  }
}

