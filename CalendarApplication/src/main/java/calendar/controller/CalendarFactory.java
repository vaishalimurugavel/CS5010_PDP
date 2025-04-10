package calendar.controller;

import calendar.model.CalendarEvent;
import calendar.model.CalendarGroup;
import calendar.model.CalendarGroupManager;
import calendar.view.CalendarView;

/**
 * <p>
 * Factory class responsible for providing instances of various calendar-related objects.
 * It acts as a centralized manager for retrieving and setting instances of CalendarEvent,
 * CalendarView, CalendarExport, and CalendarGroup. This ensures controlled access
 * to these objects and maintains a consistent state throughout the application.
 * </p>
 *
 * <p>
 * The class uses static methods and fields, meaning it follows a singleton-like approach
 * where only one instance of each object is maintained at a time.
 * </p>
 */
public class CalendarFactory {
  private static CalendarEvent model;
  private static CalendarView view;
  private static CalendarView export;
  private static CalendarGroup group = new CalendarGroupManager();

  /**
   * Returns CalendarEvent object.
   * @return CalendarEvent
   */
  public static CalendarEvent getModel() {
    return model;
  }

  /**
   * Returns CalendarView object.
   * @return CalendarView
   */
  static CalendarView getView() {
    return view;
  }

  /**
   * Returns CalendarExport Object.
   * @return CalendarExport
   */
  static CalendarView getExport() {
    return export;
  }

  /**
   * Returns CalendarGroup Object.
   * @return CalendarGroup
   */
  public static CalendarGroup getGroup() {
    return group;
  }

  /**
   * Sets a new CalendarEvent for singleCalender.
   * @param calendarEvent CalendarEvent
   */
  public static void setModel(CalendarEvent calendarEvent) {
    model = calendarEvent;
  }

  /**
   * Sets a new CalendarView object.
   * @param cview CalendarView
   */
  public static void setView(CalendarView cview) {
    view = cview;
  }

  /**
   * Sets a new CalendarExport object.
   * @param cexport CalendarExport
   */
  public static void setExport(CalendarView cexport) {
    export = cexport;
  }

  /**
   * Sets a new CalendarGroup object.
   * @param cgroup CalendarGroup
   */
  public static void setGroup(CalendarGroup cgroup) {
    group = cgroup;
  }


}