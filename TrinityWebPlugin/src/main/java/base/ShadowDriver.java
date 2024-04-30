package base;

import io.github.sukgu.Shadow;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.time.Duration;
import java.util.List;

@Slf4j

public class ShadowDriver {
    private Shadow shadow;

    public ShadowDriver(WebDriver driver) {
        this.shadow = new Shadow(driver);
        try {
            shadow.setExplicitWait(30, 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Finds a single element in the shadow DOM by automatically detecting the locator type.
     * An explicit wait is set before attempting to find the element.
     *
     * @param locator A string representing either a CSS selector or an XPath.
     * @return A WebElement found using the specified locator, or null if an exception occurs.
     */
    public WebElement getShadowElement(String locator) {
        try {

            // Detect if the locator is an XPath or CSS and find the element accordingly
            if (locator.startsWith("/") || locator.startsWith("./")) {
                return shadow.findElementByXPath(locator);
            } else { // Assume it's a CSS selector
                return shadow.findElement(locator);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null or handle the exception as needed
        }
    }

    /**
     * Finds elements in the shadow DOM by automatically detecting the locator type.
     *
     * @param locator A string representing either a CSS selector or an XPath.
     * @return A list of WebElements found using the specified locator.
     */
    public List<WebElement> getShadowElements(String locator) {
        // Check if the locator starts with "/" or "./" to assume it's an XPath
        if (locator.startsWith("/") || locator.startsWith("./")) {
            return shadow.findElementsByXPath(locator);
        } else { // Assume it's a CSS selector
            return shadow.findElements(locator);
        }
    }


    public String getText(String locator) {
        String elementText = null;
        try {
            elementText = getShadowElement(locator).getText();
        } catch (Exception e) {
            Assert.fail("Failed to return the text from the locator. Locator used: " + locator);
        }
        return elementText;
    }


    public void clickShadowElement(String locator) {
        try {
            WebElement element = getShadowElement(locator);
            element.click();
        } catch (Exception e) {
            Assert.fail("Failed to click on shadow element. Locator used: " + locator);
        }
    }
    public String getAttributeFromShadowElement(String locator, String attributeName) {
        String attribute = "";
        try {
            attribute = getShadowElement(locator).getAttribute(attributeName);
        } catch (Exception e) {
            Assert.fail("Failed to get attribute from shadow element. Locator used: " + locator);
        }
        return attribute;
    }
    public void selectShadowElement(String locator, String checkboxLabelToSelect) {

        try {
            shadow.selectCheckbox(getShadowElement(locator), checkboxLabelToSelect);
        } catch (Exception e) {
            Assert.fail("Failed to get select the shadow element checkbox. Locator used: " + locator);
        }
    }
    public void scrollToShadowElement(String locator) {

        try {
            shadow.scrollTo(getShadowElement(locator));
        } catch (Exception e) {
            Assert.fail("Failed to scroll to shadow element. Locator used: " + locator);
        }
    }
    public boolean isSelectionFromShadowElement(String locator) {
        boolean isSelected = false;
        try {
            WebElement element = getShadowElement(locator);
            isSelected = element.isSelected();
        } catch (Exception e) {
            System.out.println("Failed to get the selection state from shadow element: " + e.getMessage());
        }
        return isSelected;
    }


    public void enterTextIntoShadowElement(String locator, String text) {
        try {
            WebElement element = getShadowElement(locator);
            element.click();
            element.sendKeys(text);
        } catch (Exception e) {
            log.error("Failed to enter text into  shadow element: {}", e.getMessage());
            Assert.fail("Failed to enter text into shadow element. Locator used: " + locator);
        }
    }

    public void isShadowElementVisible(String locator) {
        try {
            shadow.isVisible(getShadowElement(locator));
        } catch (TimeoutException e) {
            log.error("Failed to wait shadow element: {}", e.getMessage());
            Assert.fail("Failed to wait shadow element");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isShadowElementVisibleNoAssert(String locator) {
        boolean isVisible = false;
        try {
            isVisible =shadow.isVisible(getShadowElement(locator));
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isVisible;

    }
    public boolean isShadowElementEnabled(String locator) {
        boolean isVisible = false;
        try {
            isVisible = getShadowElement(locator).isEnabled();
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isVisible;

    }
    public void enterKeyCombination(String locator, String keyCombination) {
        try {
            WebElement element = getShadowElement(locator);
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
            log.error("Failed to enter key combination: {}", e.getMessage());
            Assert.fail("Failed to enter key combination");
            throw e;
        }
    }
    public void clearText(String locator) {
        try {
            getShadowElement(locator).clear();
        } catch (Exception e) {
            log.error("Failed to clear text of web element: {}", e.getMessage());
            Assert.fail("Failed to clear text from element.");
            throw e;
        }
    }

    public void uploadFile(String locator, String filePath) {
        WebElement fileInput = getShadowElement(locator);
        File uploadFile = new File(filePath);
        fileInput.sendKeys(uploadFile.getAbsolutePath());
    }

    public void uploadFileWithJS(String locator, String filePath) {
        WebElement fileInput = shadow.findElement(locator);

        // Make the input element visible
        String jsScript = "arguments[0].style.height='auto'; arguments[0].style.visibility='visible';";
        ((JavascriptExecutor) shadow).executeScript(jsScript, fileInput);

        // Upload file
        fileInput.sendKeys(filePath);
    }
}
