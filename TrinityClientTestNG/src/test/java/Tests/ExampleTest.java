package Tests;

import BeforeAndAfter.MobileBeforeAndAfterSteps;
import base.MobileDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExampleTest extends MobileBeforeAndAfterSteps {
    MobileDriver driver = getMobileDriver();

    @Test
    public void testAppInitialization() {

        // Perform test actions using the driver
        // Example: Verify the app has launched successfully
        Assert.assertNotNull(driver, "MobileDriver should be initialized.");
    }
}

