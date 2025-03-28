package calendar.view;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Mock class for CalendarView.
 */
public class MockView extends CalendarView {

  private String viewName;
  private StringBuilder output;

  public MockView(String viewName) {
    super(System.out);
    this.viewName = viewName;
  }

  public MockView(OutputStream output) {
    super(output);
    this.output = new StringBuilder();
  }

  public String getViewName() {
    return viewName;
  }

  @Override
  public void displayOutput(List<Map<String, Object>> eventList) throws IOException {
    if (eventList != null && !eventList.isEmpty()) {
      for (Map<String, Object> event : eventList) {
        output.append("Event: ").append(event.get("subject")).append("\n");
      }
    } else {
      output.append("No events available\n");
    }
  }

  @Override
  public void displayOutput(String msg) throws IOException {
    output.append(msg).append("\n");
  }

  public String getOutput() {
    return output.toString();
  }
}
