package utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JavascriptScriptRunner {
    /**
     * Executes a JavaScript file in the given web browser.
     *
     * @param driver The WebDriver instance representing the web browser to execute the script in.
     *
     * @param pathToScriptFile The path to the JavaScript file to be executed.
     */
    public static void RunJavascriptFileInBrowser(WebDriver driver, String pathToScriptFile)
{
    // Read the JavaScript file
    String script = "";
    try {
        script = new String(Files.readAllBytes(Paths.get(pathToScriptFile)));
    } catch (IOException e) {
        e.printStackTrace();
    }
    // Execute the JavaScript
    if (!script.isEmpty()) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script);
    }}
}
