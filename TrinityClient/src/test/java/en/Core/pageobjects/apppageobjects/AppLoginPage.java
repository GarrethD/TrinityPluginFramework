package en.Core.pageobjects.apppageobjects;

import base.MobileDriver;
import base.MobilePlatformName;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class AppLoginPage extends AppBasePage {
    @AndroidFindBy(xpath = "com.android.chrome:id/title")
    private WebElement lblWelcomeToChrome;

    @AndroidFindBy(xpath = "com.android.chrome:id/terms_accept")
    private WebElement btnAcceptAndContinue;

    @AndroidFindBy(xpath = "com.android.chrome:id/negative_button")
    private WebElement btnNoThanks;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'Log Spotify') or contains(@text,'Log Spotify') or contains(@text,'Login to Spotify') or contains(@text,'Login to Spotify')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='Log at Spotify' or @name='Log Spotify' or @name='Login to Spotify' or @name='Login Spotify']")
    private WebElement lblLogin;

    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'EmailAddress')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField")
    private WebElement inputUsername;

    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'Password')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField")
    private WebElement inputPassword;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='Login' or @text='Login']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Login' or @name='Login']")
    private WebElement btnInloggen;

    public AppLoginPage(MobileDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        // Sometimes Chrome on Android comes with a first run screen. This can bypass that.
        if (platformName.equals(MobilePlatformName.ANDROID) && (driver.isElementDisplayed(lblWelcomeToChrome))) {
            driver.click(btnAcceptAndContinue);
            driver.click(btnNoThanks);
            driver.click(btnNoThanks);
            driver.pauseExecution(2);
        }
        // Now we can log in
        if (driver.isElementDisplayed(lblLogin)) {
            driver.pauseExecution(2);
            driver.enterText(inputUsername, username);
            driver.hideKeyboard();
            driver.enterText(inputPassword, password);
            driver.click(btnInloggen);
            driver.pauseExecution(5);
        }
    }
}
