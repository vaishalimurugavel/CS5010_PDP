package calendar.view;

import calendar.controller.CalendarFactory;
import calendar.model.Event;

/**
 * Created at 01-03-2025
 * Author Vaishali
 **/

public class CalendarView {

  public void printSingleCalenderEvent(){
    for (Event e: CalendarFactory.getSingleCalender().getEventList() ){
      System.out.println(e);
    }
  }
}
