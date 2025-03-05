package calendar.controller;

import calendar.model.CalendarEvent;
import calendar.model.RecurringCalendarEvent;
import calendar.model.SingleCalenderEvent;

/**
 * Created at 04-03-2025
 * Author Vaishali
 **/

public class CalendarFactory {
  private static final CalendarEvent singleCalender  = new SingleCalenderEvent();
  private static final CalendarEvent recurringCalender  = new RecurringCalendarEvent();

  public static CalendarEvent getSingleCalender() {
    return singleCalender;
  }

  public static CalendarEvent getRecurringCalender() {
    return recurringCalender;
  }
}
