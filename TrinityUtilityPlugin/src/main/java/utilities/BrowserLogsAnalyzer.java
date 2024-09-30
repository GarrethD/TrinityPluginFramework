package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.log.ConsoleLogEntry;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BrowserLogsAnalyzer {
    static LogEntries entry;
    static List<LogEntry> logs = new ArrayList<>();

    /**
     * Retrieves all console logs from the specified WebDriver and writes them to a file.
     *
     * @param driver              The WebDriver instance to retrieve console logs from.
     * @param filePathAndFileName The path and name of the file to write the logs to.
     */
    public static void getAllConsoleLogs(WebDriver driver, String filePathAndFileName) {
        entry = driver.manage().logs().get(LogType.BROWSER);
        logs = entry.getAll();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathAndFileName))) {
            for (LogEntry e : logs) {
                writer.write("Timestamp: " + e.getTimestamp() + ", Level: " + e.getLevel() + ", Message: " + e.getMessage() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
