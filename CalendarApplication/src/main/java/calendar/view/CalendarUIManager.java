package calendar.view;

import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import calendar.controller.CalendarFactory;
import calendar.model.EventKeys;

public class CalendarUIManager extends JFrame {
  private JFrame frame;
  private JTable eventTable;
  private DefaultTableModel tableModel;
  private JTextArea eventDetails;
  private JButton addButton, editButton, selectButton, exportButton, importButton;
  JPanel calendarPanel;
  JLabel monthLabel;
  YearMonth currentMonth;

  public CalendarUIManager() {
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
    // Create a dialog
    JDialog dialog = new JDialog(parentFrame, "Add Calendar", true);
    dialog.setSize(300, 200);
    dialog.setLayout(new GridLayout(3, 2, 5, 5));

    // Input fields
    JLabel nameLabel = new JLabel("Calendar Name:");
    JTextField nameField = new JTextField();
    JLabel timezoneLabel = new JLabel("Timezone:");
    JTextField timezoneField = new JTextField();

    // Buttons
    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");

    // Add components to dialog
    dialog.add(nameLabel);
    dialog.add(nameField);
    dialog.add(timezoneLabel);
    dialog.add(timezoneField);
    dialog.add(saveButton);
    dialog.add(cancelButton);

    // Save button action
    saveButton.addActionListener(e -> {
      String calendarName = nameField.getText();
      String timezone = timezoneField.getText();
      if (!calendarName.isEmpty() && !timezone.isEmpty()) {
        JOptionPane.showMessageDialog(dialog,
                "Calendar Created: " + calendarName + " (" + timezone + ")",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        dialog.dispose();
      } else {
        JOptionPane.showMessageDialog(dialog,
                "Please enter both fields.",
                "Error", JOptionPane.ERROR_MESSAGE);
      }
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    dialog.setLocationRelativeTo(parentFrame);
    dialog.setVisible(true);
  }

  private static void showEditCalendarDialog(JFrame parentFrame) {
    JDialog dialog = new JDialog(parentFrame, "Edit Calendar", true);
    dialog.setSize(300, 200);
    dialog.setLayout(new GridLayout(5, 3, 5, 5));

    String[] options = CalendarFactory.getGroup().getCalendarNames();
    JLabel selectLable = new JLabel("Select Calendar to edit:");
    JComboBox<String> dropdown = new JComboBox<>(options);
    dialog.add(selectLable);
    dialog.add(dropdown);

    JLabel nameLabel = new JLabel("Calendar Name:");
    JTextField nameField = new JTextField();
    JLabel timezoneLabel = new JLabel("Timezone:");
    Stream<String> op = Arrays.stream(ZoneId.getAvailableZoneIds().toArray(new String[0])).sorted();
    JComboBox<String> dropdown2 = new JComboBox<>(op.toArray(String[]::new));
    dialog.add(dropdown2);

    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");

    dialog.add(nameLabel);
    dialog.add(nameField);
    dialog.add(timezoneLabel);
    dialog.add(dropdown2);
    dialog.add(saveButton);
    dialog.add(cancelButton);

    saveButton.addActionListener(e -> {
      String calendarName = nameField.getText();
      String timezone = dropdown2.toString();
      if (!calendarName.isEmpty() || !timezone.isEmpty()) {
        JOptionPane.showMessageDialog(dialog,
                "Calendar " + calendarName + "Updated : (" + timezone + ")",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        dialog.dispose();
      } else {
        JOptionPane.showMessageDialog(dialog,
                "Please enter any one field.",
                "Error", JOptionPane.ERROR_MESSAGE);
      }
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    dialog.setLocationRelativeTo(parentFrame);
    dialog.setVisible(true);
  }

  private void showSelectCalendarDialog(JFrame selectFrame) {
    HashMap<LocalDate, List<String>> events = new HashMap<>();
    currentMonth = YearMonth.now();

    // Top Panel
    JPanel topPanel = new JPanel(new FlowLayout());
    JButton addEventButton = new JButton("Add Event");
    JButton editEventButton = new JButton("Edit Event");
    JButton prevMonthButton = new JButton("Previous");
    JButton nextMonthButton = new JButton("Next");
    monthLabel = new JLabel("", SwingConstants.CENTER);

    topPanel.add(addEventButton);
    topPanel.add(editEventButton);
    topPanel.add(prevMonthButton);
    topPanel.add(monthLabel);
    topPanel.add(nextMonthButton);

    // Calendar Panel
    calendarPanel = new JPanel(new GridLayout(0, 7));
    updateCalendar();

    selectFrame.add(topPanel, BorderLayout.NORTH);
    selectFrame.add(calendarPanel, BorderLayout.CENTER);

    prevMonthButton.addActionListener(e -> changeMonth(-1));
    nextMonthButton.addActionListener(e -> changeMonth(1));

    selectFrame.setVisible(true);
  }


  private void updateCalendar() {
      calendarPanel.removeAll();
      monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

      // Headers for days
      String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
      for (String day : days) {
        calendarPanel.add(new JLabel(day, SwingConstants.CENTER));
      }

      // First day of month
      LocalDate firstDay = currentMonth.atDay(1);
      int firstDayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // Adjust to start on Sunday
      int daysInMonth = currentMonth.lengthOfMonth();

      // Empty slots for previous month's days
      for (int i = 0; i < firstDayOfWeek; i++) {
        calendarPanel.add(new JLabel(""));
      }

      // Add buttons for each day
      for (int day = 1; day <= daysInMonth; day++) {
        LocalDate date = currentMonth.atDay(day);
        JButton dayButton = new JButton(String.valueOf(day));
       // dayButton.addActionListener(e -> viewEvents(date));
        calendarPanel.add(dayButton);
      }

      calendarPanel.revalidate();
      calendarPanel.repaint();
    }

    private void changeMonth(int offset) {
      currentMonth = currentMonth.plusMonths(offset);
      updateCalendar();
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
