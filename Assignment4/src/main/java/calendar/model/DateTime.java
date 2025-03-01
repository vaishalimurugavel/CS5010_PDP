package calendar.model;

public class DateTime {
  private final int hours;
  private final int minutes;
  private final int seconds;
  private final int day;
  private final int month;
  private final int year;

  public DateTime(int hours, int minutes, int seconds, int day, int month, int year) throws IllegalArgumentException {

    if ((hours < 0) || (minutes < 0) || (seconds < 0)) {
      throw new IllegalArgumentException("Negative durations are not supported");
    }
    if ((day < 0) || (month < 0) || (year < 0)) {
      throw new IllegalArgumentException("Negative date/month/year are not supported");
    }

    this.hours = hours;
    this.minutes = minutes;
    this.seconds = seconds;
    this.day = day;
    this.month = month;
    this.year = year;
  }

  public int[] getDateAsInt() {
    int[] date = new int[3];
    date[0] = day;
    date[1] = month;
    date[2] = year;
    return date;
  }

  public int getTimeAsSeconds() {

    return (hours * 3600) + (minutes * 60) + seconds;
  }

  public String getTime() {
    return String.format("%d:%02d:%02d", hours, minutes, seconds);
  }

  public String getDate() {
    return String.format("%02d/%02d/%02d", month, day, year);
  }
}
