package calendar.view;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import calendar.model.EventKeys;

/**
 * <p>
 * Simple view for displaying calendar events.
 * This class extends CalendarView and displays event data in a simplified format.
 * If no events are present, it shows a message indicating no events.
 * </p>
 **/
public class CalendarSimpleView extends CalendarView {

  /**
   * Constructor for CalendarSimpleView that initializes the output stream.
   *
   * @param output OutputStream where the event data will be written.
   */
  public CalendarSimpleView(OutputStream output) {
    super(output);
  }

  @Override
  public void displayOutput(List<Map<String, Object>> eventList) throws IOException {

    LocalDate date = null;
    LocalTime time = null;
    StringBuilder sb = new StringBuilder();
    if(eventList == null || eventList.isEmpty()) {
      output.write("No calendar event!".getBytes());
    }
    else{
      for (Map<String, Object> content : eventList) {
        sb.append(content.get(EventKeys.SUBJECT)).append(",");
        try {
          LocalDateTime dateTime = (LocalDateTime) content.get(EventKeys.START_DATETIME);
          date = dateTime.toLocalDate();
          time = dateTime.toLocalTime();
        } catch (Exception e) {
          date = (LocalDate) content.get(EventKeys.START_DATETIME);
          time = LocalTime.parse("00:00");
        }
        sb.append(date).append(",");
        sb.append(time).append(",");
        if(content.get(EventKeys.END_DATETIME) != null) {
          try {
            LocalDateTime dateTime = (LocalDateTime) content.get(EventKeys.END_DATETIME);
            date = dateTime.toLocalDate();
            time = dateTime.toLocalTime();
          } catch (Exception e) {
            date = (LocalDate) (content.get(EventKeys.END_DATETIME));
            time = LocalTime.parse("00:00");

            sb.append(date).append(",");
            sb.append(time).append(",");
          }
        }
        else {
          sb.append("--").append(",");
          sb.append("--").append(",");
        }

        sb.append(date).append(",");
        sb.append(time).append(",");
        sb.append(content.get(EventKeys.EVENT_TYPE)).append(",");
        sb.append(content.get(EventKeys.DESCRIPTION)).append(",");
        sb.append(content.get(EventKeys.LOCATION)).append(",");
        sb.append(content.get(EventKeys.PRIVATE)).append("\n");

        output.write(sb.toString().getBytes());
      }
    }
  }

  @Override
  public void displayOutput(String msg) throws IOException {
    output.write(msg.getBytes());
  }
}
