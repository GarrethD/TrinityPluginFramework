package en.Core.pageobjects.apppageobjects;

import base.MobileDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class AppCookiePage extends AppBasePage{
    @AndroidFindBy(xpath = "//*[@text='Gebruikersinformatie' or @label='Gebruikersinformatie']")
    @iOSXCUITFindBy(xpath = "//*[@text='Gebruikersinformatie' or @label='Gebruikersinformatie']")
    private WebElement lblGebruikersinformatie;

    @AndroidFindBy(xpath = "//*[@text='Akkoord' or @label='Akkoord']")
    @iOSXCUITFindBy(xpath = "//*[@text='Akkoord' or @label='Akkoord']")
    private WebElement btnAkkoord;

    public AppCookiePage(MobileDriver driver) {
        super(driver);
    }

    public void agreeWithCookieConsent() {
        clickIfElementIsDisplayed(lblGebruikersinformatie, btnAkkoord);
    }
}
