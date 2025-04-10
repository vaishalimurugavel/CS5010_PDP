package calendar.gui;

import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

import calendar.controller.CalendarFactory;
import calendar.model.CalendarGroupManager;
import calendar.model.CalenderEventManager;

/**
 * CalendarUIManager extends JFrame, displays the main backbone of the GUI view.
 */
public class CalendarUIManager extends JFrame {
  private JFrame frame;

  /**
   * Constructor initializes the CalendarApplication.
   */
  public CalendarUIManager() {
    CalendarFactory.setModel(new CalenderEventManager());
    CalendarFactory.setGroup(new CalendarGroupManager());
    init();
  }

  private static void showAddCalendarDialog(JFrame parentFrame) {
    CalendarAddDisplay calendarAddDisplay = new CalendarAddDisplay();
    calendarAddDisplay.showDisplay(parentFrame);
  }

  private static void showEditCalendarDialog(JFrame parentFrame) {
    CalendarGUIInterface edit = new CalendarEditDisplay();
    edit.showDisplay(parentFrame);
  }

  private void init() {
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

    JLabel title = new JLabel("WELCOME TO CALENDAR APPLICATION!");
    JLabel opt = new JLabel("Please select the option you would like to use:");
    JButton addButton = new JButton("Add calendar");
    JButton editButton = new JButton("Edit calendar");
    JButton selectButton = new JButton("Select calendar");

    gbc.gridy = 1;
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(selectButton);

    panel.add(title, gbc);
    gbc.gridy = 3;
    panel.add(opt, gbc);
    gbc.gridy = 4;
    panel.add(buttonPanel, gbc);

    frame.add(panel, BorderLayout.NORTH);
    frame.setVisible(true);
    addButton.addActionListener(e -> {
      showAddCalendarDialog(frame);
    });
    editButton.addActionListener(e -> {
      showEditCalendarDialog(frame);
    });
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

  private void showSelectCalendarDialog(JFrame parentFrame) {
    CalendarGUIInterface event = new CalendarEventDisplay();
    event.showDisplay(parentFrame);
  }

}
