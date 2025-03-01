package calendar.controller;

import java.util.Scanner;

public class CalendarController {

  private void processCommand(String command) {

    
  }
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input;

    do {
      System.out.println("Enter calender command (or 'exit' to quit):");
      input = scanner.nextLine();

      if (!input.equalsIgnoreCase("exit")) {
        System.out.println("Executing command: " + input);
      }
    } while (!input.equalsIgnoreCase("exit"));

    System.out.println("Exiting application.");
    scanner.close();
  }
}
