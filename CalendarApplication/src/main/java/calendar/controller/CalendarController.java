package calendar.controller;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * Processes a given command by executing the corresponding controller command.
 * The method parses the input string, determines the appropriate command to execute,
 * and invokes the corresponding method from the command mapper.
 * </p>
 *
 * <p>
 * It handles specific cases
 * such as creating a calendar, using a calendar, or copying events separately.
 * </p>
 */
public class CalendarController {

  static Map<String, ControllerCommand> mapper = new HashMap<>();

  static {
    mapper.put("create", new ControllerCreateCommand());
    mapper.put("edit", new ControllerEditCommand());
    mapper.put("print", new PrintEventsCommand());
    mapper.put("show", new ShowStatusCommand());
    mapper.put("export", new ExportControllerCommand());
    mapper.put("copy", new ControllerGroupCommand());
  }

  /**
   * Process the CalendarApp commands one by one.
   * @param command String command
   * @throws IllegalArgumentException Exception thrown in case of invalid command.
   */
  public void processCommand(String command) throws IllegalArgumentException {
    String[] tokens = command.split(" ");
    try {
      if ( (tokens[0].equals("create") && tokens[1].equals("calendar") ) || tokens[0].equals("use")
              || tokens[0].equals("copy") || tokens[0].equals("edit")
              && tokens[1].equals("calendar") ) {
        mapper.get("copy").execute(command);
        return;
      }
      if (!mapper.containsKey(tokens[0])) {
        throw new IllegalArgumentException("Unknown command: " + command);
      }
      mapper.get(tokens[0]).execute(command);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Error while processing command " + command);
    }
  }

}
