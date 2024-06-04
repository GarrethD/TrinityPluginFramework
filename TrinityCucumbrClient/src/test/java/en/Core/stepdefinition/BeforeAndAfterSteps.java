package en.Core.stepdefinition;


import base.SeleniumDriver;
import configuration.Environment;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import en.Core.context.ScenarioContext;
import en.Core.pageobjects.webpageobjects.WebBasePage;
import utilities.PageObjectHelper;
import java.util.Objects;

//import static configuration.Environment.extent;
//import static configuration.Environment.setupKlov;

public class BeforeAndAfterSteps extends CucumberScenario {
    private final ScenarioContext scenarioContext;

    public BeforeAndAfterSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Before(value = "@Web", order = 0)
    public void init(Scenario scenario) {
//        setupKlov();
//        extent.createTest(scenario.getName());

        CucumberScenario.scenario = scenario;
        String browserName = Environment.getBrowser("browser");
        scenarioContext.url = Environment.getEnvironment("url");
        scenarioContext.driver = new SeleniumDriver(browserName);
        Environment.users("User1");
        PageObjectHelper.initAllPageObjects(scenarioContext, WebBasePage.class, new Class<?>[]{SeleniumDriver.class}, new Object[]{scenarioContext.driver});
    }

    @After("@Web")
    public void tearDownWeb() {
        if (!Objects.isNull(scenarioContext.driver)) {
            scenario.attach(scenarioContext.driver.getByteScreenshot(), "image/png", scenario.getName());
//            extent.flush();
            scenarioContext.driver.webDriver.close();
        }
    }
}
