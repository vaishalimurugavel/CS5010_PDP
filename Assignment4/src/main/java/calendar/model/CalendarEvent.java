package calendar.model;

import java.util.List;

public abstract class CalendarEvent {

  List<Event> eventList;
  abstract public boolean addEvent();
}
