package calendar.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calendar.model.EventKeys;

/**
 * Concrete class of the Controller abstract class.
 */
public class CalendarControllerImpl extends CalenderController {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
          .ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
          .ofPattern("yyyy-MM-dd");

  private static boolean checkValidDate(String startDate, String endDate) {
    String datePattern = "\\d{4}-\\d{2}-\\d{2}";
    Pattern pattern = Pattern.compile(datePattern);
    Matcher startmatcher = pattern.matcher(startDate);
    Matcher endMatcher = pattern.matcher(endDate);
    String datePattern2 = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";
    Pattern pattern2 = Pattern.compile(datePattern2);
    Matcher startmatcher2 = pattern2.matcher(startDate);
    Matcher endMatcher2 = pattern2.matcher(endDate);


    if (startmatcher.matches() && endMatcher.matches()) {
      LocalDate startTime = LocalDate.parse(startDate, DATE_FORMATTER);
      LocalDate endTime = LocalDate.parse(endDate, DATE_FORMATTER);
      return startTime.isBefore(endTime) || startTime.equals(endTime);
    } else if (startmatcher2.matches() && endMatcher2.matches()) {
      LocalDateTime startTime = LocalDateTime.parse(startDate, DATE_TIME_FORMATTER);
      LocalDateTime endTime = LocalDateTime.parse(endDate, DATE_TIME_FORMATTER);
      return startTime.isBefore(endTime) || startTime.equals(endTime);
    }
    return false;
  }

  @Override
  public void createEvent(String command) {

    String basePattern = "create event( --autoDecline)? (.+?)";
    String otherInfo = "( location (.+?))?( description (.+?))?( (public|private))?";
    String singleAllDay = basePattern + " on (\\d{4}-\\d{2}-\\d{2})" + otherInfo;
    String singleAllDateTime = basePattern + " on (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})"
            + otherInfo;
    String singleEventPatternCommon = basePattern + " from (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) " +
            "to (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})";
    String singleEventPattern = singleEventPatternCommon + otherInfo;
    String recurringForNTimesPattern = singleEventPatternCommon + " repeats ([MTWRFSU]+) " +
            "for (\\d+) times" + otherInfo;
    String recurringUntilPattern = singleEventPatternCommon + " repeats ([MTWRFSU]+) until " +
            "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})" + otherInfo;
    String allDayEventPatternCommon = basePattern + " on (\\d{4}-\\d{2}-\\d{2})";
    String allDayEventPattern = allDayEventPatternCommon + " on (\\d{4}-\\d{2}-\\d{2})"
            + otherInfo;
    String allDayRecurringForNTimesPattern = allDayEventPatternCommon + " repeats ([MTWRFSU]+) " +
            "for (\\d+) times" + otherInfo;
    String allDayRecurringUntilPattern = allDayEventPatternCommon + " repeats ([MTWRFSU]+) " +
            "until " +
            "(\\d{4}-\\d{2}-\\d{2})" + otherInfo;

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
      recurringMatchForN(mRecurringForN);
    } else if (mRecurringUntil.matches()) {
      recurringMatchUntil(mRecurringUntil);
    } else if (mSingle.matches()) {
      singleMatch(mSingle);
    } else if (mSingleAllDay.matches()) {
      singleMatchAllDay(mSingleAllDay);
    } else if (mSingleAllDateTime.matches()) {
      singleMatchAllDayUntil(mSingleAllDateTime);
    } else if (mAllDayRecurringForN.matches()) {
      allDayRecurring(mAllDayRecurringForN);
    } else if (mAllDayRecurringUntil.matches()) {
      allDayRecurringMatchUntil(mAllDayRecurringUntil);
    } else if (mAllDay.matches()) {
      allDay(mAllDay);
    } else {
      throw new IllegalArgumentException("Invalid create event syntax.");
    }

  }

  private void allDay(Matcher mAllDay) {
    Map<String, Object> eventDetails = new HashMap<>();
    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
    eventDetails.put(EventKeys.SUBJECT, mAllDay.group(2));
    eventDetails.put(EventKeys.ALLDAY_DATE, LocalDate.parse(mAllDay.group(3), DATE_FORMATTER));
    String otherinfo = null;
    boolean flag = mAllDay.group(4) != null;
    if (flag) {
      otherinfo = mAllDay.group(5);
      eventDetails.put(EventKeys.LOCATION, otherinfo);
    }
    flag = mAllDay.group(6) != null;
    if (flag) {
      otherinfo = mAllDay.group(7);
      eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
    }
    otherinfo = mAllDay.group(8);
    if (otherinfo != null && otherinfo.equals(" private")) {
      eventDetails.put(EventKeys.PRIVATE, 1);
    }
    CalendarFactory.getRecurringCalender().addEvent(eventDetails);
  }

  private void allDayRecurringMatchUntil(Matcher mAllDayRecurringUntil) {
    Map<String, Object> eventDetails = new HashMap<>();

    if (!checkValidDate(mAllDayRecurringUntil.group(3), mAllDayRecurringUntil.group(5))) {
      throw new IllegalArgumentException("Invalid date format.");
    }
    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY_RECURRING);
    eventDetails.put(EventKeys.SUBJECT, mAllDayRecurringUntil.group(2));
    eventDetails.put(EventKeys.ALLDAY_DATE,
            LocalDate.parse(mAllDayRecurringUntil.group(3), DATE_FORMATTER));
    eventDetails.put(EventKeys.WEEKDAYS, mAllDayRecurringUntil.group(4));
    eventDetails.put(EventKeys.REPEAT_DATE,
            LocalDate.parse(mAllDayRecurringUntil.group(5), DATE_FORMATTER));
    String otherinfo;
    boolean flag = mAllDayRecurringUntil.group(6) != null;
    if (flag) {
      otherinfo = mAllDayRecurringUntil.group(7);
      eventDetails.put(EventKeys.LOCATION, otherinfo);
    }
    flag = mAllDayRecurringUntil.group(8) != null;
    if (flag) {
      otherinfo = mAllDayRecurringUntil.group(9);
      eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
    }
    otherinfo = mAllDayRecurringUntil.group(10);
    if (otherinfo != null && otherinfo.equals(" private")) {
      eventDetails.put(EventKeys.PRIVATE, 1);
    }
    CalendarFactory.getRecurringCalender().addEvent(eventDetails);
  }

  private void allDayRecurring(Matcher mAllDayRecurringForN) {

    Map<String, Object> eventDetails = new HashMap<>();
    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY_RECURRING);
    eventDetails.put(EventKeys.SUBJECT, mAllDayRecurringForN.group(2));
    eventDetails.put(EventKeys.ALLDAY_DATE,
            LocalDate.parse(mAllDayRecurringForN.group(3), DATE_FORMATTER));
    eventDetails.put(EventKeys.WEEKDAYS, mAllDayRecurringForN.group(4));
    eventDetails.put(EventKeys.OCCURRENCES, Integer.parseInt(mAllDayRecurringForN.group(5)));
    String otherinfo = null;
    boolean flag = mAllDayRecurringForN.group(6) != null;
    if (flag) {
      otherinfo = mAllDayRecurringForN.group(7);
      eventDetails.put(EventKeys.LOCATION, otherinfo);
    }
    flag = mAllDayRecurringForN.group(8) != null;
    if (flag) {
      otherinfo = mAllDayRecurringForN.group(9);
      eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
    }
    otherinfo = mAllDayRecurringForN.group(8);
    if (otherinfo != null && otherinfo.equals(" private")) {
      eventDetails.put(EventKeys.PRIVATE, 1);
    }
    CalendarFactory.getRecurringCalender().addEvent(eventDetails);
  }

  private void singleMatchAllDayUntil(Matcher mSingleAllDateTime) {

    Map<String, Object> eventDetails = new HashMap<>();

    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
    eventDetails.put(EventKeys.AUTO_DECLINE, mSingleAllDateTime.group(1) != null);
    eventDetails.put(EventKeys.SUBJECT, mSingleAllDateTime.group(2));
    eventDetails.put(EventKeys.ALLDAY_DATETIME,
            LocalDateTime.parse(mSingleAllDateTime.group(3), DATE_TIME_FORMATTER));
    String otherinfo = null;
    boolean flag = mSingleAllDateTime.group(4) != null;
    if (flag) {
      otherinfo = mSingleAllDateTime.group(5);
      eventDetails.put(EventKeys.LOCATION, otherinfo);
    }
    flag = mSingleAllDateTime.group(6) != null;
    if (flag) {
      otherinfo = mSingleAllDateTime.group(7);
      eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
    }
    otherinfo = mSingleAllDateTime.group(8);
    if (otherinfo != null && otherinfo.equals(" private")) {
      eventDetails.put(EventKeys.PRIVATE, 1);
    }
    CalendarFactory.getSingleCalender().addEvent(eventDetails);
  }

  private void singleMatchAllDay(Matcher mSingleAllDay) {

    Map<String, Object> eventDetails = new HashMap<>();

    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
    eventDetails.put(EventKeys.AUTO_DECLINE, mSingleAllDay.group(1) != null);
    eventDetails.put(EventKeys.SUBJECT, mSingleAllDay.group(2));
    eventDetails.put(EventKeys.ALLDAY_DATE,
            LocalDate.parse(mSingleAllDay.group(3), DATE_FORMATTER));
    String otherinfo = null;
    boolean flag = mSingleAllDay.group(4) != null;
    if (flag) {
      otherinfo = mSingleAllDay.group(5);
      eventDetails.put(EventKeys.LOCATION, otherinfo);
    }
    flag = mSingleAllDay.group(6) != null;
    if (flag) {
      otherinfo = mSingleAllDay.group(7);
      eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
    }
    otherinfo = mSingleAllDay.group(8);
    if (otherinfo != null && otherinfo.equals(" private")) {
      eventDetails.put(EventKeys.PRIVATE, 1);
    }
    CalendarFactory.getSingleCalender().addEvent(eventDetails);
  }

  private void singleMatch(Matcher mSingle) {

    Map<String, Object> eventDetails = new HashMap<>();

    if (!checkValidDate(mSingle.group(3), mSingle.group(4))) {
      throw new IllegalArgumentException("Invalid date format.");
    }
    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);
    eventDetails.put(EventKeys.AUTO_DECLINE, mSingle.group(1) != null);
    eventDetails.put(EventKeys.SUBJECT, mSingle.group(2));
    eventDetails.put(EventKeys.START_DATETIME, LocalDateTime.parse(mSingle.group(3),
            DATE_TIME_FORMATTER));
    eventDetails.put(EventKeys.END_DATETIME, LocalDateTime.parse(mSingle.group(4),
            DATE_TIME_FORMATTER));
    String otherinfo = null;
    boolean flag = mSingle.group(5) != null;
    if (flag) {
      otherinfo = mSingle.group(6);
      eventDetails.put(EventKeys.LOCATION, otherinfo);
    }
    flag = mSingle.group(7) != null;
    if (flag) {
      otherinfo = mSingle.group(8);
      eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
    }
    otherinfo = mSingle.group(9);
    if (otherinfo != null && otherinfo.equals(" private")) {
      eventDetails.put(EventKeys.PRIVATE, 1);
    }
    CalendarFactory.getSingleCalender().addEvent(eventDetails);
  }

  private void recurringMatchUntil(Matcher mRecurringUntil) {
    Map<String, Object> eventDetails = new HashMap<>();
    if (!checkValidDate(mRecurringUntil.group(3), mRecurringUntil.group(4))) {
      throw new IllegalArgumentException("Invalid date format.");
    }
    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.RECURRING);
    eventDetails.put(EventKeys.AUTO_DECLINE, mRecurringUntil.group(1) != null);
    eventDetails.put(EventKeys.SUBJECT, mRecurringUntil.group(2));
    eventDetails.put(EventKeys.START_DATETIME,
            LocalDateTime.parse(mRecurringUntil.group(3), DATE_TIME_FORMATTER));
    eventDetails.put(EventKeys.END_DATETIME,
            LocalDateTime.parse(mRecurringUntil.group(4), DATE_TIME_FORMATTER));
    eventDetails.put(EventKeys.WEEKDAYS, mRecurringUntil.group(5));
    eventDetails.put(EventKeys.REPEAT_DATETIME,
            LocalDateTime.parse(mRecurringUntil.group(6), DATE_TIME_FORMATTER));
    String otherinfo = null;
    boolean flag = mRecurringUntil.group(7) != null;
    if (flag) {
      otherinfo = mRecurringUntil.group(8);
      eventDetails.put(EventKeys.LOCATION, otherinfo);
    }
    flag = mRecurringUntil.group(9) != null;
    if (flag) {
      otherinfo = mRecurringUntil.group(10);
      eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
    }
    otherinfo = mRecurringUntil.group(11);
    if (otherinfo != null && otherinfo.equals(" private")) {
      eventDetails.put(EventKeys.PRIVATE, 1);
    }
    CalendarFactory.getRecurringCalender().addEvent(eventDetails);
  }

  private static void recurringMatchForN(Matcher mRecurringForN) {
    Map<String, Object> eventDetails = new HashMap<>();
    if (!checkValidDate(mRecurringForN.group(3), mRecurringForN.group(4))) {
      throw new IllegalArgumentException("Invalid date format.");
    }
    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.RECURRING);
    eventDetails.put(EventKeys.AUTO_DECLINE, mRecurringForN.group(1) != null);
    eventDetails.put(EventKeys.SUBJECT, mRecurringForN.group(2));
    eventDetails.put(EventKeys.START_DATETIME,
            LocalDateTime.parse(mRecurringForN.group(3), DATE_TIME_FORMATTER));
    eventDetails.put(EventKeys.END_DATETIME,
            LocalDateTime.parse(mRecurringForN.group(4), DATE_TIME_FORMATTER));
    eventDetails.put(EventKeys.WEEKDAYS, mRecurringForN.group(5));
    eventDetails.put(EventKeys.OCCURRENCES, Integer.parseInt(mRecurringForN.group(6)));
    String otherinfo = null;
    boolean flag = mRecurringForN.group(7) != null;
    if (flag) {
      otherinfo = mRecurringForN.group(8);
      eventDetails.put(EventKeys.LOCATION, otherinfo);
    }
    flag = mRecurringForN.group(9) != null;
    if (flag) {
      otherinfo = mRecurringForN.group(10);
      eventDetails.put(EventKeys.DESCRIPTION, otherinfo);
    }
    otherinfo = mRecurringForN.group(11);
    if (otherinfo != null && otherinfo.equals(" private")) {
      eventDetails.put(EventKeys.PRIVATE, 1);
    }
    CalendarFactory.getRecurringCalender().addEvent(eventDetails);
  }


  @Override
  void editEventCommand(String command) {
    String pattern1 = "edit event (\\w+) (.+?) from " +
            "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})" +
            " to (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) with (.+)";
    String pattern2 = "edit events (\\w+) (.+?) from (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) " +
            "with (.+)";
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

  @Override
  void printEvents(String command) {
    String pattern1 = "print events on (\\d{4}-\\d{2}-\\d{2})";
    Pattern p1 = Pattern.compile(pattern1);
    Matcher m1 = p1.matcher(command);
    String pattern2 = "print events from (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) " +
            "to (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})";
    Pattern p2 = Pattern.compile(pattern2);
    Matcher m2 = p2.matcher(command);
    if (m1.matches()) {
      CalendarFactory.getCalendarView().printEventsOn(m1.group(1));
    } else if (m2.matches()) {
      CalendarFactory.getCalendarView().printEventsFromTo(m2.group(1), m2.group(2));
    } else {
      throw new IllegalArgumentException("Invalid print command format.");
    }

  }

  @Override
  void exportCalendar(String command) {
    String[] parts = command.split(" ");
    CalendarFactory.getCalendarExport().generateCSV(parts[parts.length - 1]);
  }

  @Override
  void showStatus(String command) {
    String showPattern = "show status on (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})";
    Pattern p = Pattern.compile(showPattern);
    Matcher m = p.matcher(command);
    if (m.matches()) {
      CalendarFactory.getCalendarView().showStatusOn(m.group(1));
    } else {
      throw new IllegalArgumentException("Invalid show command format.");
    }
  }

}
