package calendar.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.model.EventKeys;

public class CalendarController {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private static void processCommand(String command) {
    String[] tokens = command.split(" ");
    if (tokens.length < 2) throw new IllegalArgumentException("Invalid command format.");

    switch (tokens[0]) {
      case "create":
        createEvent(command);
        break;
      case "edit":
        editEventCommand(command);
        break;
      case "print":
        printEvents(command);
        break;
      case "export":
        exportCalendar(command);
        break;
      case "show":
        showStatus(command);
        break;
      default:
        throw new IllegalArgumentException("Unknown command: " + tokens[0]);
    }
  }

  private static boolean checkValidDate(String startDate, String endDate) {
    String datePattern = "\\d{4}-\\d{2}-\\d{2}";
    Pattern pattern = Pattern.compile(datePattern);
    Matcher startmatcher = pattern.matcher(startDate);
    Matcher endMatcher = pattern.matcher(endDate);
    String datePattern2 = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";
    Pattern pattern2 = Pattern.compile(datePattern2);
    Matcher startmatcher2 = pattern2.matcher(startDate);
    Matcher endMatcher2 = pattern2.matcher(endDate);


    if(startmatcher.matches() && endMatcher.matches()) {
      LocalDate startTime = LocalDate.parse(startDate, DATE_FORMATTER);
      LocalDate endTime = LocalDate.parse(endDate, DATE_FORMATTER);
      return startTime.isBefore(endTime);
    }
    else if(startmatcher2.matches() && endMatcher2.matches()) {
      LocalDate startTime = LocalDate.parse(startDate, DATE_TIME_FORMATTER);
      LocalDate endTime = LocalDate.parse(endDate, DATE_TIME_FORMATTER);
      return startTime.isBefore(endTime);
    }
    return false;
  }

  private static void createEvent(String command) {

    Map<String, Object> eventDetails = new HashMap<>();

    String basePattern = "create event( --autoDecline)? (.+?)";
    String otherInfo = "( location (.+?))?( description (.+?))?( (public|private))?";
    String singleAllDay = basePattern + " on (\\d{4}-\\d{2}-\\d{2})" + otherInfo;
    String singleAllDateTime = basePattern + " on (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})" + otherInfo;
    String singleEventPattern = basePattern + " from (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})" + otherInfo;
    String recurringForNTimesPattern = singleEventPattern + " repeats ([MTWRFSU]+) for (\\d+) times" + otherInfo;
    String recurringUntilPattern = singleEventPattern + " repeats ([MTWRFSU]+) until (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})" + otherInfo;
    String allDayEventPattern = basePattern + " on (\\d{4}-\\d{2}-\\d{2})" + otherInfo;
    String allDayRecurringForNTimesPattern = allDayEventPattern + " repeats ([MTWRFSU]+) for (\\d+) times" + otherInfo;
    String allDayRecurringUntilPattern = allDayEventPattern + " repeats ([MTWRFSU]+) until (\\d{4}-\\d{2}-\\d{2})" + otherInfo;

    Pattern pSingle = Pattern.compile(singleEventPattern);
    Pattern pSingleAllDay = Pattern.compile(singleAllDay);
    Pattern pSingleAllDateTime = Pattern.compile(singleAllDateTime);
    Pattern pRecurringForN = Pattern.compile(recurringForNTimesPattern);
    Pattern pRecurringUntil = Pattern.compile(recurringUntilPattern);
    Pattern pAllDay = Pattern.compile(allDayEventPattern);
    Pattern pAllDayRecurringForN = Pattern.compile(allDayRecurringForNTimesPattern);
    Pattern pAllDayRecurringUntil = Pattern.compile(allDayRecurringUntilPattern);

    Matcher mSingle = pSingle.matcher(command);
    Matcher mSingleAllDay = pSingleAllDay.matcher(command);
    Matcher mSingleAllDateTime = pSingleAllDateTime.matcher(command);
    Matcher mRecurringForN = pRecurringForN.matcher(command);
    Matcher mRecurringUntil = pRecurringUntil.matcher(command);
    Matcher mAllDay = pAllDay.matcher(command);
    Matcher mAllDayRecurringForN = pAllDayRecurringForN.matcher(command);
    Matcher mAllDayRecurringUntil = pAllDayRecurringUntil.matcher(command);

    if (mRecurringForN.matches()) {
      if(!checkValidDate(mRecurringForN.group(3), mRecurringForN.group(4))) {
        throw new IllegalArgumentException("Invalid date format.");
      }
      System.out.println(mRecurringForN.group(6)  );
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.RECURRING);
      eventDetails.put(EventKeys.AUTO_DECLINE, mRecurringForN.group(1) != null);
      eventDetails.put(EventKeys.SUBJECT, mRecurringForN.group(2));
      eventDetails.put(EventKeys.START_DATETIME, LocalDateTime.parse(mRecurringForN.group(3), DATE_TIME_FORMATTER));
      eventDetails.put(EventKeys.END_DATETIME, LocalDateTime.parse(mRecurringForN.group(4), DATE_TIME_FORMATTER));
      eventDetails.put(EventKeys.WEEKDAYS, mRecurringForN.group(5));
      eventDetails.put(EventKeys.OCCURRENCES, Integer.parseInt(mRecurringForN.group(6)));
      String otherinfo = null;
      boolean flag = mRecurringForN.group(4) != null;
      if(flag ) {
        otherinfo = mRecurringForN.group(5);
        eventDetails.put(EventKeys.LOCATION, otherinfo);
      }
      flag = mRecurringForN.group(6) != null;
      if(flag) {
        otherinfo = mRecurringForN.group(7);
        eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
      }
      otherinfo = mRecurringForN.group(8);
      if (otherinfo != null && otherinfo.equals("private")) {
        eventDetails.put(EventKeys.PRIVATE, 1);
      }
      CalendarFactory.getRecurringCalender().addEvent(eventDetails);
    } else if (mRecurringUntil.matches()) {

      if(!checkValidDate(mRecurringUntil.group(3), mRecurringUntil.group(4))) {
        throw new IllegalArgumentException("Invalid date format.");
      }
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.RECURRING);
      eventDetails.put(EventKeys.AUTO_DECLINE, mRecurringUntil.group(1) != null);
      eventDetails.put(EventKeys.SUBJECT, mRecurringUntil.group(2));
      eventDetails.put(EventKeys.START_DATETIME, LocalDateTime.parse(mRecurringUntil.group(3), DATE_TIME_FORMATTER));
      eventDetails.put(EventKeys.END_DATETIME, LocalDateTime.parse(mRecurringUntil.group(4), DATE_TIME_FORMATTER));
      eventDetails.put(EventKeys.WEEKDAYS, mRecurringUntil.group(5));
      eventDetails.put(EventKeys.REPEAT_DATETIME, LocalDateTime.parse(mRecurringUntil.group(6), DATE_TIME_FORMATTER));
      String otherinfo = null;
      boolean flag = mRecurringUntil.group(4) != null;
      if(flag ) {
        otherinfo = mRecurringUntil.group(5);
        eventDetails.put(EventKeys.LOCATION, otherinfo);
      }
      flag = mRecurringUntil.group(6) != null;
      if(flag) {
        otherinfo = mRecurringUntil.group(7);
        eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
      }
      otherinfo = mRecurringUntil.group(8);
      if (otherinfo != null && otherinfo.equals("private")) {
        eventDetails.put(EventKeys.PRIVATE, 1);
      }
      CalendarFactory.getRecurringCalender().addEvent(eventDetails);
    } else if (mSingle.matches()) {
      if(!checkValidDate(mSingle.group(3), mSingle.group(4))) {
        throw new IllegalArgumentException("Invalid date format.");
      }
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);
      eventDetails.put(EventKeys.AUTO_DECLINE, mSingle.group(1) != null);
      eventDetails.put(EventKeys.SUBJECT, mSingle.group(2));
      eventDetails.put(EventKeys.START_DATETIME, LocalDateTime.parse(mSingle.group(3), DATE_TIME_FORMATTER));
      eventDetails.put(EventKeys.END_DATETIME, LocalDateTime.parse(mSingle.group(4), DATE_TIME_FORMATTER));
      String otherinfo = null;
      boolean flag = mSingle.group(4) != null;
      if(flag ) {
        otherinfo = mSingle.group(5);
        eventDetails.put(EventKeys.LOCATION, otherinfo);
      }
      flag = mSingle.group(6) != null;
      if(flag) {
        otherinfo = mSingle.group(7);
        eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
      }
      otherinfo = mSingle.group(8);
      if (otherinfo != null && otherinfo.equals("private")) {
        eventDetails.put(EventKeys.PRIVATE, 1);
      }
      CalendarFactory.getSingleCalender().addEvent(eventDetails);
    } else if (mSingleAllDay.matches()) {
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
      eventDetails.put(EventKeys.AUTO_DECLINE, mSingleAllDay.group(1) != null);
      eventDetails.put(EventKeys.SUBJECT, mSingleAllDay.group(2));
      eventDetails.put(EventKeys.ALLDAY_DATE, LocalDate.parse(mSingleAllDay.group(3), DATE_FORMATTER));
      String otherinfo = null;
      boolean flag = mSingleAllDay.group(4) != null;
      if(flag ) {
        otherinfo = mSingleAllDay.group(5);
        eventDetails.put(EventKeys.LOCATION, otherinfo);
      }
      flag = mSingleAllDay.group(6) != null;
      if(flag) {
        otherinfo = mSingleAllDay.group(7);
        eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
      }
      otherinfo = mSingleAllDay.group(8);
      if (otherinfo != null && otherinfo.equals("private")) {
        eventDetails.put(EventKeys.PRIVATE, 1);
      }
      CalendarFactory.getSingleCalender().addEvent(eventDetails);
    } else if (mSingleAllDateTime.matches()) {
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
      eventDetails.put(EventKeys.AUTO_DECLINE, mSingleAllDateTime.group(1) != null);
      eventDetails.put(EventKeys.SUBJECT, mSingleAllDateTime.group(2));
      eventDetails.put(EventKeys.ALLDAY_DATETIME, LocalDateTime.parse(mSingleAllDateTime.group(3), DATE_TIME_FORMATTER));
      String otherinfo = null;
      System.out.println(mSingleAllDateTime.group(3));
      boolean flag = mSingleAllDateTime.group(4) != null;
      if(flag ) {
        otherinfo = mSingleAllDateTime.group(5);
        eventDetails.put(EventKeys.LOCATION, otherinfo);
      }
      flag = mSingleAllDateTime.group(6) != null;
      if(flag) {
        otherinfo = mSingleAllDateTime.group(7);
        eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
      }
      otherinfo = mSingleAllDateTime.group(8);
      if (otherinfo != null && otherinfo.equals("private")) {
        eventDetails.put(EventKeys.PRIVATE, 1);
      }
      CalendarFactory.getSingleCalender().addEvent(eventDetails);
    } else if (mAllDayRecurringForN.matches()) {
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY_RECURRING);
      eventDetails.put(EventKeys.SUBJECT, mAllDayRecurringForN.group(2));
      eventDetails.put(EventKeys.ALLDAY_DATE, LocalDate.parse(mAllDayRecurringForN.group(3), DATE_FORMATTER));
      eventDetails.put(EventKeys.WEEKDAYS, mAllDayRecurringForN.group(4));
      eventDetails.put(EventKeys.OCCURRENCES, Integer.parseInt(mAllDayRecurringForN.group(5)));
      String otherinfo = null;
      boolean flag = mAllDayRecurringForN.group(6) != null;
      if(flag ) {
        otherinfo = mAllDayRecurringForN.group(7);
        eventDetails.put(EventKeys.LOCATION, otherinfo);
      }
      flag = mAllDayRecurringForN.group(8) != null;
      if(flag) {
        otherinfo = mAllDayRecurringForN.group(9);
        eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
      }
      otherinfo = mAllDayRecurringForN.group(8);
      if (otherinfo != null && otherinfo.equals("private")) {
        eventDetails.put(EventKeys.PRIVATE, 1);
      }
      CalendarFactory.getRecurringCalender().addEvent(eventDetails);
    } else if (mAllDayRecurringUntil.matches()) {
      if(!checkValidDate(mAllDayRecurringUntil.group(3), mAllDayRecurringUntil.group(5))) {
        throw new IllegalArgumentException("Invalid date format.");
      }
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY_RECURRING);
      eventDetails.put(EventKeys.SUBJECT, mAllDayRecurringUntil.group(2));
      eventDetails.put(EventKeys.ALLDAY_DATE, LocalDate.parse(mAllDayRecurringUntil.group(3), DATE_FORMATTER));
      eventDetails.put(EventKeys.WEEKDAYS, mAllDayRecurringUntil.group(4));
      eventDetails.put(EventKeys.REPEAT_DATE, LocalDate.parse(mAllDayRecurringUntil.group(5), DATE_FORMATTER));
      String otherinfo = null;
      boolean flag = mAllDayRecurringUntil.group(6) != null;
      if(flag ) {
        otherinfo = mAllDayRecurringUntil.group(7);
        eventDetails.put(EventKeys.LOCATION, otherinfo);
      }
      flag = mAllDayRecurringUntil.group(8) != null;
      if(flag) {
        otherinfo = mAllDayRecurringUntil.group(9);
        eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
      }
      otherinfo = mAllDayRecurringUntil.group(10);
      if (otherinfo != null && otherinfo.equals("private")) {
        eventDetails.put(EventKeys.PRIVATE, 1);
      }
      CalendarFactory.getRecurringCalender().addEvent(eventDetails);
    } else if (mAllDay.matches()) {
      eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
      eventDetails.put(EventKeys.SUBJECT, mAllDay.group(2));
      eventDetails.put(EventKeys.ALLDAY_DATE, LocalDate.parse(mAllDay.group(3), DATE_FORMATTER));
      String otherinfo = null;
      boolean flag = mAllDay.group(6) != null;
      if(flag ) {
        otherinfo = mAllDay.group(7);
        eventDetails.put(EventKeys.LOCATION, otherinfo);
      }
      flag = mAllDay.group(8) != null;
      if(flag) {
        otherinfo = mAllDay.group(9);
        eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
      }
      otherinfo = mAllDay.group(10);
      if (otherinfo != null && otherinfo.equals("private")) {
        eventDetails.put(EventKeys.PRIVATE, 1);
      }
      CalendarFactory.getRecurringCalender().addEvent(eventDetails);
    } else {
      throw new IllegalArgumentException("Invalid create event syntax.");
    }

  }


  private static void editEventCommand(String command) {
    String pattern1 = "edit event (\\w+) (.+?) from (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) with (.+)";
    String pattern2 = "edit events (\\w+) (.+?) from (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) with (.+)";
    String pattern3 = "edit events (\\w+) (.+?) (.+)";

    Pattern p1 = Pattern.compile(pattern1);
    Pattern p2 = Pattern.compile(pattern2);
    Pattern p3 = Pattern.compile(pattern3);

    Matcher m1 = p1.matcher(command);
    Matcher m2 = p2.matcher(command);
    Matcher m3 = p3.matcher(command);

    Map<String, Object> eventDes = new HashMap<>();
    if (m1.matches()) {
      String property = m1.group(1);
      String eventName = m1.group(2);
      LocalDateTime startDateTime = LocalDateTime.parse(m1.group(3), DATE_TIME_FORMATTER);
      LocalDateTime endDateTime = LocalDateTime.parse(m1.group(4), DATE_TIME_FORMATTER);
      String newValue = m1.group(5);

      eventDes.put(EventKeys.PROPERTY, property);
      eventDes.put(EventKeys.SUBJECT, eventName);
      eventDes.put(EventKeys.START_DATETIME, startDateTime);
      eventDes.put(EventKeys.END_DATETIME, endDateTime);
      eventDes.put(EventKeys.NEW_VALUE, newValue);

      CalendarFactory.getSingleCalender().editEvent(eventDes);
    } else if (m2.matches()) {
      String property = m2.group(1);
      String eventName = m2.group(2);
      LocalDateTime startDateTime = LocalDateTime.parse(m2.group(3), DATE_TIME_FORMATTER);
      String newValue = m2.group(4);

      eventDes.put(EventKeys.PROPERTY, property);
      eventDes.put(EventKeys.SUBJECT, eventName);
      eventDes.put(EventKeys.START_DATETIME, startDateTime);
      eventDes.put(EventKeys.NEW_VALUE, newValue);
      CalendarFactory.getSingleCalender().editEvent(eventDes);
    } else if (m3.matches()) {
      String property = m3.group(1);
      String eventName = m3.group(2);
      String newValue = m3.group(3);

      eventDes.put(EventKeys.PROPERTY, property);
      eventDes.put(EventKeys.SUBJECT, eventName);
      eventDes.put(EventKeys.NEW_VALUE, newValue);
      CalendarFactory.getSingleCalender().editEvent(eventDes);
    } else {
      throw new IllegalArgumentException("Invalid edit command format.");
    }
  }

  private static void printEvents(String command) {
    String pattern1 = "print events on (\\d{4}-\\d{2}-\\d{2})";
    Pattern p1 = Pattern.compile(pattern1);
    Matcher m1 = p1.matcher(command);
    String pattern2 = "print events from (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})";
    Pattern p2 = Pattern.compile(pattern2);
    Matcher m2 = p2.matcher(command);
    if(m1.matches()){
      CalendarFactory.getCalendarView().printEventsOn(m1.group(1));
    }
    else if (m2.matches()) {
      CalendarFactory.getCalendarView().printEventsFromTo(m2.group(1), m2.group(2));
    }
    else {
      throw new IllegalArgumentException("Invalid print command format.");
    }

  }

  private static void exportCalendar(String command) {
    String[] parts  = command.split(" ");
    CalendarFactory.getCalendarExport().generateCSV(parts[parts.length -1]);
  }

  private static void showStatus(String command) {
    String showPattern = "show status on (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})";
    Pattern p = Pattern.compile(showPattern);
    Matcher m = p.matcher(command);
    if (m.matches()) {
      CalendarFactory.getCalendarView().showStatusOn(m.group(1));
    }
    else {
      throw new IllegalArgumentException("Invalid show command format.");
    }
  }

public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input;

    do {
      System.out.println("Enter calender command (or 'exit' to quit):");
      input = scanner.nextLine();

      if (!input.equalsIgnoreCase("exit")) {
        System.out.println("Executing command: " + input);
        processCommand(input);
      }
    } while (!input.equalsIgnoreCase("exit"));

    System.out.println("Exiting application.");
    scanner.close();
  }
}
