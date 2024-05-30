package base;

import com.browserstack.utils.UtilityMethods;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.cucumber.java.Scenario;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.nio.file.Paths;
import java.time.Duration;

import java.util.logging.Logger;

@Slf4j
public class PlaywrightDriver {
    @Getter
    public BrowserContext browserContext;
    @Getter
    public Page page;
    @Setter
    @Getter
    private Duration defaultTimeout = Duration.ofSeconds(30);
    @Setter
    @Getter
    private Duration defaultPollInterval = Duration.ofSeconds(1);
    private static final Logger logger = Logger.getLogger(PlaywrightDriver.class.getName());

    private Playwright playwright;
    private Browser browser;
    /**
     * Enables custom driver and waiting set up.
     * @param page configured playwright page instance.
     */
    public PlaywrightDriver(Page page) {
        this.page = page;
        configure();
    }
    public PlaywrightDriver(String browserName) {
        playwright = Playwright.create();
        if(UtilityMethods.getBrowserstackEnabled())
        {
            initializeRemoteDriver();
        }
        else {
            switch (browserName.toUpperCase()) {
                case "CHROME":
                    initializeChromeDriver();
                    break;
                case "CHROME_HEADLESS":
                    initializeHeadlessChromeDriver();
                    break;
                case "FIREFOX":
                    initializeFirefoxDriver();
                    break;
                case "FIREFOX_HEADLESS":
                    initializeHeadlessFirefoxDriver();
                    break;
                case "WEBKIT":
                    initializeWebkitDriver();
                    break;
                case "WEBKIT_HEADLESS":
                    initializeHeadlessWebkitDriver();
                    break;
            }
        }
        configure();
    }
    /**
     * Start the Chrome browse.
     */
    private void initializeChromeDriver() {
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }
    /**
     * Start the Chrome browse in headless mode.
     */
    private void initializeHeadlessChromeDriver() {
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }
    /**
     * Start the remote browse (Browserstack).
     */
    private void initializeRemoteDriver() {
        // Implement BrowserStack integration for Playwright if available.
    }
    /**
     * Start the firefox browse.
     */
    private void initializeFirefoxDriver() {
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }
    /**
     * Start the firefox browse in headless mode.
     */
    private void initializeHeadlessFirefoxDriver() {
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }
    /**
     * Start the webkit browse.
     */
    private void initializeWebkitDriver() {
        browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }
    /**
     * Start the webkit browse in headless mode.
     */
    private void initializeHeadlessWebkitDriver() {
        browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(true));
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }
    private void loggingSuppressors() {
        // Add logging suppressors if necessary
    }
    /**
     * Gets the current Page.
     */
    public Page getPage() {
        return this.page;
    }
    //=================================================================== locator Free methods =========================================================
    public void navigateToURL(String URL) {
        try {
            page.navigate(URL);
        } catch (Exception e) {
            Assert.fail(String.valueOf(URL.equalsIgnoreCase(page.url())));
            log.error(e.getLocalizedMessage(), e);
        }
    }
    public String getCurrentWindowHandle() {
        String id = "";
        try {
            id = page.context().pages().get(0).url();
        } catch (Exception e) {
            Assert.fail("Failed to get current window handle.");
            log.error(e.getLocalizedMessage(), e);
        }
        return id;
    }
    public void navigateBack() {
        try {
            page.goBack();
        } catch (Exception e) {
            Assert.fail("Failed to navigate back to the previous page");
            log.error(e.getLocalizedMessage(), e);
        }
    }
    public void navigateForward() {
        try {
            page.goForward();
        } catch (Exception e) {
            Assert.fail("Failed to navigate forward to the next page");
            log.error(e.getLocalizedMessage(), e);
        }
    }
    public void refreshBrowserPage() {
        try {
            page.reload();
        } catch (Exception e) {
            Assert.fail("Failed to reload the page");
            log.error(e.getLocalizedMessage(), e);
        }
    }
    public void closeFocusedBrowserTab() {
        try {
            page.close();
        } catch (Exception e) {
            Assert.fail("Failed to close browser instance");
        }
    }
    public byte[] getByteScreenshot() {
        byte[] fileContent = null;
        try {
            fileContent = page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot.png")));
        } catch (Exception e) {
            System.out.println("Failed to create screenshot");
        }
        return fileContent;
    }
    public void acceptAlert() {
        try {
            page.onDialog(Dialog::accept);
        } catch (Exception e) {
            Assert.fail("Failed to accept popup alert");
        }
    }
    public void closeBrowserInstance() {
        try {
            browser.close();
        } catch (Exception e) {
            Assert.fail("Failed to quit browser instance");
        }
    }
    public String getTitle() {
        try {
            return page.title();
        } catch (Exception e) {
            Assert.fail("Failed to get the title of the page");
        }
        return null;
    }
    public String getCurrentUrl() {
        try {
            return page.url();
        } catch (Exception e) {
            Assert.fail("Failed to get the URL of the page");
        }
        return null;
    }
    public void captureScreenshot(Scenario scenario) {
        try {
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            scenario.attach(screenshot, "image/png", scenario.getName());
        } catch (Exception e) {
            Assert.fail("Failed to capture screenshot");
        }
    }
    //======================================================================== locators ===============================================================
    private Locator getElementIfVisible(String locator) {
        try {
            Locator element = page.locator(locator);
            element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            return element;
        } catch (Exception e) {
            Assert.fail("The element is either not visible or clickable: " + locator, e);
        }
        return null;
    }

    private Locator getElementIfClickable(String locator) {
        try {
            Locator element = page.locator(locator);
            element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(30000));
            return element;
        } catch (Exception e) {
            Assert.fail("Failed to get clickable element: " + locator, e);
        }
        return null;
    }
    public boolean isElementDisplayed(String locator) {
        try {
            return page.locator(locator).isVisible();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isElementDisplayedWithTimeout(String locator, int timeInSecondsToWaitForElement) {
        try {
            page.waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(timeInSecondsToWaitForElement * 1000));
            return page.locator(locator).isVisible();
        } catch (Exception e) {
            return false;
        }
    }
    public void clickByLocatorIndex(String locator, int locatorIndex) {
        try {
            page.locator(locator).nth(locatorIndex).click();
        } catch (Exception e) {
            Assert.fail("Failed to click on the element");
        }
    }
    public void click(String locator) {
        getElementIfClickable(locator).click();
    }
    public void clearText(String locator) {
        try {
            getElementIfVisible(locator).fill("");
        } catch (Exception e) {
            Assert.fail("Failed to clear text from element");
        }
    }
    public void enterText(String locator, String text) {
        try {
            Locator element = getElementIfClickable(locator);
            element.click();
            element.fill(text);
        } catch (Exception e) {
            Assert.fail("Failed to enter text into element | Text to be entered: " + text);
        }
    }
    public void uploadFile(String locator, String filePath) {
        try {
            page.setInputFiles(locator, Paths.get(filePath));
        } catch (Exception e) {
            Assert.fail("Failed to upload file");
        }
    }
    public String getTextAttribute(String locator) {
        try {
            return getElementIfVisible(locator).getAttribute("value");
        } catch (Exception e) {
            Assert.fail("Failed to get text attribute 'value'");
            return "";
        }
    }
    public String getText(String locator) {
        try {
            return getElementIfVisible(locator).innerText();
        } catch (Exception e) {
            Assert.fail("Failed to get text");
            return "";
        }
    }
    // Wait for an element with exact text
    public void waitForText(String locator, String text, int timeToWaitInSeconds) {
        try {
            page.waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(timeToWaitInSeconds * 1000).setState(WaitForSelectorState.ATTACHED));
            getText(locator).equals(text);
        } catch (Exception e) {
            Assert.fail("Failed to wait for text");
        }
    }
    // Wait for an element containing specific text
    public void waitForTextContaining(String locator, String text, int timeToWaitInSeconds) {
        try {
            page.waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(timeToWaitInSeconds * 1000).setState(WaitForSelectorState.ATTACHED));
            getText(locator).contains(text);
        } catch (Exception e) {
            Assert.fail("Failed to wait for text containing");
        }
    }
    public void getAndCompareText(String locator, String textToCompare) {
        try {
            String retrievedText = getText(locator);
            if (!retrievedText.equals(textToCompare)) {
                Assert.fail("The actual text retrieved and the expected text do not match!");
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
    public void pauseExecution(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
    public void enterKeyCombination(String locator, String keyCombination) {
        try {
            Locator element = getElementIfClickable(locator);
            switch (keyCombination.toUpperCase()) {
                case "TAB" -> element.press("Tab");
                case "ENTER" -> element.press("Enter");
                case "F5" -> element.press("F5");
                case "ALT" -> element.press("Alt");
                case "ESCAPE" -> element.press("Escape");
                case "DELETE" -> element.press("Delete");
                case "PAGE UP" -> element.press("PageUp");
                case "PAGE DOWN" -> element.press("PageDown");
                case "LEFT_CONTROL" -> element.press("Control");
                case "SPACE" -> element.press("Space");
                case "BACKSPACE" -> element.press("Backspace");
                case "LEFT_SHIFT" -> element.press("Shift");
            }
        } catch (Exception e) {
            Assert.fail("Failed to send keys.");
        }
    }
    public void switchToCurrentBrowserTab() {
        try {
            page.context().pages().get(0).bringToFront();
        } catch (Exception e) {
            Assert.fail("Failed to switch to current browser tab");
        }
    }
    public void switchToBrowserTabByTitle(String title) {
        try {
            page.context().pages().stream().filter(p -> p.title().equals(title)).findFirst().ifPresent(Page::bringToFront);
        } catch (Exception e) {
            Assert.fail("Failed to switch tab by title");
        }
    }
    public void switchToBrowserTabByIndex(int index) {
        try {
            page.context().pages().get(index).bringToFront();
        } catch (Exception e) {
            Assert.fail("Failed to switch to Tab by index");
        }
    }
    public void scrollHorizontallyByAGivenAmount(String locator, int xAxisAmount) {
        try {
            Locator element = getElementIfVisible(locator);
            page.mouse().wheel(xAxisAmount, 0);
        } catch (Exception e) {
            Assert.fail("Failed to scroll horizontally by amount specified");
        }
    }
    public void scrollVerticallyByAGivenAmount(String locator, int yAxisAmount) {
        try {
            Locator element = getElementIfVisible(locator);
            page.mouse().wheel(0, yAxisAmount);
        } catch (Exception e) {
            Assert.fail("Failed to scroll vertically by amount specified");
        }
    }
    public void scrollToElement(String locator) {
        try {
            Locator element = getElementIfVisible(locator);
            element.scrollIntoViewIfNeeded();
        } catch (Exception e) {
            Assert.fail("Failed to scroll to element");
        }
    }
    public void clickElementAndHold(String locator) {
        try {
            Locator clickable = getElementIfClickable(locator);
            page.mouse().down();
        } catch (Exception e) {
            Assert.fail("Failed to click and hold element");
        }
    }
    public void doubleClickElement(String locator) {
        try {
            Locator clickable = getElementIfClickable(locator);
            clickable.dblclick();
        } catch (Exception e) {
            Assert.fail("Failed to double click on element");
        }
    }
    public void moveMouseCursorToElement(String locator) {
        try {
            Locator hoverable = getElementIfClickable(locator);
            hoverable.hover();
        } catch (Exception e) {
            Assert.fail("Failed to move the mouse cursor to the center of the element.");
        }
    }
    public void dragAndDropElement(String dragLocator, String dropLocator) {
        try {
            Locator draggable = getElementIfClickable(dragLocator);
            Locator droppable = getElementIfClickable(dropLocator);
            draggable.dragTo(droppable);
        } catch (Exception e) {
            Assert.fail("Failed to drag the element to the drop location.");
        }
    }
    /**
     * This will interact with the accept button on a generic popup on a website.
     */
    public void clickByJavascriptUsingLocator(String locator, int locatorIndex) {
        Locator element = page.locator(locator).nth(locatorIndex);
        clickByJS(element);
    }
    public void clickByJavascriptUsingLocator(Locator locator, int locatorIndex) {
        Locator element = locator.nth(locatorIndex);
        clickByJS(element);
    }
    /**
     * Waiting for the webelement be present on the page under the specified locator and clicking it by executing a JS function in the browser.
     * @param locator the locator of the element to click.
     */
    public void clickByJS(String locator) {
        try {
            page.locator(locator).evaluate("element => element.click()");
        } catch (Throwable t) {
            Assert.fail("Unable to click element located by %s by executing Javascript: %s".formatted(locator, t.getMessage()), t);
        }
    }
    /**
     * Clicking the passed webelement by executing a JS function in the browser.
     * @param locator the element to click.
     */
    public void clickByJS(Locator locator) {
        try {
            locator.evaluate("element => element.click()");
        } catch (Throwable t) {
            Assert.fail("Unable to click element by executing Javascript: %s".formatted(t.getMessage()), t);
        }
    }
    /**
     * This closes the browser instance and ends the test.
     */
    public void selectFromDropdownUsingVisibleText(String locator, String visibleTextToSelect) {
        try {
            page.selectOption(locator, visibleTextToSelect);
        } catch (Exception e) {
            Assert.fail("Failed to select item from dropdown list using visible text");
        }
    }
    /**
     * This will scroll the webElement down.
     */
    public void scrollElementIntoView(String locator,String trueForDownFalseForUp)
    {
        try {
            Locator element = page.locator(locator);
            boolean alignToTop = Boolean.parseBoolean(trueForDownFalseForUp);
            element.scrollIntoViewIfNeeded(new Locator.ScrollIntoViewIfNeededOptions().setTimeout(alignToTop ? 0 : 1000));
        } catch (Exception e) {
            Assert.fail("Failed to scroll element into view");
        }
    }
    private void configure() {
        loggingSuppressors();
        waitForPageLoadUsingJavascript();
    }
    public void waitForPageLoadUsingJavascript() {
        page.waitForLoadState(LoadState.LOAD);
    }

}
