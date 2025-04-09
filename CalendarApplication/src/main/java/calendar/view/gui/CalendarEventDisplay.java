package calendar.view.gui;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import calendar.controller.CalendarFactory;
import calendar.model.EventKeys;

/**
 * <p>
 * CalendarEventDisplay class displays month view of a selected calendar.
 * It also supports adding new event, editing event, importing and exporting csv calendar files.
 * </p>
 **/
public class CalendarEventDisplay implements CalendarGUIInterface {
  CalendarGUIManager calendarGUIManager = new CalendarGUIManager();
  JPanel calendarPanel;
  JLabel monthLabel;
  YearMonth currentMonth;

  @Override
  public void showDisplay(JFrame parentFrame) {
    currentMonth = YearMonth.now();

    String[] options = CalendarFactory.getGroup().getCalendarNames();
    JComboBox<String> dropdown = new JComboBox<>(options);

    JPanel panels = new JPanel();
    JLabel label = new JLabel("Select a calendar:");
    panels.add(label);
    panels.add(dropdown);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.CENTER;


    JButton okButton = new JButton("Ok");
    JButton cancelButton = new JButton("Cancel");
    gbc.gridy = 3;
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);


    panels.add(buttonPanel, gbc);
    parentFrame.add(panels, BorderLayout.NORTH);

    parentFrame.setVisible(true);
    cancelButton.addActionListener(e -> {
      parentFrame.dispose();
    });
    okButton.addActionListener(e -> {
      String calendar = (String) dropdown.getSelectedItem();
      calendarGUIManager.setCalendar(calendar);
      showSelectDisplay(calendar);
    });

  }

  private void showSelectDisplay(String selectedCalendar) {
    String timeZone = CalendarFactory.getGroup().getCalendar(selectedCalendar).getZoneName();
    JFrame selectFrame = new JFrame("Calendar: " + selectedCalendar + " Time Zone: " + timeZone);
    selectFrame.setSize(800, 600);
    selectFrame.setLayout(new BorderLayout());

    JPanel topPanel = new JPanel(new FlowLayout());
    JButton addEventButton = new JButton("Add Event");
    JButton editEventButton = new JButton("Edit Event");
    JButton prevMonthButton = new JButton("Previous");
    JButton nextMonthButton = new JButton("Next");
    JButton exportButton = new JButton("Export calendar");
    JButton importButton = new JButton("Import calendar");
    monthLabel = new JLabel("", SwingConstants.CENTER);

    topPanel.add(addEventButton);
    topPanel.add(editEventButton);
    topPanel.add(prevMonthButton);
    topPanel.add(monthLabel);
    topPanel.add(nextMonthButton);
    topPanel.add(exportButton);
    topPanel.add(importButton);

    calendarPanel = new JPanel(new GridLayout(0, 7));
    updateCalendar();

    selectFrame.add(topPanel, BorderLayout.NORTH);
    selectFrame.add(calendarPanel, BorderLayout.CENTER);

    prevMonthButton.addActionListener(e -> changeMonth(-1));
    nextMonthButton.addActionListener(e -> changeMonth(1));
    addEventButton.addActionListener(e -> addEvents());
    editEventButton.addActionListener(e -> editEvents());
    exportButton.addActionListener(e -> {
      JFileChooser folderChooser = new JFileChooser();
      folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int result = folderChooser.showOpenDialog(null);

      if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFolder = folderChooser.getSelectedFile();
        String folderPath = selectedFolder.getAbsolutePath();

        String fileName = JOptionPane.showInputDialog(null,
                "Enter file name (e.g. data.csv):");
        if (fileName != null && !fileName.trim().isEmpty()) {
          fileName = folderPath + File.separator + fileName;
        } else {
          JOptionPane.showMessageDialog(null, "Please enter a valid file name.");
        }
        try {
          calendarGUIManager.exportCalendar(fileName);
        } catch (IOException ex) {
          JOptionPane.showMessageDialog(selectFrame, "Error in exporting CSV file",
                  "CSV Export Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    importButton.addActionListener(e -> {
      JFileChooser fileChooser = new JFileChooser();
      int result = fileChooser.showOpenDialog(selectFrame);

      if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        parseCSV(selectFrame, selectedFile);
      }
    });
    selectFrame.setVisible(true);
  }

  private void parseCSV(JFrame selectFrame, File file) {
    Map<String, Object> importedData = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      br.readLine();
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        importedData.put(EventKeys.SUBJECT, values[0]);
        LocalDateTime dateTime = LocalDateTime.parse(values[1] + "T" + values[2],
                DateTimeFormatter.ISO_DATE_TIME);
        importedData.put(EventKeys.START_DATETIME, dateTime);
        dateTime = LocalDateTime.parse(values[3] + "T" + values[4],
                DateTimeFormatter.ISO_DATE_TIME);
        importedData.put(EventKeys.END_DATETIME, dateTime);
        String str = values[5];
        if (str.equalsIgnoreCase("true")) {
          importedData.put(EventKeys.EVENT_TYPE, EventKeys.EventType.RECURRING);
        } else {
          importedData.put(EventKeys.EVENT_TYPE, EventKeys.EventType.SINGLE);
        }
        importedData.put(EventKeys.DESCRIPTION, values[6]);
        importedData.put(EventKeys.LOCATION, values[7]);
        calendarGUIManager.importCalendar(importedData);
      }
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(selectFrame, "Error in importing CSV file", "CSV Import Error",
              JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(selectFrame, "Calendar not free. Please check",
              "CSV Import Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void updateCalendar() {
    calendarPanel.removeAll();
    monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

    String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    for (String day : days) {
      calendarPanel.add(new JLabel(day, SwingConstants.CENTER));
    }
    LocalDate firstDay = currentMonth.atDay(1);
    int firstDayOfWeek = firstDay.getDayOfWeek().getValue() % 7;
    int daysInMonth = currentMonth.lengthOfMonth();

    for (int i = 0; i < firstDayOfWeek; i++) {
      calendarPanel.add(new JLabel(""));
    }

    for (int day = 1; day <= daysInMonth; day++) {
      LocalDate date = currentMonth.atDay(day);
      JButton dayButton = new JButton(String.valueOf(day));
      dayButton.addActionListener(e -> viewEvents(date));
      calendarPanel.add(dayButton);
    }

    calendarPanel.revalidate();
    calendarPanel.repaint();
  }

  private void changeMonth(int offset) {
    currentMonth = currentMonth.plusMonths(offset);
    updateCalendar();
  }

  private void viewEvents(LocalDate date) {
    JFrame frame = new JFrame("Calendar Application");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setLayout(new BorderLayout());
    frame.setResizable(true);
    List<Map<String, Object>> events = CalendarFactory.getModel().getEventForDisplay(date, null);

    JTextArea eventDisplayArea = new JTextArea();
    eventDisplayArea.setEditable(false);
    eventDisplayArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

    StringBuilder sb = new StringBuilder();
    if (events != null && !events.isEmpty()) {
      LocalDateTime endDateTime = null;
      LocalDate endDate = null;
      String endtime = "--";
      for (Map<String, Object> event : events) {
        String subject = (String) event.get(EventKeys.SUBJECT);
        LocalDateTime eventDate = (LocalDateTime) event.get(EventKeys.START_DATETIME);
        String time = eventDate.toString().split("T")[1];
        if (event.get(EventKeys.END_DATETIME) != null) {
          endDateTime = ((LocalDateTime) event.get(EventKeys.END_DATETIME));
          endDate = endDateTime.toLocalDate();
        } else if (event.get(EventKeys.REPEAT_DATETIME) != null) {
          endDateTime = ((LocalDateTime) event.get(EventKeys.REPEAT_DATETIME));
          endDate = endDateTime.toLocalDate();
        }
        if (endDate != null) {
          endtime = endDateTime.toString().split("T")[1];
        }
        String type = (String) event.get(EventKeys.EVENT_TYPE);

        sb.append("Subject: ").append(subject).append("\n")
                .append("Start Date: ").append(eventDate.toLocalDate()).append("\n")
                .append("Start Time: ").append(time).append("\n")
                .append("End Date: ").append(endDate).append("\n")
                .append("End Time: ").append(endtime).append("\n")
                .append("Is Private?: ").append(type).append("\n\n");
      }

      eventDisplayArea.setText(sb.toString());

      JScrollPane scrollPane = new JScrollPane(eventDisplayArea);
      scrollPane.setPreferredSize(new Dimension(400, 200));

      JOptionPane.showMessageDialog(frame, scrollPane, "Events on " + date,
              JOptionPane.INFORMATION_MESSAGE);
    } else {

      JOptionPane.showMessageDialog(frame, "No event on this day", "Events on " + date,
              JOptionPane.WARNING_MESSAGE);
    }
  }

  private void addEvents() {
    JLabel nameLabel = new JLabel("Event Name:");
    JTextField nameField = new JTextField(20);

    JLabel locationLabel = new JLabel("Location:");
    JTextField locField = new JTextField(20);

    JLabel descLabel = new JLabel("Description:");
    JTextField descField = new JTextField(20);

    JLabel privateLabel = new JLabel("Is Private?:");
    JRadioButton yesButton = new JRadioButton("Yes");
    JRadioButton noButton = new JRadioButton("No");
    ButtonGroup privateGroup = new ButtonGroup();
    privateGroup.add(yesButton);
    privateGroup.add(noButton);
    JPanel privatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    privatePanel.add(yesButton);
    privatePanel.add(noButton);

    JLabel isAllDayLabel = new JLabel("Is AllDay?:");
    JComboBox<String> isAllDayBox = new JComboBox<>(new String[]{"No", "Single AllDay",
            "Recurring AllDay"});

    JLabel recurring = new JLabel("Recurring Days:");

    JCheckBox mon = new JCheckBox("M");
    JCheckBox tue = new JCheckBox("T");
    JCheckBox wed = new JCheckBox("W");
    JCheckBox thu = new JCheckBox("R");
    JCheckBox fri = new JCheckBox("F");
    JCheckBox sat = new JCheckBox("S");
    JCheckBox sun = new JCheckBox("U");

    JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    checkboxPanel.add(mon);
    checkboxPanel.add(tue);
    checkboxPanel.add(wed);
    checkboxPanel.add(thu);
    checkboxPanel.add(fri);
    checkboxPanel.add(sat);
    checkboxPanel.add(sun);


    JLabel startDateLabel = new JLabel("Start DateTime:");
    JTextField startDateField = new JTextField(20);
    JLabel endDate = new JLabel("End DateTime:");
    JTextField endDateField = new JTextField(20);

    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");

    JFrame frame = new JFrame("Add Event");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(450, 450);
    frame.setLayout(new GridBagLayout());
    frame.setResizable(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;

    gbc.gridx = 0;
    gbc.gridy = 0;
    frame.add(nameLabel, gbc);
    gbc.gridx = 1;
    frame.add(nameField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    frame.add(locationLabel, gbc);
    gbc.gridx = 1;
    frame.add(locField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    frame.add(descLabel, gbc);
    gbc.gridx = 1;
    frame.add(descField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    frame.add(startDateLabel, gbc);
    gbc.gridx = 1;
    frame.add(startDateField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    frame.add(endDate, gbc);
    gbc.gridx = 1;
    frame.add(endDateField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 5;
    frame.add(privateLabel, gbc);
    gbc.gridx = 1;
    frame.add(privatePanel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 6;
    frame.add(isAllDayLabel, gbc);
    gbc.gridx = 1;
    frame.add(isAllDayBox, gbc);

    gbc.gridx = 0;
    gbc.gridy = 7;
    frame.add(recurring, gbc);
    gbc.gridx = 1;
    frame.add(checkboxPanel, gbc);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

    gbc.gridx = 0;
    gbc.gridy = 8;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    frame.add(buttonPanel, gbc);

    Map<String, String> details = new HashMap<>();
    saveButton.addActionListener(e -> {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
      try {
        LocalDateTime startDate = LocalDateTime.parse(startDateField.getText(), formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateField.getText(), formatter);

        details.put(EventKeys.SUBJECT, nameField.getText());
        details.put(EventKeys.LOCATION, locField.getText());
        details.put(EventKeys.DESCRIPTION, descField.getText());
        details.put(EventKeys.PRIVATE, privateLabel.getText());
        String isAll = isAllDayBox.getSelectedItem().toString();

        if (!isAll.equals("No")) {
          if (isAll.equals("Single AllDay")) {
            details.put(EventKeys.START_DATETIME, formatter.format(startDate));
          } else {
            details.put(EventKeys.START_DATETIME, formatter.format(startDate));
            details.put(EventKeys.REPEAT_DATETIME, formatter.format(endDateTime));

            StringBuilder selectedDays = new StringBuilder();
            if (mon.isSelected()) selectedDays.append("M");
            if (tue.isSelected()) selectedDays.append("T");
            if (wed.isSelected()) selectedDays.append("W");
            if (thu.isSelected()) selectedDays.append("R");
            if (fri.isSelected()) selectedDays.append("F");
            if (sat.isSelected()) selectedDays.append("S");
            if (sun.isSelected()) selectedDays.append("U");

            if (selectedDays.length() > 0) {
              details.put(EventKeys.WEEKDAYS, selectedDays.toString());
            }
          }
        } else {
          details.put(EventKeys.START_DATETIME, formatter.format(startDate));
          details.put(EventKeys.END_DATETIME, formatter.format(endDateTime));
        }
        details.put(EventKeys.EVENT_TYPE, isAll);
        calendarGUIManager.addEvent(details);
        frame.dispose();

      } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(frame,
                "Please enter date and time in format: yyyy-MM-ddTHH:mm (e.g., 2025-04-07T14:30)",
                "Invalid DateTime Format",
                JOptionPane.ERROR_MESSAGE);
      } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(frame, "Calendar busy on the date ", "BUSY!",
                JOptionPane.ERROR_MESSAGE);
      }
    });
    cancelButton.addActionListener(e -> frame.dispose());

    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private void editEvents() {
    JLabel nameLabel = new JLabel("Event Name:");
    JTextField nameField = new JTextField(20);

    JLabel newNameLabel = new JLabel("New event Name:");
    JTextField newNameField = new JTextField(20);

    JLabel locationLabel = new JLabel("New Location:");
    JTextField locField = new JTextField(20);

    JLabel descLabel = new JLabel("New Description:");
    JTextField descField = new JTextField(20);

    JLabel startDateLabel = new JLabel("from DateTime:");
    JTextField startDateField = new JTextField(20);
    JLabel endDate = new JLabel("to DateTime:");
    JTextField endDateField = new JTextField(20);

    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");

    JFrame frame = new JFrame("Edit Event");
    frame.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;

    int row = 0;
    gbc.gridy = row;
    gbc.gridx = 0;
    frame.add(nameLabel, gbc);
    gbc.gridx = 1;
    frame.add(nameField, gbc);

    gbc.gridx = 1;
    gbc.gridy = ++row;
    frame.add(new JLabel("OR"), gbc);

    gbc.gridy = ++row;
    gbc.gridx = 0;
    frame.add(new JLabel("Please enter in format: yyyy-MM-ddTHH:mm"), gbc);
    gbc.gridy = ++row;
    gbc.gridx = 0;
    frame.add(startDateLabel, gbc);
    gbc.gridx = 1;
    frame.add(startDateField, gbc);

    gbc.gridy = ++row;
    gbc.gridx = 0;
    frame.add(endDate, gbc);
    gbc.gridx = 1;
    frame.add(endDateField, gbc);

    gbc.gridy = ++row;
    gbc.gridx = 0;
    frame.add(new JLabel("Editing properties:"), gbc);

    gbc.gridy = ++row;
    gbc.gridx = 0;
    frame.add(newNameLabel, gbc);
    gbc.gridx = 1;
    frame.add(newNameField, gbc);

    gbc.gridy = ++row;
    gbc.gridx = 0;
    frame.add(locationLabel, gbc);
    gbc.gridx = 1;
    frame.add(locField, gbc);

    gbc.gridy = ++row;
    gbc.gridx = 0;
    frame.add(descLabel, gbc);
    gbc.gridx = 1;
    frame.add(descField, gbc);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

    gbc.gridy = ++row;
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    frame.add(buttonPanel, gbc);

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    cancelButton.addActionListener(e -> frame.dispose());
    Map<String, String> details = new HashMap<>();
    saveButton.addActionListener(e -> {
      DateTimeFormatter formatter =
              DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
      try {
        LocalDateTime startDate = null;
        LocalDateTime endDateTime = null;
        if (!startDateField.getText().equalsIgnoreCase("")) {
          startDate = LocalDateTime.parse(startDateField.getText(), formatter);
          details.put(EventKeys.START_DATETIME, startDate.toString());
        }
        if (!endDateField.getText().equalsIgnoreCase("")) {
          endDateTime = LocalDateTime.parse(endDateField.getText(), formatter);
          details.put(EventKeys.END_DATETIME, endDateTime.format(formatter));
        }
        if (!nameField.getText().equalsIgnoreCase("")) {
          details.put(EventKeys.SUBJECT, nameField.getText());
        }
        if (!descField.getText().equalsIgnoreCase("")) {
          details.put(EventKeys.LOCATION, locField.getText());
        }
        if (!newNameField.getText().equalsIgnoreCase("")) {
          details.put(EventKeys.NEW_VALUE, newNameField.getText());
        }
        calendarGUIManager.editEvent(details);
        frame.dispose();

      } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(frame,
                "Wrong Date format. yyyy-MM-ddTHH:mm (e.g., 2025-04-07T14:30)",
                "Invalid DateTime Format",
                JOptionPane.ERROR_MESSAGE);
      }
    });
  }
}
