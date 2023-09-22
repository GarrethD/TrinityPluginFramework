package utilities;

import com.google.gson.Gson;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PageLoadTimer {

    private Map<String, Long> pageLoadTimes;
    String pageTitle;
    public PageLoadTimer() {
        this.pageLoadTimes = new HashMap<>();
    }

    /**
     * Records the load time of a web page.
     *
     * @param driver the WebDriver instance to use for executing JavaScript code
     */
    public void recordLoadTime(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long navigationStart = (Long) js.executeScript("return window.performance.timing.navigationStart");
        Long loadEventEnd = (Long) js.executeScript("return window.performance.timing.loadEventEnd");
        Long loadTime = loadEventEnd - navigationStart;
        String pageTitle = driver.getTitle();
        pageLoadTimes.put(pageTitle, loadTime);
    }

    /**
     * Writes the recorded page load times to a JSON file.
     * The JSON file will be named "pageloadtimes.json" and will be saved in the "./ExtentReports/{pageTitle}" directory.
     * If the directory does not exist, it will be created.
     *
     * This method uses the Gson library to convert the pageLoadTimes map to a JSON string.
     * The JSON string is then written to the file using a FileWriter.
     *
     * @throws IOException if an I/O error occurs when writing the JSON file
     */
    public void writeLoadTimesToJson() {
        Gson gson = new Gson();
        String json = gson.toJson(pageLoadTimes);

        try (FileWriter file = new FileWriter("./ExtentReports/"+pageTitle+"/pageloadtimes.json")) {
            file.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
