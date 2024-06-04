package org.example.pageobjects.webpageobjects;

import base.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends WebBasePage {

    @FindBy(xpath ="//img[@id='logo']")
    private WebElement LoginLogo;


    public LoginPage(Driver driver) {
        super(driver);
    }

    public void userSignIn(String username, String password) {

        }



}
