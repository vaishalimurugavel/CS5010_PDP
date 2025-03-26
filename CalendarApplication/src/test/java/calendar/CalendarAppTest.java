package calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

/**
 * Test class for CalendarApp.
 */
public class CalendarAppTest {

  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;
  private final InputStream originalIn = System.in;
  private ByteArrayOutputStream outContent;
  private ByteArrayOutputStream errContent;

  @Before
  public void setUpStreams() {
    outContent = new ByteArrayOutputStream();
    errContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
    System.setIn(originalIn);
  }

  @Test
  public void testMain_InvalidArgs() {
    CalendarApp.main(new String[]{});
    String output = errContent.toString();
    assertTrue(output.contains("Usage:"));
  }

  @Test
  public void testMain_InvalidMode() {
    CalendarApp.main(new String[]{"--mode", "invalid"});
    String output = errContent.toString();
    assertTrue(output.contains("Invalid mode"));
  }

  @Test
  public void testMain_MissingFilenameInHeadlessMode() {
    CalendarApp.main(new String[]{"--mode", "headless"});
    String output = errContent.toString();
    assertTrue(output.contains("Error: Missing filename for headless mode."));
  }

  @Test
  public void testMain_FileNotFoundInHeadlessMode() {
    CalendarApp.main(new String[]{"--mode", "headless", "non_existent_file.txt"});
    String output = errContent.toString();
    assertTrue(output.contains("Error: File not found"));
  }

  @Test
  public void testInteractiveMode_ExitCommand() {
    String simulatedInput = "exit\n";
    System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

    CalendarApp.main(new String[]{"--mode", "interactive"});
    String output = outContent.toString();

    assertTrue(output.contains("Entering interactive mode"));
    assertTrue(output.contains("Exiting..."));
  }

  @Test
  public void testHeadlessMode_FileProcessing() throws Exception {
    File tempFile = File.createTempFile("test", ".txt");
    FileWriter writer = new FileWriter(tempFile);
    writer.write("exit\n");
    writer.close();

    CalendarApp.main(new String[]{"--mode", "headless", tempFile.getAbsolutePath()});
    String output = outContent.toString();

    assertTrue(output.contains("Executing command: exit"));
    assertTrue(output.contains("Exiting..."));

    tempFile.delete();
  }
}
