package calendar.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import calendar.model.CalendarEvent;
import calendar.model.Event;

/**
 * Export class used to export Events as CSV files.
 **/
public class CalendarExport {

  /**
   * Generates a CSV file adding all the Events available in the CalendarEvent.
   * @param fileName Filepath.
   */
  public void generateCSV(String fileName) {

    StringBuffer sb = new StringBuffer();
    File file = new File(fileName);
    String temp;
    try (FileWriter writer = new FileWriter(file)) {
      List<Event> eventList = CalendarEvent.getEventList();

      writer.append("Subject,Start Date,Start Time,End Date,End Time,All Day Event," +
              "Description,Location,Private\n");
      for (Event event : eventList) {
        sb.append(event.getSubject()).append(",");
        LocalDateTime date = event.getStartDateTime();
        if (date == null) {
          LocalDateTime allDateTime = event.getAllDateTime();
          LocalDate allDate = event.getAllDate();
          if (allDateTime != null) {
            date = allDateTime;
            temp = date.toLocalDate().toString();
            sb.append(temp).append(",");

            temp = date.toLocalTime().toString();
            sb.append(temp).append(",");
          } else if (allDate != null) {
            temp = allDate.toString();
            sb.append(temp).append(",");
            temp = "00:00";
            sb.append(temp).append(",");
          }
        } else {
          temp = date.toLocalDate().toString();
          sb.append(temp).append(",");

          temp = date.toLocalTime().toString();
          sb.append(temp).append(",");
        }
        date = event.getEndDateTime();
        if (date == null) {
          LocalDateTime repeatDateTime = event.getRepeatDateTime();
          LocalDate repeatDate = event.getRepeatDate();
          if (repeatDateTime != null) {
            date = repeatDateTime;
            temp = date.toLocalDate().toString();
            sb.append(temp).append(",");

            temp = date.toLocalTime().toString();
            sb.append(temp).append(",");
          } else if (repeatDate != null) {
            temp = repeatDate.toString();
            sb.append(temp).append(",");
            temp = "00:00";
            sb.append(temp).append(",");
          } else {
            sb.append("").append(",");
            sb.append("").append(",");
          }
        } else {
          temp = date.toLocalDate().toString();
          sb.append(temp).append(",");

          temp = date.toLocalTime().toString();
          sb.append(temp).append(",");
        }
        sb.append(event.isAllDay()).append(",");
        sb.append(event.getDescription()).append(",");
        sb.append(event.getLocation()).append(",");
        sb.append(event.getEventType() == 1).append("\n");
      }
      writer.write(sb.toString());
      System.out.println("CSV file created successfully: " + file.getAbsolutePath());
    } catch (IOException e) {
      System.out.println("Error while downloading " + e);
    }
  }
}

