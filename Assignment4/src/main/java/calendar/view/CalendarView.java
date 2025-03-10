package calendar.view;

import calendar.model.CalendarEvent;
import calendar.model.Event;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CalendarView class for displaying calendar events.
 * Created at 01-03-2025
 * Author Vaishali
 **/
public class CalendarView {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  // Print all events on a specific date
  public void printEventsOn(String dateString) {
    LocalDate date = LocalDate.parse(dateString, DATE_FORMAT);
    List<Event> events = CalendarEvent.getEventList()
            .stream()
            .filter(e -> e.getStartDateTime() != null)
            .filter(e -> e.getStartDateTime().toLocalDate().equals(date))  // Compare only the date part
            .collect(Collectors.toList());

    if (events.isEmpty()) {
      System.out.println("No events scheduled on " + dateString);
    } else {
      System.out.println("Events on " + dateString + ":");
      for (Event e : events) {
        System.out.println("- " + e.getSubject() + " (" + e.getStartDateTime().format(DATE_TIME_FORMAT) + " - "
                + e.getEndDateTime().format(DATE_TIME_FORMAT) + ") at " + e.getLocation());
      }
    }
  }


  // Print events in a given time range
  public void printEventsFromTo(String startDateTimeStr, String endDateTimeStr) {
    LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeStr, DATE_TIME_FORMAT);
    LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeStr, DATE_TIME_FORMAT);

    List<Event> events = CalendarEvent.getEventList()
            .stream()
            .filter(e -> e.getStartDateTime() != null && e.getEndDateTime() != null)  // Add null check
            .filter(e -> (e.getStartDateTime().isAfter(startDateTime) || e.getStartDateTime().isEqual(startDateTime))
                    && (e.getEndDateTime().isBefore(endDateTime) || e.getEndDateTime().isEqual(endDateTime)))
            .collect(Collectors.toList());

    if (events.isEmpty()) {
      System.out.println("No events scheduled in the given time range.");
    } else {
      System.out.println("Events from " + startDateTimeStr + " to " + endDateTimeStr + ":");
      for (Event e : events) {
        System.out.println("- " + e.getSubject() + " (" + e.getStartDateTime().format(DATE_TIME_FORMAT) + " - "
                + e.getEndDateTime().format(DATE_TIME_FORMAT) + ") at " + e.getLocation());
      }
    }
  }

  // Show busy status on a given date-time
  public void showStatusOn(String dateTimeStr) {
    LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMAT);
    boolean isBusy = CalendarEvent.getEventList()
            .stream()
            .filter(e -> e.getStartDateTime() != null && e.getEndDateTime() != null)
            .anyMatch(e -> (e.getStartDateTime().isBefore(dateTime) || e.getStartDateTime().isEqual(dateTime))
                    && (e.getEndDateTime().isAfter(dateTime) || e.getEndDateTime().isEqual(dateTime)));

    if (isBusy) {
      System.out.println("Status: Busy at " + dateTimeStr);
    } else {
      System.out.println("Status: Available at " + dateTimeStr);
    }
  }
}