package calendar.controller;

import calendar.model.CalendarEvent;
import calendar.model.RecurringCalendarEvent;
import calendar.model.SingleCalenderEvent;
import calendar.view.CalendarExport;
import calendar.view.CalendarView;


/**
 * Factory class to get instance of classes.
 */
public class CalendarFactory {
  private static final CalendarEvent singleCalender  = new SingleCalenderEvent();
  private static final CalendarEvent recurringCalender  = new RecurringCalendarEvent();
  private static final CalendarView calendarView = new CalendarView();
  private static final CalendarExport calendarExport = new CalendarExport();
  private static final CalenderController calendarControl = new CalendarControllerImpl();

  /**
   * Returns CalenderController object.
   * @return  CalenderController
   */
  public static CalenderController getCalendarController() {
    return calendarControl;
  }

  /**
   * Returns CalendarEvent object.
   * @return CalendarEvent
   */
  public static CalendarEvent getSingleCalender() {
    return singleCalender;
  }

  /**
   * Returns CalendarEvent object.
   * @return CalendarEvent
   */
  public static CalendarEvent getRecurringCalender() {
    return recurringCalender;
  }

  /**
   * Returns CalendarView object.
   * @return CalendarView
   */
  public static CalendarView getCalendarView() {
    return calendarView;
  }

  /**
   * Returns CalendarExport Object.
   * @return CalendarExport
   */
  public static CalendarExport getCalendarExport() {
    return calendarExport;
  }
}
