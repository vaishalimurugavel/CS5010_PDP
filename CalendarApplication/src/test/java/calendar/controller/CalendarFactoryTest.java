package calendar.controller;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import calendar.model.CalendarEvent;
import calendar.model.CalendarGroup;
import calendar.model.MockGroup;
import calendar.model.MockModel;
import calendar.view.CalendarView;
import calendar.view.MockView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for CalendarFactory class. Tests if all the method invoked performs the intended operation.
 */
public class CalendarFactoryTest {

  private CalendarEvent calendarEvent;
  private CalendarView calendarView;
  private CalendarGroup calendarGroup;

  @Before
  public void setUp() {
    calendarEvent = new MockModel("Meeting");
    calendarView = new MockView("Monthly View");
    calendarGroup = new MockGroup("Work Group");

    CalendarFactory.setModel(null);
    CalendarFactory.setView(null);
    CalendarFactory.setExport(null);
    CalendarFactory.setGroup(null);
  }

  @Test
  public void testGetModelBeforeSetting() {
    assertNull(CalendarFactory.getModel());
  }

  @Test
  public void testGetViewBeforeSetting() {
    assertNull(CalendarFactory.getView());
  }

  @Test
  public void testSetAndGetView() {
    CalendarFactory.setView(calendarView);
    assertNotNull(CalendarFactory.getView());
    assertEquals(calendarView, CalendarFactory.getView());
  }

  @Test
  public void testGetExportBeforeSetting() {
    assertNull(CalendarFactory.getExport());
  }

  @Test
  public void testSetAndGetExport() throws IOException {
    CalendarFactory.setExport(calendarView);
    assertNotNull(CalendarFactory.getExport());
    assertEquals(calendarView, CalendarFactory.getExport());
  }

  @Test
  public void testGetGroupBeforeSetting() {
    assertNull(CalendarFactory.getGroup());
  }

  @Test
  public void testSetAndGetGroup() {
    CalendarFactory.setGroup(calendarGroup);
    assertNotNull(CalendarFactory.getGroup());
    assertEquals(calendarGroup, CalendarFactory.getGroup());
  }

  @Test
  public void testSetGroupWithNull() {
    CalendarFactory.setGroup(null);
    assertNull(CalendarFactory.getGroup());
  }
}
