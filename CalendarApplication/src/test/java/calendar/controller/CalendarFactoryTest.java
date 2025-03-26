package calendar.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.management.ObjectName;

import calendar.model.CalendarGroup;
import calendar.model.CalendarEvent;
import calendar.model.EventKeys;
import calendar.model.MockGroup;
import calendar.model.MockModel;
import calendar.view.CalendarView;
import calendar.view.MockView;




public class CalendarFactoryTest {

  private CalendarEvent calendarEvent;
  private CalendarView calendarView;
  private CalendarGroup calendarGroup;

  @Before
  public void setUp() {
    // Initialize the objects to use in the tests
    calendarEvent = new MockModel("Meeting");
    calendarView = new MockView("Monthly View");
    calendarGroup = new MockGroup("Work Group");

    // Reset static fields before each test
    CalendarFactory.setModel(null);
    CalendarFactory.setView(null);
    CalendarFactory.setExport(null);
    CalendarFactory.setGroup(null);
  }

  @Test
  public void testGetModelBeforeSetting() {
    // Test that getModel() returns null before setting
    assertNull(CalendarFactory.getModel());
  }

  @Test
  public void testSetAndGetModel() {
    // Test that the model can be set and retrieved
    CalendarFactory.setModel(calendarEvent);
    assertNotNull(CalendarFactory.getModel());
    assertEquals(calendarEvent, CalendarFactory.getModel());
    List<Map<String, Object>> res = CalendarFactory.getModel().getEventForDisplay("Meeting");
//    assertEquals("Meeting", res.get(0).get(EventKeys.SUBJECT));

  }

  @Test
  public void testGetViewBeforeSetting() {
    // Test that getView() returns null before setting
    assertNull(CalendarFactory.getView());
  }

  @Test
  public void testSetAndGetView() {
    // Test that the view can be set and retrieved
    CalendarFactory.setView(calendarView);
    assertNotNull(CalendarFactory.getView());
    assertEquals(calendarView, CalendarFactory.getView());
    //assertEquals("Monthly View", CalendarFactory.getView().getViewName());
  }

  @Test
  public void testGetExportBeforeSetting() {
    // Test that getExport() returns null before setting
    assertNull(CalendarFactory.getExport());
  }

  @Test
  public void testSetAndGetExport() throws IOException {
    // Test that the export view can be set and retrieved
    CalendarFactory.setExport(calendarView);
    assertNotNull(CalendarFactory.getExport());
    assertEquals(calendarView, CalendarFactory.getExport());
  //  assertEquals("Monthly View", CalendarFactory.getExport().displayOutput("Monthly view"));
  }

  @Test
  public void testGetGroupBeforeSetting() {
    // Test that getGroup() returns null before setting
    assertNull(CalendarFactory.getGroup());
  }

  @Test
  public void testSetAndGetGroup() {
    // Test that the group can be set and retrieved
    CalendarFactory.setGroup(calendarGroup);
    assertNotNull(CalendarFactory.getGroup());
    assertEquals(calendarGroup, CalendarFactory.getGroup());
    //assertEquals("Work Group", CalendarFactory.getGroup().getGroupName());
  }

  @Test
  public void testSetGroupWithNull() {
    // Test that setting null as a group works correctly
    CalendarFactory.setGroup(null);
    assertNull(CalendarFactory.getGroup());
  }
}
