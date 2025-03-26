package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  Manages calendar events, including adding, updating, checking for duplicates, and retrieving events for display.
 **/
public class CalenderEventManager implements CalendarEvent {
  List<Event> events = new ArrayList<Event>();

  private void addEvents(Map<String , Object> event, Event.EventBuilder eventBuilder) {
    if(event.containsKey(EventKeys.START_DATETIME)){
      eventBuilder.startDateTime((LocalDateTime) event.get(EventKeys.START_DATETIME));
    }
    if(event.containsKey(EventKeys.END_DATETIME)){
      eventBuilder.endDateTime((LocalDateTime) event.get(EventKeys.END_DATETIME));
    }
    if(event.containsKey(EventKeys.DESCRIPTION)){
      eventBuilder.description((String) event.get(EventKeys.DESCRIPTION));
    }
    if(event.containsKey(EventKeys.LOCATION)){
      eventBuilder.location((String) event.get(EventKeys.LOCATION));
    }
    if(event.containsKey(EventKeys.REPEAT_DATE)){
      eventBuilder.repeatDate((LocalDate) event.get(EventKeys.REPEAT_DATE));
    }
    if(event.containsKey(EventKeys.REPEAT_DATETIME)){
      eventBuilder.repeatDateTime((LocalDateTime) event.get(EventKeys.REPEAT_DATETIME));
    }
    if(event.containsKey(EventKeys.ALLDAY_DATE)){
      eventBuilder.allDate((LocalDate) event.get(EventKeys.ALLDAY_DATE));
    }
    if(event.containsKey(EventKeys.WEEKDAYS)){
      eventBuilder.weekdays((String) event.get(EventKeys.WEEKDAYS));
    }
    if(event.containsKey(EventKeys.OCCURRENCES)){
      eventBuilder.occurrences((int) event.get(EventKeys.OCCURRENCES));
    }
    if(event.containsKey(EventKeys.PRIVATE)){
      eventBuilder.privateEvent(1);
    }
    if(event.containsKey(EventKeys.PRIVATE)){
      eventBuilder.privateEvent(1);
    }
    else {
      eventBuilder.privateEvent(0);
    }
    eventBuilder.allDay(event.get(EventKeys.EVENT_TYPE).equals(EventKeys.EventType.ALL_DAY)
            || event.get(EventKeys.EVENT_TYPE).equals(EventKeys.EventType.ALL_DAY_RECURRING));
  }

  @Override
  public boolean checkForDuplicates(Event event) {
      if (event == null || events == null) {
        return false;
      }

      LocalDateTime start = event.getStartDateTime();
      LocalDate allDate = event.getAllDate();
      LocalDate repeat = event.getRepeatDate();
      LocalDateTime repeatDt = event.getRepeatDateTime();

      for (Event e : events) {
        if (e == null) {
          continue;
        }

        LocalDateTime start2 = e.getStartDateTime();
        LocalDateTime end2 = e.getEndDateTime();
        LocalDate allDate2 = e.getAllDate();
        LocalDate repeat2 = e.getRepeatDate();
        LocalDateTime repeatDt2 = e.getRepeatDateTime();

        if ((end2 != null && start != null && (end2.isAfter(start) || end2.isEqual(start)))
                || (start2 != null && start != null && start2.isEqual(start))
                || (allDate2 != null && allDate != null && (allDate2.isAfter(allDate)
                || allDate2.isEqual(allDate)))
                || (repeat != null && allDate2 != null && (allDate2.isEqual(repeat)
                || allDate2.isAfter(allDate) && allDate2.isBefore(repeat)))
                || (repeat != null && repeat2 != null && repeat2.isEqual(repeat))
                || (repeatDt != null && repeatDt2 != null && (repeatDt2.isAfter(repeatDt)
                || repeatDt2.isEqual(repeatDt)))
                || (repeat2 != null && allDate != null && repeat2.isEqual(allDate))
                || (repeatDt != null && start2 != null && (start2.isAfter(repeatDt)
                || start2.isEqual(repeatDt)))) {
          return true;
        }
      }
      return false;
    }


    @Override
  public void addEvent(Map<String, Object> eventMap) {
    String subject = eventMap.get(EventKeys.SUBJECT).toString();
    Event.EventBuilder eventNew = new Event.EventBuilder(subject);
    addEvents(eventMap, eventNew);
    Event event = eventNew.build();
    if(checkForDuplicates(event)){
      throw new IllegalArgumentException("Calender shows busy!");
    }
    else {
      events.add(event);
    }

  }

  @Override
  public void updateEvent(Map<String, Object> eventMap) {
    LocalDateTime start;
    LocalDateTime end;
    String eventName = (String) eventMap.get(EventKeys.SUBJECT);
    String property = (String) eventMap.get(EventKeys.PROPERTY);
    String newValue = (String) eventMap.get(EventKeys.NEW_VALUE);

    if (eventMap.containsKey(EventKeys.START_DATETIME)) {
      start = (LocalDateTime) eventMap.getOrDefault(EventKeys.START_DATETIME, null);
    } else {
      start = null;
    }
    if (eventMap.containsKey(EventKeys.END_DATETIME)) {
      end = (LocalDateTime) eventMap.getOrDefault(EventKeys.END_DATETIME, null);
    } else {
      end = null;
    }

    List<Event> filtered = null;
    if (start != null && end != null) {
      filtered = events.stream()
              .filter(event -> event.getSubject() != null
                      && event.getSubject().equals(eventName)
                      && event.getStartDateTime() != null
                      && event.getStartDateTime().equals(start)
                      || event.getStartDateTime() != null
                      && event.getStartDateTime().isBefore(start)
                      && event.getEndDateTime() != null && event.getEndDateTime().isAfter(end)
                      || event.getEndDateTime() != null && event.getEndDateTime().equals(end))
              .collect(Collectors.toList());
    } else if (start != null) {
      filtered = events.stream()
              .filter(event -> event.getSubject() != null
                      && event.getSubject().equals(eventName)
                      && event.getStartDateTime() != null
                      && event.getStartDateTime().equals(start))
              .collect(Collectors.toList());
    } else {
      filtered = events.stream()
              .filter(event -> event.getSubject() != null
                      && event.getSubject().equals(eventName))
              .collect(Collectors.toList());
    }

    List<Event> updatedEvents = new ArrayList<>();
    for (Event event : filtered) {
      Event updatedEvent = updateProperty(event, property, newValue);
      updatedEvents.add(updatedEvent);
    }

    events.removeAll(filtered);
    events.addAll(updatedEvents);
  }


  private Event updateProperty(Event event, String property, String newValue) {
    Event.EventBuilder builder = new Event.EventBuilder(event.getSubject())
            .location(event.getLocation())
            .description(event.getDescription())
            .privateEvent(event.getEventType())
            .allDay(event.isAllDay())
            .allDate(event.getAllDayEnd());

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
        System.err.println("Unknown property: " + property);
        return event;
    }

    return builder.build();
  }

  private List<Map<String,Object>> getEventDetails(List<Event> events) {
    List<Map<String, Object>> eventList = new ArrayList<>();
    Map<String, Object> eventDetails;
    for (Event event : events) {
      eventDetails = new HashMap<>();
      eventDetails.put(EventKeys.SUBJECT, event.getSubject());
      LocalDateTime startDateTime = event.getStartDateTime();
      if(startDateTime != null) {
        eventDetails.put(EventKeys.START_DATETIME, startDateTime);
      } else {
        LocalDate allDate = event.getAllDayEnd();
        if(allDate != null) {
          eventDetails.put(EventKeys.START_DATETIME, allDate);
        }
      }
      LocalDateTime endDateTime = event.getEndDateTime();
      if(endDateTime != null) {
        eventDetails.put(EventKeys.END_DATETIME, endDateTime);
      } else if (event.getRepeatDateTime() != null) {
        eventDetails.put(EventKeys.END_DATETIME, event.getRepeatDateTime());
      } else {
        LocalDate repDate = event.getRepeatDate();
        if(repDate != null) {
          eventDetails.put(EventKeys.END_DATETIME, repDate);
        }
      }
      eventDetails.put(EventKeys.LOCATION, event.getLocation());
      eventDetails.put(EventKeys.EVENT_TYPE, String.valueOf(event.isAllDay()));
      eventDetails.put(EventKeys.DESCRIPTION, event.getDescription());
      eventDetails.put(EventKeys.PRIVATE,event.getEventType() == 1 ? "true" : "false");
      eventList.add(eventDetails);
    }
    return eventList;
  }

  @Override
  public List<Map<String,Object>> getEventsForDisplay() {
    return getEventDetails(events);
  }

  @Override
  public List<Map<String,Object>> getEventForDisplay(String subject) {
    List<Event> eventList = events.stream().filter(e -> e.getSubject().equals(subject))
            .collect(Collectors.toList());
    return getEventDetails(eventList);
  }

  @Override
  public List<Map<String,Object>> getEventForDisplay(LocalDateTime startTime, LocalDateTime endTime) {
    if(startTime == null && endTime == null) {
      return null;
    }
    LocalDate allDate = (startTime != null) ? startTime.toLocalDate() : null;
    LocalDate repeat = (endTime != null) ? endTime.toLocalDate() : null;

    List<Event> eventList = events.stream()
            .filter(e -> e.getStartDateTime() != null || e.getEndDateTime() != null)
            .filter(e ->
                    (e.getStartDateTime() != null && (e.getStartDateTime().isAfter(startTime)
                            || e.getStartDateTime().isEqual(startTime)))
                            || (e.getEndDateTime() != null && (endTime == null
                            || e.getEndDateTime().isBefore(endTime)
                            || e.getEndDateTime().isEqual(endTime)))
                            || (e.getAllDate() != null && (allDate != null
                            && (e.getAllDate().isAfter(allDate)
                            || e.getAllDate().equals(allDate))))
                            || (e.getRepeatDate() != null && (repeat != null
                            && (e.getRepeatDate().isAfter(repeat)
                            || e.getRepeatDate().equals(repeat))))
                            || (e.getRepeatDateTime() != null && (startTime != null
                            && (e.getRepeatDateTime().isAfter(startTime)
                            || e.getRepeatDateTime().equals(startTime))))
            )
            .collect(Collectors.toList());
    return getEventDetails(eventList);
  }


  @Override
  public List<Map<String,Object>> getEventForDisplay(LocalDate startDate, LocalDate endDate) {
    if(startDate == null && endDate == null) {
      return null;
    }
    return null;
  }

  @Override
  public List<Map<String,Object>> getUserStatus(LocalDateTime date) {

    List<Event> eventList = events.stream()
            .filter(e->((e.getStartDateTime() != null && e.getEndDateTime() != null )
                    && ( date.equals(e.getStartDateTime() ) || date.isEqual(e.getEndDateTime())
            || (date.isAfter(e.getStartDateTime() ) && date.isBefore(e.getEndDateTime()))))
            || e.getRepeatDateTime() != null && e.getRepeatDateTime().isEqual(date))
            .collect(Collectors.toList());

    return getEventDetails(eventList);
  }

}
