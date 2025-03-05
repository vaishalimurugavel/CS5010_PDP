package calendar.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import calendar.controller.CalendarFactory;
import calendar.model.Event;

/**
 * Created at 01-03-2025
 * Author Vaishali
 **/

public class CalendarExport {
    public static void generateCSV(String fileName) {

      StringBuffer sb = new StringBuffer();
      File file = new File(fileName);
      try (FileWriter writer = new FileWriter(file)) {
        List<Event> eventList = CalendarFactory.getSingleCalender().getEventList();
        eventList.addAll(CalendarFactory.getRecurringCalender().getEventList());

        writer.append("Subject,Start Date,Start Time,End Date,End Time,All Day Event,Description,Location,Private\n");
        for (Event event : eventList) {
          sb.append(event.getSubject()).append(",");
          LocalDate date = event.getStartDateTime().toLocalDate();
          sb.append(date).append(",");
          LocalTime time = event.getStartDateTime().toLocalTime();
          sb.append(time).append(",");
          date = event.getEndDateTime().toLocalDate();
          sb.append(date).append(",");
          time = event.getEndDateTime().toLocalTime();
          sb.append(time).append(",");
          sb.append(event.isAllDay()).append(",");
          sb.append(event.getDescription()).append(",");
          sb.append(event.getLocation()).append(",");
          sb.append(event.getEventType() == 1).append("\n");

          writer.write(sb.toString());
        }
        System.out.println("CSV file created successfully: " + file.getAbsolutePath());
      } catch (IOException e) {
        System.out.println("Error while downloading " + e);
      }
    }
  }

