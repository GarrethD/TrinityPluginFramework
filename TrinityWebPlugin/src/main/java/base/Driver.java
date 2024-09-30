package base;

import com.browserstack.config.BrowserStackConfig;
import com.browserstack.utils.UtilityMethods;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.bidi.browsingcontext.BrowsingContext;
import org.openqa.selenium.bidi.browsingcontext.ReadinessState;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Slf4j
public class Driver {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Driver.class);
    @Getter
    public WebDriver webDriver;
    @Getter
    public ShadowDriver shadowDriver;
    private FluentWait<WebDriver> wait;
    @Setter
    @Getter
    private Duration defaultTimeout = Duration.ofSeconds(30);
    @Setter
    @Getter
    private Duration defaultPollInterval = Duration.ofSeconds(1);
    private Actions actions;

    private static final Logger logger = Logger.getLogger(Driver.class.getName());

    /**
     * Enables custom driver and waiting set up.
     *
     * @param webDriver configured webdriver instance.
     */
    public Driver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public Driver(String browserName) {
        if (UtilityMethods.getBrowserstackEnabled()) {
            initializeRemoteDriver();
        } else {
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
                case "EDGE":
                    initializeEdgeDriver();
                    break;
                case "EDGE_HEADLESS":
                    initializeHeadlessEdgeDriver();
                    break;
                case "SAFARI":
                    initializeSafariDriver();
                    break;
            }
            configure();
        }
    }

    /**
     * Start the Chrome browse.
     */
    private void initializeChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.setCapability("webSocketUrl", true);
        ChromeBrowserLogs(options);
        webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();
    }

    /**
     * Start the Chrome browse in headless mode.
     */
    private void initializeHeadlessChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--headless=new");
        options.setCapability("webSocketUrl", true);
        options.setAcceptInsecureCerts(true);
        ChromeBrowserLogs(options);
        webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();
    }

    /**
     * Start the remote browse (Browserstack).
     */
    private void initializeRemoteDriver() {
        try {
            DesiredCapabilities cap = new DesiredCapabilities();
            BrowserStackConfig bConfig = BrowserStackConfig.getInstance();
            String username = bConfig.getUserName();
            String accessToken = bConfig.getAccessKey();
            webDriver = new RemoteWebDriver(new URL("https://" + username + ":" + accessToken + "@hub.browserstack.com/wd/hub"), cap);
            webDriver.manage().window().maximize();
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "Malformed URL Exception occurred", e);
        }
    }

    /**
     * Start the firefox browse.
     */
    private void initializeFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.setCapability("webSocketUrl", true);
        options.addArguments("--window-size=1920,1080");
        FirefoxBrowserLogs(options);
        webDriver = new FirefoxDriver(options);
        webDriver.manage().window().maximize();
    }

    /**
     * Start the firefox browse in headless mode.
     */
    private void initializeHeadlessFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-headless");
        options.setCapability("webSocketUrl", true);
        options.addArguments("window-size=1920,1080");
        FirefoxBrowserLogs(options);
        webDriver = new FirefoxDriver(options);
    }

    /**
     * Start the safari browse.
     */
    private void initializeSafariDriver() {
        System.setProperty("webdriver.safari.driver", "/usr/bin/safaridriver");
        SafariOptions options = new SafariOptions();
        options.setCapability("webSocketUrl", true);
        options.setCapability("safari:useSimulator", false);
        options.setCapability("safari:diagnose", false);
        webDriver = new SafariDriver(options);
        webDriver.manage().window().maximize();
    }

    /**
     * Start the Edge browse.
     */
    private void initializeEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--no-sandbox");
        EdgeBrowserLogs();
        EdgeDriverService service = EdgeDriverService.createDefaultService();
        webDriver = new EdgeDriver(service, options);
        webDriver.manage().window().maximize();
    }

    /**
     * Start the Edge browse in headless mode.
     */
    private void initializeHeadlessEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("window-size=1920,1080");
        EdgeBrowserLogs();
        webDriver = new EdgeDriver(options);
    }

    private void loggingSuppressors() {
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
        Logger.getLogger("org.apache.commons.logging").setLevel(Level.SEVERE);
        Logger.getLogger("io.github.bonigarcia.wdm").setLevel(Level.SEVERE);
    }

    private void ChromeBrowserLogs(ChromeOptions options) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logPrefs);
    }

    private void FirefoxBrowserLogs(FirefoxOptions options) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        logPrefs.enable(LogType.DRIVER, Level.INFO);
        options.setCapability("moz:firefoxOptions", java.util.Collections.singletonMap("log", java.util.Collections.singletonMap("level", "trace")));
        options.setCapability("goog:loggingPrefs", logPrefs);
    }

    private void EdgeBrowserLogs() {
        System.setProperty("webdriver.edge.loglevel", "ALL"); // You can set it to ALL, DEBUG, INFO, WARNING, SEVERE, or OFF
    }

    /**
     * Gets the current Webdriver.
     */
    public WebDriver getDriver() {
        return this.webDriver;
    }

    //=================================================================== locator Free methods =========================================================
    public void navigateToURL(String URL) {
        try {
            webDriver.get(URL);
        } catch (Exception e) {

            Assert.fail(String.valueOf(URL.equalsIgnoreCase(webDriver.getCurrentUrl())));
            log.error(e.getLocalizedMessage(), e);
        }
    }

    void navigateToUrlWithReadinessState(String url, ReadinessState readinessState) {
        BrowsingContext navigateContext;
        try {
            navigateContext = new BrowsingContext(webDriver, WindowType.TAB);
            navigateContext.navigate(url, readinessState);
        } catch (Exception e) {
            Assert.fail("Failed to navigate to url: " + url + " or failed to wait for readiness state");
            log.error(e.getLocalizedMessage(), e);
        }

    }

    public String getCurrentWindowHandle() {
        String id = "";
        try {
            id = webDriver.getWindowHandle();
            BrowsingContext currentWindowHandleContext = new BrowsingContext(webDriver, id);
        } catch (Exception e) {
            Assert.fail("Failed to get current window handle.");
            log.error(e.getLocalizedMessage(), e);
        }
        return id;
    }

    public void navigateBack() {
        try {
            webDriver.navigate().back();
        } catch (Exception e) {
            Assert.fail("Failed to navigate back to the previous page");
            log.error(e.getLocalizedMessage(), e);
        }
    }

    public void navigateForward() {
        try {
            webDriver.navigate().forward();
        } catch (Exception e) {
            Assert.fail("Failed to navigate forward to the next page");
            log.error(e.getLocalizedMessage(), e);
        }
    }

    public void refreshBrowserPage() {
        try {
            webDriver.navigate().refresh();
        } catch (Exception e) {
            Assert.fail("Failed to navigate forward to the next page");
            log.error(e.getLocalizedMessage(), e);
        }
    }

    public void closeFocusedBrowserTab() {
        try {
            webDriver.close();
        } catch (Exception e) {
            Assert.fail("Failed to close browser instance");
        }
    }

    public byte[] getByteScreenshot() {
        byte[] fileContent = null;
        try {
            File src = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            fileContent = FileUtils.readFileToByteArray(src);
        } catch (Exception e) {
            System.out.println("Failed to create screenshot");
        }
        return fileContent;
    }

    public void acceptAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            webDriver.switchTo().alert().accept();
        } catch (Exception e) {

            Assert.fail("Failed to accept popup alert");
        }
    }

    public void closeBrowserInstance() {
        try {
            webDriver.quit();
        } catch (Exception e) {

            Assert.fail("Failed to quit browser instance");
        }
    }

    public String getTitle() {
        try {
            return webDriver.getTitle();
        } catch (Exception e) {

            Assert.fail("Failed to get the title of the page");
        }
        return null;
    }

    public String getCurrentUrl() {
        try {
            return webDriver.getCurrentUrl();
        } catch (Exception e) {

            Assert.fail("Failed to get the title of the page");
        }
        return null;
    }

    public void captureScreenshot(Scenario scenario) {
        byte[] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", scenario.getName());
    }

    //======================================================================== locators ===============================================================
//    private WebElement getElementIfVisible(By locator) {
//
//        if(Boolean.FALSE.equals(wait.until(ExpectedConditions.and(ExpectedConditions.elementToBeClickable(locator), ExpectedConditions.visibilityOfElementLocated(locator)))))
//        {
//            Assert.fail("The element is either not visible or clickable ");
//        }
//        return webDriver.findElement(locator);
//
//    }
    private WebElement getElementIfVisible(By locator) {

        Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(500));
        WebElement currentWebelement = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        });
        return currentWebelement;
    }

    public List<WebElement> getElementsIfVisible(By locator) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

        List<WebElement> currentWebElements = wait.until(new Function<WebDriver, List<WebElement>>() {
            public List<WebElement> apply(WebDriver driver) {
                List<WebElement> elements = driver.findElements(locator);
                if (elements.isEmpty()) {
                    return null;
                } else {
                    return elements.stream().filter(WebElement::isDisplayed).collect(Collectors.toList());
                }
            }
        });
        return currentWebElements;
    }

    private WebElement getElementIfClickable(By locator) {
        Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(Throwable.class);

        return wait.until(driver -> {
            WebElement element = driver.findElement(locator);
            return (element != null && element.isEnabled() && element.isDisplayed()) ? element : null;
        });
    }


    public boolean isElementDisplayedWithTimeout(By locator, int timeInSecondsToWaitForElement) {
        try {
            WebDriverWait methodWait = new WebDriverWait(webDriver, Duration.ofSeconds(timeInSecondsToWaitForElement));
            return methodWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (NoSuchElementException | TimeoutException ex) {
            return false;
        }
    }

    public boolean isElementsDisplayed(By Locator, int locatorIndex) {
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(Locator)).get(locatorIndex).isDisplayed();
        } catch (NoSuchElementException | TimeoutException ex) {
            return false;
        }
    }

    public void clickByLocatorIndex(By locator, int locatorIndex) {
        try {
            List<WebElement> multiLocator = webDriver.findElements(locator);
            multiLocator.get(locatorIndex).click();
        } catch (Exception e) {
            Assert.fail("Failed to click on the element");
        }
    }

    public void click(By locator) {
        getElementIfClickable(locator).click();
    }

    public void clearText(By locator) {
        try {
            getElementIfVisible(locator).clear();
        } catch (Exception e) {
            Assert.fail("Failed to clear text from element");
        }
    }

    public void enterText(By locator, String text) {
        try {
            WebElement element = getElementIfClickable(locator);
            element.click();
            element.sendKeys(text);
        } catch (Exception e) {
            Assert.fail("Failed to enter text into element | Text to be entered: " + text);
        }
    }

    public void uploadFile(By locator, String filePath) {
        WebElement fileInput = webDriver.findElement(locator);
        File uploadFile = new File(filePath);
        fileInput.sendKeys(uploadFile.getAbsolutePath());
    }

    public void uploadFileWithJS(By locator, String filePath) {
        WebElement fileInput = webDriver.findElement(locator);

        // Make the input element visible
        String jsScript = "arguments[0].style.height='auto'; arguments[0].style.visibility='visible';";
        ((JavascriptExecutor) webDriver).executeScript(jsScript, fileInput);

        // Upload file
        fileInput.sendKeys(filePath);
    }

    public WebElement findElement(By locator) {
        WebElement element = null;
        try {
            element = webDriver.findElement(locator);
        } catch (Exception e) {

            Assert.fail("Failed to return element by locator: ");
        }
        return element;
    }

    public String getTextAttribute(By locator) {
        try {
            return getElementIfVisible(locator).getAttribute("value");
        } catch (Exception e) {

            Assert.fail("Failed to get text attribute 'value'");
            return "";
        }
    }

    public String getText(By locator) {
        return getElementIfVisible(locator).getText();
    }

    // Wait for an element with exact text
    public void waitForText(By locator, String text, int timeToWaitInSeconds) {
        new WebDriverWait(webDriver, Duration.ofSeconds(timeToWaitInSeconds)).until((ExpectedCondition<Boolean>) d ->
                getText(locator).equals(text)
        );
    }

    // Wait for an element containing specific text
    public void waitForTextContaining(By locator, String text, int timeToWaitInSeconds) {
        new WebDriverWait(webDriver, Duration.ofSeconds(timeToWaitInSeconds)).until((ExpectedCondition<Boolean>) d ->
                getText(locator).contains(text)
        );
    }

    public void getAndCompareText(By locator, String textToCompare) {

        try {
            String retrievedText = getElementIfVisible(locator).getText();
            if (!retrievedText.equals(textToCompare)) {
                Assert.fail("The actual text retrieved and the expected text do not match!");
            }
        } catch (NoSuchElementException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    public void pauseExecution(int seconds) {
        seconds = seconds * 1000;
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {

            log.error(e.getLocalizedMessage(), e);
        }
    }

    public void enterKeyCombination(By locator, String keyCombination) {
        try {
            WebElement element = getElementIfClickable(locator);
            switch (keyCombination.toUpperCase()) {
                case "TAB" -> element.sendKeys(Keys.TAB);
                case "ENTER" -> element.sendKeys(Keys.ENTER);
                case "F5" -> element.sendKeys(Keys.F5);
                case "ALT" -> element.sendKeys(Keys.ALT);
                case "ESCAPE" -> element.sendKeys(Keys.ESCAPE);
                case "DELETE" -> element.sendKeys(Keys.DELETE);
                case "PAGE UP" -> element.sendKeys(Keys.PAGE_UP);
                case "PAGE DOWN" -> element.sendKeys(Keys.PAGE_DOWN);
                case "LEFT_CONTROL" -> element.sendKeys(Keys.LEFT_CONTROL);
                case "SPACE" -> element.sendKeys(Keys.SPACE);
                case "BACKSPACE" -> element.sendKeys(Keys.BACK_SPACE);
                case "LEFT_SHIFT" -> element.sendKeys(Keys.LEFT_SHIFT);
            }
        } catch (Exception e) {

            Assert.fail("Failed to send keys.");
        }
    }

    public void switchToCurrentBrowserTab() {
        try {
            ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
            webDriver.switchTo().window(tabs.get(0));
        } catch (Exception e) {

            Assert.fail("Failed to switch to current browser tab");
        }
    }

    public void switchToFrameByWebElement(By locator) {
        try {
            WebElement element = getElementIfVisible(locator);
            webDriver.switchTo().frame(element);
        } catch (Exception e) {

            Assert.fail("Failed to switch to IFrame by locator");
        }
    }

    public void switchToBrowserTabByTitle(String title) {
        try {
            ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
            webDriver.switchTo().window(title);
        } catch (Exception e) {

            Assert.fail("Failed to switch tab by title");
        }
    }

    public void switchToBrowserTabByIndex(int index) {
        try {
            ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
            webDriver.switchTo().window(tabs.get(index));
        } catch (Exception e) {

            Assert.fail("Failed to switch to Tab by index");
        }
    }

    public void scrollHorizontallyByAGivenAmount(By locator, int xAxisAmount) {
        try {
            WebElement element = getElementIfVisible(locator);
            int deltaY = element.getRect().y;
            new Actions(webDriver)
                    .scrollByAmount(xAxisAmount, deltaY)
                    .perform();
        } catch (Exception e) {

            Assert.fail("Failed to scroll horizontally by amount specified");
        }
    }

    public void scrollVerticallyByAGivenAmount(By locator, int yAxisAmount) {
        try {
            WebElement element = getElementIfVisible(locator);
            int deltaX = element.getRect().x;
            new Actions(webDriver)
                    .scrollByAmount(deltaX, yAxisAmount)
                    .perform();
        } catch (Exception e) {

            Assert.fail("Failed to scroll vertically by amount specified");
        }
    }

    public void scrollToElement(By locator) {
        try {
            WebElement iframe = getElementIfVisible(locator);
            new Actions(webDriver)
                    .scrollToElement(iframe)
                    .perform();
        } catch (Exception e) {

            Assert.fail("Failed to scroll to element");
        }
    }

    public void clickElementAndHold(By locator) {
        try {
            WebElement clickable = getElementIfClickable(locator);
            new Actions(webDriver)
                    .clickAndHold(clickable)
                    .perform();
        } catch (Exception e) {

            Assert.fail("Failed to click and hold element");
        }
    }

    public void doubleClickElement(By locator) {
        try {
            WebElement clickable = getElementIfClickable(locator);
            new Actions(webDriver)
                    .doubleClick(clickable)
                    .perform();
        } catch (Exception e) {

            Assert.fail("Failed to double click on element");
        }
    }

    public void moveMouseCursorToElement(By locator) {
        try {
            WebElement hoverable = getElementIfClickable(locator);
            new Actions(webDriver)
                    .moveToElement(hoverable)
                    .perform();
        } catch (Exception e) {

            Assert.fail("Failed to move the mouse cursor to the center of the element.");
        }
    }

    public void dragAndDropElement(By dragLocator, By dropLocator) {
        try {
            WebElement draggable = getElementIfClickable(dragLocator);
            WebElement droppable = getElementIfClickable(dropLocator);
            new Actions(webDriver)
                    .dragAndDrop(draggable, droppable)
                    .perform();
        } catch (Exception e) {

            Assert.fail("Failed to drag the element to the drop location.");
        }
    }

    /**
     * This closes the browser instance and ends the test.
     */
    public void selectFromDropdownUsingVisibleText(By locator, String visibleTextToSelect) {
        WebElement dropdownRoot = null;
        Select select = null;

        try {
            dropdownRoot = getElementIfVisible(locator);
            select = new Select(dropdownRoot);
            select.selectByVisibleText(visibleTextToSelect);
        } catch (Exception e) {
            Assert.fail("Failed to select item from dropdown list using visible text");
        }
    }

    public void selectFromDropdownUsingIndex(By locator, int indexToSelect) {
        WebElement dropdownRoot = null;
        Select select = null;
        try {
            dropdownRoot = getElementIfVisible(locator);
            select = new Select(dropdownRoot);
            select.selectByIndex(indexToSelect);
        } catch (Exception e) {
            Assert.fail("Failed to select item from dropdown list using index");
        }
    }

    public void selectFromDropdownUsingValue(By locator, String valueToSelect) {
        WebElement dropdownRoot = null;
        Select select = null;
        try {
            dropdownRoot = getElementIfVisible(locator);
            select = new Select(dropdownRoot);
            select.selectByValue(valueToSelect);
        } catch (Exception e) {
            Assert.fail("Failed to select item from dropdown list using index");
        }
    }

    /**
     * This will scroll the webElement down.
     * Note: This only works for Chromium browsers
     */
    public void scrollElementIntoView(By locator, String trueForDownFalseForUp) {
        WebElement element = webDriver.findElement(locator);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(" + trueForDownFalseForUp + ");", element);
    }


    private void configure() {
        loggingSuppressors();
        actions = new Actions(webDriver);
        wait = new FluentWait<>(webDriver).withTimeout(defaultTimeout)
                .pollingEvery(defaultPollInterval)
                .ignoring(Throwable.class);
    }
}