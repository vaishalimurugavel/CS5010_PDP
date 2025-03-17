package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Calendar Event class.
 * <p>
 * An abstract class used to perform Calendar operations like add and update events.
 * </p>
 */
public abstract class CalendarEvent {

  static List<Event> eventList = new ArrayList<Event>();

  /**
   * The method checks if any event already exists in the given time period.
   *
   * @param start       starting DateTime
   * @param end         ending DateTime
   * @param allDateTime allDay DateTime
   * @param allDate     allDay Date
   * @return boolean
   */
  protected boolean checkForDuplicates(LocalDateTime start, LocalDateTime end,
                                       LocalDateTime allDateTime, LocalDate allDate) {
    for (Event e : eventList) {
      LocalDateTime start2 = e.getStartDateTime();
      LocalDateTime end2 = e.getEndDateTime();
      LocalDateTime allDateTime2 = e.getAllDateTime();
      LocalDate allDate2 = e.getAllDate();
      LocalDate repeat2 = e.getRepeatDate();
      LocalDateTime repeatDateTime2 = e.getRepeatDateTime();
      if (start2 != null && end2 != null && start != null && end != null) {
        if ((start.isBefore(end2) && start2.isBefore(end))) {
          return true;
        }
      }
      if (start != null && start.equals(start2)) {
        return true;
      }
      if (allDate != null && allDate.equals(allDate2) || allDate2 != null
              && repeat2 != null && allDate2.isBefore(repeat2)) {
        return true;
      }
      if (allDateTime != null && allDateTime.equals(allDateTime2) || allDateTime2 != null
              && allDateTime2.isBefore(repeatDateTime2)) {
        return true;
      }
    }
    return false;
  }

  /**
   * It returns the list of all the events.
   *
   * @return List of Events is returned
   */
  public static List<Event> getEventList() {
    return eventList;
  }

  /**
   * Add Event to the list of events.
   *
   * @param e event of type Event
   */
  public void addEvent(Event e) {
    eventList.add(e);
  }

  abstract public void addEvent(Map<String, Object> eventDes);

  /**
   * Edit an existing event.
   *
   * @param eventDes Details of the event to be edited.
   */
  public void editEvent(Map<String, Object> eventDes) {
    LocalDateTime start;
    LocalDateTime end;
    String eventName = (String) eventDes.get(EventKeys.SUBJECT);
    String property = (String) eventDes.get(EventKeys.PROPERTY);
    String newValue = (String) eventDes.get(EventKeys.NEW_VALUE);

    if (eventDes.containsKey(EventKeys.START_DATETIME)) {
      start = (LocalDateTime) eventDes.getOrDefault(EventKeys.START_DATETIME, null);
    } else {
      start = null;
    }
    if (eventDes.containsKey(EventKeys.END_DATETIME)) {
      end = (LocalDateTime) eventDes.getOrDefault(EventKeys.END_DATETIME, null);
    } else {
      end = null;
    }

    List<Event> filtered = null;
    if (start != null && end != null) {
      filtered = eventList.stream()
              .filter(event -> event.getSubject().equals(eventName)
                      && event.getStartDateTime().equals(start)
                      || event.getStartDateTime().isBefore(start)
                      && event.getEndDateTime().isAfter(end)
                      || event.getEndDateTime().equals(end))
              .collect(Collectors.toList());
    }
    else if (start != null) {
      filtered = eventList.stream()
              .filter(event -> event.getSubject().equals(eventName)
                      && event.getStartDateTime().equals(start))
              .collect(Collectors.toList());
    } else {
      filtered = eventList.stream()
              .filter(event -> event.getSubject().equals(eventName))
              .collect(Collectors.toList());
    }

    List<Event> updatedEvents = new ArrayList<>();
    for (Event event : filtered) {
      Event updatedEvent = updateProperty(event, property, newValue);
      updatedEvents.add(updatedEvent);
    }

    eventList.removeAll(filtered);
    eventList.addAll(updatedEvents);
  }

  private Event updateProperty(Event event, String property, String newValue) {
    Event.EventBuilder builder = new Event.EventBuilder(event.getSubject())
            .location(event.getLocation())
            .description(event.getDescription())
            .privateEvent(event.getEventType())
            .allDay(event.isAllDay())
            .allDate(event.getAllDayEnd())
            .allDateTime(event.getAllDateTime());

    switch (property.toLowerCase()) {
      case "subject":
        builder.subject(newValue);
        break;
      case "location":
        builder.location(newValue);
        break;
      case "description":
        builder.description(newValue);
        break;
      case "start_datetime":
        builder.startDateTime(LocalDateTime.parse(newValue));
        break;
      case "end_datetime":
        builder.endDateTime(LocalDateTime.parse(newValue));
        break;
      default:
        System.out.println("Unknown property: " + property);
        return event;
    }

    return builder.build();
  }


}
