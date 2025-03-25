package calendar.view;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Abstract class representing a calendar view.
 * This class is designed to be extended by specific view implementations
 * (e.g., simple view, export view) for displaying event data.
 * The view uses an OutputStream to output the data in a specific format.
 * </p>
 **/
public abstract class CalendarView {

  OutputStream output;

  /**
   * Constructor to initialize the OutputStream.
   *
   * @param output OutputStream where the event data will be written.
   */
  public CalendarView(OutputStream output) {
    this.output = output;
  }

  /**
   * <p>
   * Abstract method to display event data.
   * This method will be implemented by subclasses to define how the event list is displayed.
   * </p>
   *
   * @param eventList List of maps containing event details.
   * @throws IOException if there is an issue writing data to the output stream.
   */
  abstract public void displayOutput(List<Map<String, Object>> eventList) throws IOException;

  /**
   * <p>
   * Abstract method to display a custom message.
   * This method will be implemented by subclasses to define how the message is displayed.
   * </p>
   *
   * @param msg Message to be displayed.
   * @throws IOException if there is an issue writing the message to the output stream.
   */
  abstract public void displayOutput(String msg) throws IOException;
}
