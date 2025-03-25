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

  public void processCommand(String command) throws IllegalAccessException {
    String[] tokens = command.split(" ");
    try {
      if ( (tokens[0].equals("create") && tokens[1].equals("calendar") ) || tokens[0].equals("use")
              || tokens[0].equals("copy")) {
        mapper.get("copy").execute(command);
      }
      mapper.get(tokens[0]).execute(command);
    } catch (IllegalAccessException e) {
      throw new IllegalAccessException("Error while processing command " + command);
    }
  }

}
