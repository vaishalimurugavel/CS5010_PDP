package calendar.view;

import calendar.controller.CalenderFactory;
import calendar.model.Event;

/**
 * Created at 01-03-2025
 * Author Vaishali
 **/

public class CalendarView {

  public void printSingleCalenderEvent(){
    for (Event e: CalenderFactory.getSingleCalender().getEventList() ){
      System.out.println(e);
    }
  }
}
