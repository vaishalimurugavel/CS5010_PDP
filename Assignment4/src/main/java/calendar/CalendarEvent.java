package calendar;

import java.util.List;

public abstract class CalendarEvent {
  List<Event> eventList;
  abstract public boolean createCalendarEvent();
}
