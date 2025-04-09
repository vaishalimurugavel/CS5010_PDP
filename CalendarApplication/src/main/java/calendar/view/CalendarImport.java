package calendar.view;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * Created at 08-04-2025
 * Author Vaishali
 **/

public class CalendarImport {


    private static void createAndShowGUI() {
      JFrame frame = new JFrame("CSV Importer");
      JButton importButton = new JButton("Import CSV");

      importButton.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          List<String[]> parsedData = parseCSV(selectedFile);

          // Print parsed data
          for (String[] row : parsedData) {
            System.out.println(Arrays.toString(row));
          }
        }
      });

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(importButton);
      frame.setSize(300, 100);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    }

    // ✅ Function to parse CSV file
    private static List<String[]> parseCSV(File file) {
      List<String[]> data = new ArrayList<>();
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;

        while ((line = br.readLine()) != null) {
          // Simple split by comma — update if your CSV is more complex (e.g. quoted strings)
          String[] values = line.split(",");
          data.add(values);
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }

      return data;
    }

}
