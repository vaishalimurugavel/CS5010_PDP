package calendar.view.gui;

import java.awt.GridLayout;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.swing.*;

import calendar.controller.CalendarFactory;
import calendar.model.EventKeys;

/**
 * <p>
 *   CalendarEditDisplay displays the options to edit existing calendar.
 * </p>
 **/
public class CalendarEditDisplay implements CalendarGUIInterface {
  CalendarGUIManager calendarGUIManager = new CalendarGUIManager();

  @Override
  public void showDisplay(JFrame parentFrame) {
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

    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");

    dialog.add(nameLabel);
    dialog.add(nameField);
    dialog.add(timezoneLabel);
    dialog.add(dropdown2);
    dialog.add(saveButton);
    dialog.add(cancelButton);

    Map<String, String> edit = new HashMap<>();
    saveButton.addActionListener(e -> {
      String name = dropdown.getSelectedItem().toString();
      String calendarName = nameField.getText();
      String timezone = dropdown2.getSelectedItem().toString();
      if (!calendarName.isEmpty() || !timezone.isEmpty()) {
        JOptionPane.showMessageDialog(dialog,
                "Calendar " + calendarName + "Updated : (" + timezone + ")" ,
                "Success" , JOptionPane.INFORMATION_MESSAGE);
        dialog.dispose();
        if(!calendarName.isEmpty()) {
            edit.put(EventKeys.PROPERTY, EventKeys.CALENDAR_NAME);
            edit.put(EventKeys.NEW_VALUE, calendarName);
        }
        else {
          edit.put(EventKeys.PROPERTY, EventKeys.TIMEZONE);
          edit.put(EventKeys.NEW_VALUE, timezone);
        }
        edit.put(EventKeys.CALENDAR_NAME, name);
        calendarGUIManager.editCalendar(edit);
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
}
