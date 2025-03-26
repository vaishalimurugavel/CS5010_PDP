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
 * Export class used to export Events as CSV files.
 * This class extends CalendarView and implements the logic for exporting
 * event data into a CSV format.
 * </p>
 **/
public class CalendarExport extends CalendarView{


  /**
   * Constructor for CalendarExport that initializes the output stream.
   *
   * @param output OutputStream to which the exported CSV will be written.
   */
  public CalendarExport(OutputStream output) {
    super(output);
  }

  @Override
  public void displayOutput(List<Map<String,Object>> eventList) throws IOException {

    StringBuffer sb = new StringBuffer();
    LocalDate date = null;
    LocalTime time = null;

    sb.append("Subject,Start Date,Start Time,End Date,End Time,All Day Event," +
            "Description,Location,Private\n");
    if(eventList != null && !eventList.isEmpty()) {
      for (Map<String, Object> content : eventList) {
        sb.append(content.get(EventKeys.SUBJECT)).append(",");
        try {
          LocalDateTime dateTime = (LocalDateTime) (content.get(EventKeys.START_DATETIME));
          date = dateTime.toLocalDate();
          time = dateTime.toLocalTime();
        } catch (Exception e) {
          date = (LocalDate) (content.get(EventKeys.START_DATETIME));
          time = LocalTime.parse("00:00");
        }
        sb.append(date).append(",");
        sb.append(time).append(",");
        if(content.get(EventKeys.END_DATETIME) != null) {
          try {
            LocalDateTime dateTime = (LocalDateTime) (content.get(EventKeys.END_DATETIME));
            date = dateTime.toLocalDate();
            time = dateTime.toLocalTime();

          } catch (Exception e) {
            date = (LocalDate) (content.get(EventKeys.END_DATETIME));
            time = LocalTime.parse("00:00");

          }
          sb.append(date).append(",");
          sb.append(time).append(",");
        }
        else {
          sb.append("--").append(",");
          sb.append("--").append(",");
        }

        sb.append(content.get(EventKeys.EVENT_TYPE)).append(",");
        sb.append(content.get(EventKeys.DESCRIPTION)).append(",");
        sb.append(content.get(EventKeys.LOCATION)).append(",");
        sb.append(content.get(EventKeys.PRIVATE)).append("\n");
      }
      output.write(sb.toString().getBytes());
      output.flush();

    }
  }

  @Override
  public void displayOutput(String msg) throws IOException {
    output.write(("Export class!").getBytes());

  }

  public void close() throws IOException {
    if (output != null) {
      output.close();
    }
  }
}

