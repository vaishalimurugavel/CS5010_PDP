package calendar;

import static org.junit.Assert.*;
import org.junit.Test;

import calendar.model.CalendarEvent;
import calendar.view.CalendarExport;
import calendar.view.CalendarView;
import calendar.controller.CalendarFactory;

public class CalendarFactoryTest {

  @Test
  public void testGetSingleCalender() {
    CalendarEvent singleCalendar = CalendarFactory.getSingleCalender();
    assertNotNull(singleCalendar);
  }

  @Test
  public void testGetRecurringCalender() {
    CalendarEvent recurringCalendar = CalendarFactory.getRecurringCalender();
    assertNotNull(recurringCalendar);
  }

  @Test
  public void testGetCalendarView() {
    CalendarView view = CalendarFactory.getCalendarView();
    assertNotNull(view);
  }

  @Test
  public void testGetCalendarExport() {
    CalendarExport export = CalendarFactory.getCalendarExport();
    assertNotNull(export);
  }
}
