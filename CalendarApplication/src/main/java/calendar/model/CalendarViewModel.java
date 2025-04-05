package calendar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public interface CalendarViewModel {
  /**
   * Retrieves a list of events that can be displayed.
   *
   * @return A list of maps where each map represents an event with its details.
   */
  List<Map<String, Object>> getEventsForDisplay();

  /**
   * Retrieves events that match the specified subject.
   *
   * @param subject The subject of the event to search for.
   * @return A list of events that match the given subject.
   */
  List<Map<String, Object>> getEventForDisplay(String subject);

  /**
   * Retrieves events that fall within the specified time range.
   *
   * @param startTime The start time of the range.
   * @param endTime The end time of the range.
   * @return A list of events that fall within the specified time range.
   */
  List<Map<String, Object>> getEventForDisplay(LocalDateTime startTime, LocalDateTime endTime);

  /**
   * Retrieves events that fall within the specified date range.
   *
   * @param startDate The start date of the range.
   * @param endDate The end date of the range.
   * @return A list of events that fall within the specified date range.
   */
  List<Map<String, Object>> getEventForDisplay(LocalDate startDate, LocalDate endDate);

  /**
   * Retrieves the user's status (busy or not) for a specified date and time.
   *
   * @param date The date and time for which to check the user's status.
   * @return A list of events that indicate whether the user is busy or not at the given time.
   */
  List<Map<String, Object>> getUserStatus(LocalDateTime date);

  /**
   * Retrieves a list of events.
   *
   * @return A list of maps where each map represents an event with its details.
   */
  List<Map<String, Object>> getEvents();

  List<String> getGroups();
}
