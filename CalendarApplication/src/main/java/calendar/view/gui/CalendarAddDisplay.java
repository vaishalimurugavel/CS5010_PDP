package calendar.view.gui;


import java.awt.*;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.swing.*;

/**
 * Created at 05-04-2025
 * Author Vaishali
 **/

public class CalendarAddDisplay implements CalendarGUIInterface {
  CalendarGUIManager calendarGUIManager = new CalendarGUIManager();
  @Override
  public void showDisplay(JFrame parentFrame) {
    JDialog dialog = new JDialog(parentFrame, "Add Calendar", true);
    dialog.setSize(300, 200);
    dialog.setLayout(new GridLayout(3, 2, 5, 5));

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

    saveButton.addActionListener(e -> {
      String calendarName = nameField.getText();
      String timezone = dropdown2.getSelectedItem().toString();
      if (!calendarName.isEmpty() && !timezone.isEmpty()) {
        JOptionPane.showMessageDialog(dialog,
                "Calendar Created: " + calendarName + " (" + timezone + ")",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        dialog.dispose();
        calendarGUIManager.addCalendar(calendarName,timezone);
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
}
