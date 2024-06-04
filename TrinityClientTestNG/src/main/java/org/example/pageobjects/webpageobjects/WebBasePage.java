package org.example.pageobjects.webpageobjects;

import base.Driver;
import org.openqa.selenium.support.PageFactory;

public abstract class WebBasePage {
    protected final Driver driver;

    public WebBasePage(Driver driver) {
        this.driver = driver;
        PageFactory.initElements(driver.getDriver(),this);
    }
}
