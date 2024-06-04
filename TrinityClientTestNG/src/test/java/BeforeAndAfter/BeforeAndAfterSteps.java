package BeforeAndAfter;

import base.SeleniumDriver;
import org.example.configuration.Environment;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

//import static configuration.Environment.extent;
//import static configuration.Environment.setupKlov;

public class BeforeAndAfterSteps {

    SeleniumDriver seDriver;
    String browser;
    String url;
    String testUser;

    @BeforeClass
    public void Setup() {
//        setupKlov();
//        extent.createTest(scenario.getName());
        browser = Environment.getBrowser("browser");
        url =  Environment.getEnvironment("url");
        seDriver = new SeleniumDriver(browser);
        Environment.users("User1");
    }

    @AfterClass
    public void Teardown() {
//            extent.flush();
        seDriver.closeBrowserInstance();
    }
}
