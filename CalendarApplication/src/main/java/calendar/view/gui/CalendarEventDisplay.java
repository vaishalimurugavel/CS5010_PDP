package calendar.view.gui;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import calendar.controller.CalendarFactory;
import calendar.model.EventKeys;

/**
 * Created at 05-04-2025
 * Author Vaishali
 **/

public class CalendarEventDisplay implements CalendarGUIInterface {
  CalendarGUIManager calendarGUIManager = new CalendarGUIManager();
  JPanel calendarPanel;
  JLabel monthLabel;
  YearMonth currentMonth;

  @Override
  public void showDisplay(JFrame parentFrame) {
    HashMap<LocalDate, List<String>> events = new HashMap<>();
    currentMonth = YearMonth.now();

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

    calendarPanel = new JPanel(new GridLayout(0, 7));
    updateCalendar();

    parentFrame.add(topPanel, BorderLayout.NORTH);
    parentFrame.add(calendarPanel, BorderLayout.CENTER);

    prevMonthButton.addActionListener(e -> changeMonth(-1));
    nextMonthButton.addActionListener(e -> changeMonth(1));
    addEventButton.addActionListener(e -> {
      addEvents();
    });

    parentFrame.setVisible(true);
  }


  private void updateCalendar() {
    calendarPanel.removeAll();
    monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

    // Headers for days
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

      for (Map<String, Object> event : events) {
        String subject = (String) event.get("subject");
        LocalDate eventDate = (LocalDate) event.get("startDateTime");
        String time = event.get("startDateTime").toString().split("T")[1];
        String type = (String) event.get("eventType");

        sb.append("Subject: ").append(subject).append("\n")
                .append("Date: ").append(eventDate).append("\n")
                .append("Time: ").append(time).append("\n")
                .append("Type: ").append(type).append("\n\n");
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
    JComboBox<String> isAllDayBox = new JComboBox<>(new String[] { "No", "Single AllDay",
            "Recurring AllDay" });

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
    SpinnerDateModel model = new SpinnerDateModel();
    JSpinner dateTimeSpinner = new JSpinner(model);
    JSpinner.DateEditor editor = new JSpinner.DateEditor(dateTimeSpinner, "yyyy-MM-dd HH:mm");
    dateTimeSpinner.setEditor(editor);

    JLabel endDate = new JLabel("End DateTime:");
    SpinnerDateModel endModel = new SpinnerDateModel();
    JSpinner endSpinner = new JSpinner(endModel);
    JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endSpinner, "yyyy-MM-dd HH:mm");
    endSpinner.setEditor(endEditor);

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
    frame.add(dateTimeSpinner, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    frame.add(endDate, gbc);
    gbc.gridx = 1;
    frame.add(endSpinner, gbc);

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
      details.put(EventKeys.SUBJECT, nameField.getText());
      details.put(EventKeys.LOCATION, locField.getText());
      details.put(EventKeys.DESCRIPTION, descField.getText());
      details.put(EventKeys.PRIVATE,privateLabel.getText());
      String  isAll = isAllDayBox.getSelectedItem().toString();
      if (!isAll.equals("No")) {
       if (isAll.equals("Single AllDay")) {
         LocalDateTime selectedDate = (LocalDateTime) dateTimeSpinner.getValue();
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddTHH:mm");
         String formattedDate = formatter.format(selectedDate);
         details.put(EventKeys.START_DATETIME, formattedDate);
       }
       else{
         LocalDateTime selectedDate = (LocalDateTime) dateTimeSpinner.getValue();
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddTHH:mm");
         String formattedDate = formatter.format(selectedDate);
         details.put(EventKeys.START_DATETIME, formattedDate);

         LocalDateTime end = (LocalDateTime) dateTimeSpinner.getValue();
         formattedDate = formatter.format(end);
         details.put(EventKeys.REPEAT_DATETIME, formattedDate);

         StringBuilder selectedDays = new StringBuilder("");

         if (mon.isSelected()) {
           selectedDays.append("M");
         }
         if (tue.isSelected()) {
           selectedDays.append("T");
         }
         if (wed.isSelected()) {
           selectedDays.append("W");
         }
         if (thu.isSelected()) {
           selectedDays.append("R");
         }
         if (fri.isSelected()) {
           selectedDays.append("F");
         }
         if (sat.isSelected()) {
           selectedDays.append("S");
         }
         if (sun.isSelected()) {
           selectedDays.append("U");
         }
         if(selectedDays.length()>0){
           details.put(EventKeys.OCCURRENCES, selectedDays.toString());
         }

       }
      }
      else {
        LocalDateTime selectedDate = (LocalDateTime) dateTimeSpinner.getValue();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddTHH:mm");
        String formattedDate = formatter.format(selectedDate);
        details.put(EventKeys.START_DATETIME, formattedDate);

        LocalDateTime end = (LocalDateTime) dateTimeSpinner.getValue();
        formattedDate = formatter.format(end);
        details.put(EventKeys.END_DATETIME, formattedDate);
      }
      calendarGUIManager.addEvent(details);
    });
    cancelButton.addActionListener(e -> frame.dispose());

    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
