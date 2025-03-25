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
 * <p>
 * Concrete implementation of the ControllerCommand interface, responsible for handling
 * the creation of different types of calendar events based on the given command.
 * This class interprets commands related to single, recurring, and all-day events,
 * and then processes them accordingly to create appropriate event objects.
 * </p>
 *
 * <p>
 * The class uses regular expressions to parse various event formats such as single events,
 * recurring events (either for a set number of occurrences or until a specific date),
 * and all-day events. It validates input date formats and constructs event details which
 * are subsequently added to the calendar model.
 * </p>
 *
 * <p>
 * Each method in this class is responsible for processing a specific event pattern and
 * extracting the relevant details (such as subject, start and end times, location,
 * description, and privacy settings) to form an event object that is added to the calendar.
 * </p>
 */
public class ControllerCreateCommand implements ControllerCommand {

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
  public void execute(String command)  {

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

    Map<String, Object> eventDetails = null;
    if (mRecurringForN.matches()) {
      eventDetails = recurringMatchForN(mRecurringForN);
    } else if (mRecurringUntil.matches()) {
      eventDetails =  recurringMatchUntil(mRecurringUntil);
    } else if (mSingle.matches()) {
      eventDetails = singleMatch(mSingle);
    } else if (mSingleAllDay.matches()) {
      eventDetails = singleMatchAllDay(mSingleAllDay);
    } else if (mSingleAllDateTime.matches()) {
      eventDetails = singleMatchAllDayUntil(mSingleAllDateTime);
    } else if (mAllDayRecurringForN.matches()) {
      eventDetails = allDayRecurring(mAllDayRecurringForN);
    } else if (mAllDayRecurringUntil.matches()) {
      eventDetails = allDayRecurringMatchUntil(mAllDayRecurringUntil);
    } else if (mAllDay.matches()) {
      eventDetails =  allDay(mAllDay);
    } else {
      throw new IllegalArgumentException("Invalid create event syntax.");
    }
    CalendarFactory.getModel().addEvent(eventDetails);

  }

  private Map<String, Object> allDay(Matcher mAllDay) {
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
    return eventDetails;
  }

  private Map<String, Object> allDayRecurringMatchUntil(Matcher mAllDayRecurringUntil) {
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
    return eventDetails;
  }

  private Map<String, Object> allDayRecurring(Matcher mAllDayRecurringForN) {

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
    return eventDetails;
  }

  private Map<String, Object> singleMatchAllDayUntil(Matcher mSingleAllDateTime) {

    Map<String, Object> eventDetails = new HashMap<>();

    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
    eventDetails.put(EventKeys.AUTO_DECLINE, true);
    eventDetails.put(EventKeys.SUBJECT, mSingleAllDateTime.group(2));
    eventDetails.put(EventKeys.START_DATETIME,
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
    return eventDetails;
  }

  private Map<String, Object> singleMatchAllDay(Matcher mSingleAllDay) {

    Map<String, Object> eventDetails = new HashMap<>();

    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.ALL_DAY);
    eventDetails.put(EventKeys.AUTO_DECLINE, true);
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
    return eventDetails;
  }

  private Map<String, Object> singleMatch(Matcher mSingle) {

    Map<String, Object> eventDetails = new HashMap<>();

    if (!checkValidDate(mSingle.group(3), mSingle.group(4))) {
      throw new IllegalArgumentException("Invalid date format.");
    }
    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);
    eventDetails.put(EventKeys.AUTO_DECLINE, true);
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
    return eventDetails;
  }

  private Map<String, Object> recurringMatchUntil(Matcher mRecurringUntil) {
    Map<String, Object> eventDetails = new HashMap<>();
    if (!checkValidDate(mRecurringUntil.group(3), mRecurringUntil.group(4))) {
      throw new IllegalArgumentException("Invalid date format.");
    }
    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.RECURRING);
    eventDetails.put(EventKeys.AUTO_DECLINE, true);
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
    return eventDetails;
  }

  private Map<String, Object> recurringMatchForN(Matcher mRecurringForN) {
    Map<String, Object> eventDetails = new HashMap<>();
    if (!checkValidDate(mRecurringForN.group(3), mRecurringForN.group(4))) {
      throw new IllegalArgumentException("Invalid date format.");
    }
    eventDetails.put(EventKeys.EVENT_TYPE, EventKeys.EventType.RECURRING);
    eventDetails.put(EventKeys.AUTO_DECLINE, true);
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
    return eventDetails;
  }
}
