package en.Core.pageobjects.webpageobjects;

import base.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utilities.DateCalculator;

public class CookiePage extends WebBasePage {
    @FindBy(xpath ="//button[@id='grantPermissionButton']")
    private WebElement CookieAcceptButton;


    public CookiePage(Driver driver) {
        super(driver);
    }


    public void acceptCookies()
    {

        }

}
