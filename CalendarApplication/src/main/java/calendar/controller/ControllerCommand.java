package calendar.controller;

/**
 * <p>
 * Interface to define a contract for processing different types of commands.
 * Implementing classes must provide an implementation for the execute method,
 * which will handle specific command actions.
 * </p>
 *
 * <p>
 * This allows for a flexible and extensible command processing system, where
 * new commands can be introduced by simply implementing this interface.
 * </p>
 */
public interface ControllerCommand {

  /**
   * Execute method that needs to be implemented to perform any necessary actions.
   * @param command command String
   * @throws IllegalAccessException  Any error during any of the operation
   */
  void execute(String command) throws IllegalAccessException;
}
