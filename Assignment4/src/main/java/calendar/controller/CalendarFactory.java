package calendar.controller;

import calendar.model.CalendarEvent;
import calendar.model.RecurringCalendarEvent;
import calendar.model.SingleCalenderEvent;
import calendar.view.CalendarExport;
import calendar.view.CalendarView;

/**
 * Created at 04-03-2025
 * Author Vaishali
 **/

public class CalendarFactory {
  private static final CalendarEvent singleCalender  = new SingleCalenderEvent();
  private static final CalendarEvent recurringCalender  = new RecurringCalendarEvent();
  private static final CalendarView calendarView = new CalendarView();
  private static final CalendarExport calendarExport = new CalendarExport();

  public static CalendarEvent getSingleCalender() {
    return singleCalender;
  }

  public static CalendarEvent getRecurringCalender() {
    return recurringCalender;
  }

  public static CalendarView getCalendarView() {
    return calendarView;
  }
  public static CalendarExport getCalendarExport() {
    return calendarExport;
  }
}
