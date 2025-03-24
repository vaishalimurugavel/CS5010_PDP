package calendar.view;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created at 21-03-2025
 * Author Vaishali
 **/
public abstract class CalendarView {

  OutputStream output;

  public CalendarView(OutputStream output) {
    this.output = output;
  }

  /**
   * Method that out the message.
   * @param eventList List of Map of event details
   */
  abstract public void displayOutput(List<Map<String,String>> eventList) throws IOException;
  abstract public void displayOutput(String msg) throws IOException;
}
