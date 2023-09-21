package base;

import io.github.sukgu.Shadow;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class ShadowDriver {
    private Shadow shadow;
    WebDriverWait wait;

    public ShadowDriver(WebDriver driver) {
        this.shadow = new Shadow(driver);
    }
    private WebElement getShadowElementByCss(String locator) {
        try {
            shadow.setExplicitWait(30,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shadow.findElement(locator);
    }
    private WebElement getShadowElementByXpath(String locator) {
        try {
            shadow.setExplicitWait(30,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shadow.findElementByXPath(locator);
    }

    private WebElement getShadowElementFromParentByCss(WebElement parentElement,String selector)
    {
        return shadow.findElement(parentElement,selector);
    }

    private List<WebElement> getShadowElementsByCss(String locator) {
        return shadow.findElements(locator);
    }

    private List<WebElement> getShadowElementsByXpath(String locator) {
        return shadow.findElementsByXPath(locator);
    }


    public String getTextByCss(String locator) {
        String elementText = null;
        try {
            elementText = getShadowElementByCss(locator).getText();
        } catch (Exception e) {
            Assert.fail("Failed to return the text from the locator. Locator used: " + locator);
        }
        return elementText;
    }

    public String getTextByXpath(String locator) {
        String elementText = null;
        try {
            elementText = getShadowElementByXpath(locator).getText();
        } catch (Exception e) {
            Assert.fail("Failed to return the text from the locator.  Locator used: " + locator);
        }
        return elementText;
    }

    public void clickShadowElementByCss(String locator) {
        try {
            WebElement element = getShadowElementByCss(locator);
            element.click();
        } catch (Exception e) {
            Assert.fail("Failed to click on shadow element. Locator used: " + locator);
        }
    }
    public void clickShadowElementByXpath(String locator) {
        try {
            WebElement element = getShadowElementByXpath(locator);
            element.click();
        } catch (Exception e) {
            Assert.fail("Failed to click on shadow element. Locator used: " + locator);
        }
    }
    public void clickShadowElementFromParentByCss(WebElement webElement, String locator) {
        try {
            WebElement element = getShadowElementFromParentByCss(webElement,locator);
            element.click();
        } catch (Exception e) {
            Assert.fail("Failed to click on shadow element. Locator used: " + locator);
        }
    }

    public void enterTextIntoShadowElementByCss(String locator, String text) {
        try {
            WebElement element = getShadowElementByCss(locator);
            element.sendKeys(text);
        } catch (Exception e) {
            Assert.fail("Failed to enter text into shadow element. Locator used: " + locator);
        }
    }

    public void enterTextIntoShadowElementByXpath(String locator, String text) {
        try {
            WebElement element = getShadowElementByXpath(locator);
            element.sendKeys(text);
        } catch (Exception e) {
            Assert.fail("Failed to enter text into shadow element. Locator used: " + locator);
        }
    }

    public void isShadowElementVisibleByCss(String locator) {
        try {
            shadow.isVisible(getShadowElementByCss(locator));
        } catch (TimeoutException e) {
            Assert.fail("Failed to wait shadow element");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void isShadowElementVisibleByXpath(String locator) {
        try {
            shadow.isVisible(getShadowElementByXpath(locator));
        } catch (TimeoutException e) {
            Assert.fail("Failed to wait shadow element");
        }
    }

    public boolean isShadowElementEnabled(String locator) {
        WebElement element = null;
        try {
            element = getShadowElementByCss(locator);

        } catch (Exception e) {
            Assert.fail("Failed to check if shadow element is enabled. Locator used: " + locator);
        }
        return element.isEnabled();
    }

    public boolean isShadowElementEnabledByXpath(String locator) {
        WebElement element = null;
        try {
            element = getShadowElementByXpath(locator);

        } catch (Exception e) {
            Assert.fail("Failed to check if shadow element is enabled. Locator used: " + locator);
        }
        return element.isEnabled();
    }
}
