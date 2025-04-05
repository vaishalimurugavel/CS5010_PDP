package calendar.view.gui;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import calendar.controller.CalendarFactory;
import calendar.model.CalendarGroupManager;
import calendar.model.CalenderEventManager;
import calendar.model.EventKeys;

public class CalendarUIManager extends JFrame {
  static CalendarGUIManager calendarGUIManager = new CalendarGUIManager();
  private JFrame frame;
  private JTable eventTable;
  private DefaultTableModel tableModel;
  private JTextArea eventDetails;
  private JButton addButton, editButton, selectButton, exportButton, importButton;

  public CalendarUIManager() {
    CalendarFactory.setModel(new CalenderEventManager());
    CalendarFactory.setGroup(new CalendarGroupManager());
    frame = new JFrame("Calendar Application");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setLayout(new BorderLayout());
    frame.setResizable(true);

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.CENTER;

    String[] options = CalendarFactory.getGroup().getCalendarNames();
    JComboBox<String> dropdown = new JComboBox<>(options);
    panel.add(dropdown, gbc);

    addButton = new JButton("Add calendar");
    editButton = new JButton("Edit calendar");
    selectButton = new JButton("Select calendar");
    exportButton = new JButton("Export calendar");
    importButton = new JButton("Import calendar");

    gbc.gridy = 1;
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(selectButton);

    panel.add(buttonPanel, gbc);

    frame.add(panel, BorderLayout.NORTH);
    frame.setVisible(true);
    addButton.addActionListener(e -> {showAddCalendarDialog(frame);});
    editButton.addActionListener(e -> {showEditCalendarDialog(frame);});
    selectButton.addActionListener(e -> {
      JFrame selectFrame = new JFrame("Select Calendar");
      selectFrame.setSize(600, 500);
      selectFrame.setLayout(new BorderLayout());

      JPanel panels = new JPanel();
      JLabel label = new JLabel("Select a calendar:");
      panels.add(label);

      selectFrame.add(panels, BorderLayout.NORTH);

      showSelectCalendarDialog(selectFrame);
      selectFrame.setVisible(true);
    });
    exportButton.addActionListener(e -> {});
    importButton.addActionListener(e -> {});

  }

  private static void showAddCalendarDialog(JFrame parentFrame) {
    CalendarAddDisplay calendarAddDisplay = new CalendarAddDisplay();
    calendarAddDisplay.showDisplay(parentFrame);
  }

  private static void showEditCalendarDialog(JFrame parentFrame) {
    CalendarGUIInterface edit = new CalendarEditDisplay();
    edit.showDisplay(parentFrame);
  }

  private void showSelectCalendarDialog(JFrame parentFrame) {
    CalendarGUIInterface event = new CalendarEventDisplay();
    event.showDisplay(parentFrame);
  }


  public void updateEventTable(List<Map<String, Object>> eventList) {
    tableModel.setRowCount(0);
    for (Map<String, Object> event : eventList) {
      LocalDate date = (LocalDate) event.get(EventKeys.START_DATETIME);
      String time = event.get(EventKeys.START_DATETIME).toString().split("T")[1];
      String subject = (String) event.get(EventKeys.SUBJECT);
      String type = (String) event.get(EventKeys.EVENT_TYPE);
      tableModel.addRow(new Object[]{date, time, subject, type});
    }
  }

  public static void main(String[] args) {
    new CalendarUIManager();
  }

}
