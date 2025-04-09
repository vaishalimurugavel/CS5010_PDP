package calendar.view.gui;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.*;

import calendar.controller.CalendarFactory;
import calendar.model.CalendarGroupManager;
import calendar.model.CalenderEventManager;
import calendar.view.CalendarExport;

public class CalendarUIManager extends JFrame {
  private JFrame frame;
  private JButton addButton, editButton, selectButton;

  public CalendarUIManager() {
    CalendarFactory.setModel(new CalenderEventManager());
    CalendarFactory.setGroup(new CalendarGroupManager());
    try {
      CalendarFactory.setExport(new CalendarExport(new FileOutputStream("tester.csv")));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    init();
  }

  private void init(){
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

    addButton = new JButton("Add calendar");
    editButton = new JButton("Edit calendar");
    selectButton = new JButton("Select calendar");

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

  public static void main(String[] args) {
    new CalendarUIManager();
  }

}
