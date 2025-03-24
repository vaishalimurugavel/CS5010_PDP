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
 * Created at 21-03-2025
 * Author Vaishali
 **/

public class CalendarSimpleView extends CalendarView {

  public CalendarSimpleView(OutputStream output) {
    super(output);
  }

  @Override
  public void displayOutput(List<Map<String, String>> eventList) throws IOException {

    LocalDate date = null;
    LocalTime time = null;
    StringBuilder sb = new StringBuilder();
    if(eventList == null || eventList.isEmpty()) {
      System.out.println("Simple View: \n");
      output.write("No calendar event!".getBytes());
      output.close();
    }
    else{
      for (Map<String, String> content : eventList) {
        sb.append(content.get(EventKeys.SUBJECT)).append(",");
        try {
          LocalDateTime dateTime = LocalDateTime.parse(content.get(EventKeys.START_DATETIME));
          date = dateTime.toLocalDate();
          time = dateTime.toLocalTime();
        } catch (Exception e) {
          date = LocalDate.parse(content.get(EventKeys.START_DATETIME));
          time = LocalTime.parse("00:00");
        }
        sb.append(date).append(",");
        sb.append(time).append(",");
        if(content.get(EventKeys.END_DATETIME) != null) {
          try {
            LocalDateTime dateTime = LocalDateTime.parse(content.get(EventKeys.END_DATETIME));
            date = dateTime.toLocalDate();
            time = dateTime.toLocalTime();
          } catch (Exception e) {
            date = LocalDate.parse(content.get(EventKeys.END_DATETIME));
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

        System.out.println("Simple View: \n" + sb.toString());
        output.write(sb.toString().getBytes());
        output.close();
      }
    }
  }

  @Override
  public void displayOutput(String msg) throws IOException {
    output.write(msg.getBytes());
  }
}
