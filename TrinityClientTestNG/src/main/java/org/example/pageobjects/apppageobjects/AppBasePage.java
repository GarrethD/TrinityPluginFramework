package org.example.pageobjects.apppageobjects;

import base.MobileDriver;
import base.MobilePlatformName;
import configuration.Environment;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utilities.PlatformDetection;

import java.time.Duration;

public abstract class AppBasePage {
    protected final MobileDriver driver;
    protected final MobilePlatformName platformName;

    public AppBasePage(MobileDriver driver) {
        this.driver = driver;
        String deviceName = Environment.getDevice("device");
        platformName = MobilePlatformName.valueOf(PlatformDetection.mobilePlatform(deviceName).toUpperCase());
        PageFactory.initElements(new AppiumFieldDecorator(driver.getMobileDriver(), Duration.ofSeconds(5)), this);
    }

    public void clickIfElementIsDisplayed(WebElement elementDisplayed, WebElement elementToClick) {
        driver.isElementDisplayed(elementDisplayed);
        driver.click(elementToClick);
    }
}
