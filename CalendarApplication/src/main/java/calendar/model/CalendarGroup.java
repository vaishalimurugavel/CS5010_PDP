package calendar.model;

import java.time.ZoneId;

/**
 * Created at 23-03-2025
 * Author Vaishali
 **/

public class CalendarGroup {

  private String title;
  private ZoneId zoneId;
  private String zoneName;
  private CalendarEvent calendarEvents;

  CalendarGroup(String title, ZoneId zoneId, String zoneName) {
    this.title = title;
    this.zoneId = zoneId;
    this.zoneName = zoneName;
  }

  public String getTitle() {
    return title;
  }

  public CalendarEvent getCalendarEvents() {
    return calendarEvents;
  }

  public ZoneId getZoneId() {
    return zoneId;
  }
}
