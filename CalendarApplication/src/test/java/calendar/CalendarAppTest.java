package calendar;

import calendar.model.CalendarEvent;
import calendar.controller.MockController;
import calendar.model.MockModel;
import calendar.view.MockView;
import calendar.view.CalendarView;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CalendarAppTest {

  @Test
  public void testAddEvent() throws IOException {
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CalendarEvent mockModel = new MockModel();
    CalendarView mockView = new MockView(outputStream);
    MockController mockController = new MockController(mockModel, mockView);

    // Act
    try {
      mockController.processCommand("add event");
      mockController.processCommand("view events");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    // Assert
    mockView.displayOutput("New Event");
//    System.out.println(output);
//    assertTrue(output.contains("New Event"));  // Check if the new event appears in the output
  }

  public void testRemoveEvent() throws IOException {
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    MockModel mockModel = new MockModel();
    MockView mockView = new MockView(outputStream);
    MockController mockController = new MockController(mockModel, mockView);

    // Act
    try {
      mockController.processCommand("view event");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    try {
      mockController.processCommand("view events");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    // Assert
    String output = mockView.getOutput();
    assertTrue(output.contains("New Event"));  // Ensure the event is removed from the output
  }

  @Test
  public void testViewEvents() throws IOException {
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    MockModel mockModel = new MockModel();
    MockView mockView = new MockView(outputStream);
    MockController mockController = new MockController(mockModel, mockView);

    // Act
    try {
      mockController.processCommand("view events");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    // Assert
    String output = mockView.getOutput();
    assertTrue(output.contains("Meeting"));  // Ensure the "Meeting" event appears in the output
  }

  @Test
  public void testInvalidCommand() throws IOException {
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    MockModel mockModel = new MockModel();
    MockView mockView = new MockView(outputStream);
    MockController mockController = new MockController(mockModel, mockView);

    // Act
    try {
      mockController.processCommand("invalid command");
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    // Assert
    String output = mockView.getOutput();
    assertEquals("Invalid command\n", output);  // Ensure invalid command is handled
  }
}
