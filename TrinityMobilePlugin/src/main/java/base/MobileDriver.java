package base;

import com.browserstack.utils.UtilityMethods;
import com.google.gson.JsonObject;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utilities.AppiumServer;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Date;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static utilities.BarcodeScanner.decodeQRCode;
import static utilities.BarcodeScanner.generateImage;

@Slf4j
public class MobileDriver {
    private static final int defaultTimeout = 10;
    private final MobilePlatformName mobilePlatformName;
    private AndroidDriver androidDriver;
    private IOSDriver iosDriver;
    private WebDriverWait wait;

    /**
     * Constructs a new MobileDriver object.
     *
     * @param mobilePlatformName the platform name of the mobile device (Android or IOS)
     * @param deviceOptions      the device options in JSON format
     */
    public MobileDriver(MobilePlatformName mobilePlatformName, JsonObject deviceOptions) {
        this.mobilePlatformName = mobilePlatformName;
        switch (this.mobilePlatformName) {
            case ANDROID -> initializeAndroidDriver(deviceOptions);
            case IOS -> initializeIosDriver(deviceOptions);
        }
        this.wait = new WebDriverWait(getMobileDriver(), Duration.ofSeconds(defaultTimeout));
    }

    /**
     * Retrieves the current time of the device.
     *
     * @return the current time of the device as a java.util.Date object
     */
    public Date getDeviceTime() {
        String deviceTime = switch (this.mobilePlatformName) {
            case ANDROID -> androidDriver.getDeviceTime();
            case IOS -> iosDriver.getDeviceTime();
        };
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(deviceTime, ISO_OFFSET_DATE_TIME);
        return new Date(offsetDateTime.toInstant().toEpochMilli());
    }

    /**
     * Set a new timeout for looking up elements in de DOM.
     * The default in the constructor is 10 seconds.
     *
     * @param seconds Number of seconds to wait till element action can be executed
     */
    public void setNewWaitTimeOut(int seconds) {
        wait = new WebDriverWait(getMobileDriver(), Duration.ofSeconds(seconds));
    }

    /**
     * Getter for returning an instance of the MobileDriver
     *
     * @return An instance of the MobileDriver. Can be an Android or iOS driver instance.
     */
    public AppiumDriver getMobileDriver() {
        return switch (this.mobilePlatformName) {
            case ANDROID -> androidDriver;
            case IOS -> iosDriver;
        };
    }

    /**
     * Initializes an Android driver instance based on the provided device options.
     *
     * @param deviceOptions The device options for configuring the Android driver.
     */
    public void initializeAndroidDriver(JsonObject deviceOptions) {
        try {
            UiAutomator2Options options = new UiAutomator2Options();
            if (UtilityMethods.getBrowserstackEnabled()) {
                androidDriver = new AndroidDriver(new URL(UtilityMethods.getHubUrl()), options);
            } else {
                deviceOptions.asMap().forEach((key, value) -> options.setCapability(key, value.getAsString()));
                androidDriver = new AndroidDriver(AppiumServer.getService(), options);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Initialises an iOS driver based on the device given
     *
     * @param deviceOptions Device name that is known in the devices.json config on the client side
     */
    public void initializeIosDriver(JsonObject deviceOptions) {
        try {
            XCUITestOptions options = new XCUITestOptions();
            if (UtilityMethods.getBrowserstackEnabled()) {
                iosDriver = new IOSDriver(new URL(UtilityMethods.getHubUrl()), options);
            } else {
                deviceOptions.asMap().forEach((key, value) -> options.setCapability(key, value.getAsString()));
                iosDriver = new IOSDriver(AppiumServer.getService(), options);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Stops the MobileDriver
     */
    public void closeDriver() {
        getMobileDriver().quit();
    }

    /**
     * Generic method the get an element and wait till it's visible before returned
     *
     * @param webElement The webelement to be used for the action
     * @return The element requested in the locator
     */
    private WebElement getElementIfVisible(WebElement webElement) {
        return wait.until(ExpectedConditions.visibilityOf(webElement));
    }

    /**
     * Generic method the get an element and wait till it's clickable before returned
     *
     * @param webElement The webelement to be used for the action
     * @return The element requested in the locator
     */
    private WebElement getElementIfClickable(WebElement webElement) {
        return wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    /**
     * Clicks (touch) an element
     *
     * @param webElement The webelement to be used for the action
     */
    public void click(WebElement webElement) {
        try {
            getElementIfClickable(webElement).click();
        } catch (Exception e) {
            Assert.fail("Failed to click on the element");
        }
    }

    /**
     * Clears the text in the element
     *
     * @param webElement The webelement to be used for the action
     */
    public void clearText(WebElement webElement) {
        try {
            getElementIfVisible(webElement).clear();
        } catch (Exception e) {
            Assert.fail("Failed to clear text from element");
        }
    }

    /**
     * Enters text into an element
     *
     * @param webElement The webelement to be used for the action
     * @param text       The text that needs to be entered into the element
     */
    public void enterText(WebElement webElement, String text) {
        try {
            WebElement element = getElementIfClickable(webElement);
            element.click();
            element.sendKeys(text);
        } catch (Exception e) {
            Assert.fail("Failed to enter text into element | Text to be entered: " + text);
        }
    }

    /**
     * Get the value attribute of the given element
     *
     * @param webElement The webelement to be used for the action
     * @return The text of the value attribute of the element
     */
    public String getTextAttribute(WebElement webElement) {
        try {
            return getElementIfVisible(webElement).getAttribute("value");
        } catch (Exception e) {
            Assert.fail("Failed to get text attribute 'value'");
            return "";
        }
    }

    /**
     * Checks if an element is displayed or not
     *
     * @param webElement The webelement to be used for the action
     * @return True is element is displayed, false if not
     */
    public boolean isElementDisplayed(WebElement webElement) {
        try {
            return getElementIfVisible(webElement).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if an element is displayed or not withing a given timeout
     *
     * @param webElement       The webelement to be used for the action
     * @param timeoutInSeconds The nr of seconds to wait before element should be shown
     * @return True is element is displayed, false if not
     */
    public boolean isElementDisplayed(WebElement webElement, int timeoutInSeconds) {
        try {
            setNewWaitTimeOut(timeoutInSeconds);
            return getElementIfVisible(webElement).isDisplayed();
        } catch (Exception e) {
            return false;
        } finally {
            setNewWaitTimeOut(defaultTimeout);
        }
    }

    /**
     * Checks if an element is displayed or not (Used for dynamic locators)
     *
     * @param locator The locator of the element
     * @return True is element is displayed, false if not
     */
    public boolean isElementDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if an element is enabled or not
     *
     * @param webElement The webelement to be used for the action
     * @return True is element is enabled, false if not
     */
    public boolean isElementEnabled(WebElement webElement) {
        try {
            return getElementIfVisible(webElement).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if an element is not displayed
     *
     * @param webElement The webelement to be used for the action
     * @return True is element is not displayed, false if displayed
     */
    public boolean isElementNotDisplayed(WebElement webElement) {
        try {
            return wait.until(ExpectedConditions.invisibilityOf(webElement));
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Reads the QR code that is shown by the given element and returns the string value of it
     *
     * @param webElement The webelement to be used for the action
     * @return The content of the QR code
     */
    public String readQRCode(WebElement webElement) {
        try {
            WebElement qrCodeElement = getElementIfVisible(webElement);
            File screenshot = getMobileDriver().getScreenshotAs(OutputType.FILE);
            String content = decodeQRCode(generateImage(qrCodeElement, screenshot));
            log.debug("content = " + content);
            return content;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retrieves the text from an element
     *
     * @param webElement The webelement to be used for the action
     * @return The text found in the element that is given
     */
    public String getText(WebElement webElement) {
        return getElementIfVisible(webElement).getText();
    }

    /**
     * This function will hide the keyboard of the device
     */
    public void hideKeyboard() {
        switch (this.mobilePlatformName) {
            case ANDROID -> androidDriver.hideKeyboard();
            case IOS -> {
                try {
                    getElementIfClickable(iosDriver.findElement(AppiumBy.accessibilityId("Sluit"))).click();
                    doTap(new Point(1, 250), 500);
                } catch (Exception e) {
                    log.debug("Tried to hide iOS keyboard, but most likely it was not visible.");
                }
            }
        }
    }

    /**
     * If for some reason that execution of the test run should pause, use this function to pause it for
     * the given amount of seconds
     *
     * @param seconds Number of seconds to pause the test run
     */
    public void pauseExecution(int seconds) {
        log.debug("Try to avoid this pauseExecution function please. It will freezes everything!");
        seconds = seconds * 1000;
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * This function is used to swipe down the page
     */
    public void scrollPageDown() {
        hideKeyboard();
        Dimension dimension = getMobileDriver().manage().window().getSize();
        Point start = new Point(dimension.width / 2, (int) (dimension.height / 1.5));
        Point end = new Point(dimension.width / 2, dimension.height / 10);
        doSwipe(start, end, 1000);
    }

    /**
     * This function is used to swipe down the page
     */
    public void scrollPageUp() {
        hideKeyboard();
        Dimension dimension = getMobileDriver().manage().window().getSize();
        Point start = new Point(dimension.width / 2, dimension.height / 10);
        Point end = new Point(dimension.width / 2, (int) (dimension.height / 1.5));
        doSwipe(start, end, 1000);
    }

    /**
     * Function that swipes down to refresh
     */
    public void swipeDownToRefresh() {
        Dimension dimension = getMobileDriver().manage().window().getSize();
        Point start = new Point(dimension.width / 2, (int) (dimension.height * 0.3));
        Point end = new Point(dimension.width / 2, (int) (dimension.height * 0.8));
        doSwipe(start, end, 500);
    }

    /**
     * Function to swipe in general
     *
     * @param start        Start point for finger
     * @param stop         End point for finger
     * @param milliseconds Time in ms take it will take before reaching end point
     */
    public void doSwipe(Point start, Point stop, int milliseconds) {
        W3CActions.doSwipe(getMobileDriver(), start, stop, milliseconds);
    }

    /**
     * Function to tap in general
     *
     * @param point        Start point for finger
     * @param milliseconds Time in ms take it will take before releasing the spot
     */
    public void doTap(Point point, int milliseconds) {
        W3CActions.doTap(getMobileDriver(), point, milliseconds);
    }

    /**
     * Will create a screenshot and return it as a byte array
     *
     * @return the screenshot taken
     */
    public byte[] getByteScreenshot() {
        return ((TakesScreenshot) getMobileDriver()).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Finds and returns the first element that matches the given XPath expression.
     *
     * @param xpath the XPath expression used to find the element
     * @return the first element that matches the given XPath expression, or null if no element is found
     */
    public WebElement findElementByXPath(String xpath) {
        try {
            return getMobileDriver().findElement(By.xpath(xpath));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Will keep refreshing every 2 seconds till an element is shown
     *
     * @param webElement The element to be seen after refresh
     */
    public void refreshTillElementIsDisplayed(WebElement webElement) {
        for (int i = 0; i < 30; i++) {
            if (isElementDisplayed(webElement)) {
                break;
            }
            pauseExecution(2);
            swipeDownToRefresh();
        }
        log.error("refreshTillElementIsDisplayed: Element not found after 60 seconds. Aborting.");
    }

    /**
     * Lookup a web element that is not visible on the screen.
     * This is hard, because the find elements won't work if the element is not visible by the human eye.
     *
     * @param webElement WebElement to look for
     * @return The web element that is found or null if not found
     */
    public WebElement findElementByScrollingDown(WebElement webElement) {
        return findElementByScrollingDown(webElement, 5);
    }

    /**
     * Lookup a web element that is not visible on the screen.
     * This is hard, because the find elements won't work if the element is not visible by the human eye.
     *
     * @param webElement     WebElement to look for
     * @param maximumScrolls max nr of times to scroll
     * @return The web element that is found or null if not found
     */
    public WebElement findElementByScrollingDown(WebElement webElement, int maximumScrolls) {
        hideKeyboard();
        for (int i = 0; i < maximumScrolls; i++) {
            if (isElementDisplayed(webElement, 1)) {
                return webElement;
            }
            scrollPageDown();
        }
        return null;
    }

    /**
     * Lookup a web element that is not visible on the screen.
     * This is hard, because the find elements won't work if the element is not visible by the human eye.
     *
     * @param webElement WebElement to look for
     * @return The web element that is found or null if not found
     */
    public WebElement findElementByScrollingUp(WebElement webElement) {
        return findElementByScrollingUp(webElement, 5);
    }

    /**
     * Lookup a web element that is not visible on the screen.
     * This is hard, because the find elements won't work if the element is not visible by the human eye.
     *
     * @param webElement     WebElement to look for
     * @param maximumScrolls max nr of times to scroll
     * @return The web element that is found or null if not found
     */
    public WebElement findElementByScrollingUp(WebElement webElement, int maximumScrolls) {
        hideKeyboard();
        for (int i = 0; i < maximumScrolls; i++) {
            if (isElementDisplayed(webElement, 1)) {
                return webElement;
            }
            scrollPageUp();
        }
        return null;
    }

    /**
     * Fill a form field with the given value.
     *
     * @param webElement The web element representing the form field
     * @param value      The value to fill the form field with
     */
    public void fillFormField(WebElement webElement, String value) {
        if (value != null) {
            findElementByScrollingDown(webElement);
            enterText(webElement, value);
        }
    }

    /**
     * Clicks on an element if it is displayed on the webpage.
     *
     * @param elementDisplayed The web element to check if it is displayed
     * @param elementToClick   The web element to click
     */
    public void clickIfElementIsDisplayed(WebElement elementDisplayed, WebElement elementToClick) {
        isElementDisplayed(elementDisplayed);
        click(elementToClick);
    }
}
