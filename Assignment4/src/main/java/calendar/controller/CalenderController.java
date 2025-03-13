package calendar.controller;

/**
 * Interface for Controller class.
 */
public abstract class CalenderController {

  /**
   * Process commands.
   * @param command String
   */
  public void processCommand(String command) {
    String[] tokens = command.split(" ");
    switch (tokens[0]) {
      case "create":
        createEvent(command);
        break;
      case "edit":
        editEventCommand(command);
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

  abstract void createEvent(String command);

  abstract void editEventCommand(String command);

  abstract void printEvents(String command);

  abstract void exportCalendar(String command);

  abstract void showStatus(String command);

}
