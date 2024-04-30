package en.Core.stepdefinition;

import base.MobileDriver;
import base.MobilePlatformName;
import com.google.gson.JsonObject;
import configuration.Environment;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import en.Core.context.MobileScenarioContext;
import en.Core.pageobjects.apppageobjects.AppBasePage;
import utilities.PageObjectHelper;

import java.util.Objects;

public class MobileBeforeAndAfterSteps extends CucumberScenario {
    private final MobileScenarioContext scenarioContext;

    public MobileBeforeAndAfterSteps(MobileScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Before(value = "@App", order = 0)
    public void init(Scenario scenario) {
        CucumberScenario.scenario = scenario;
        Environment.users("User1");
        JsonObject deviceOptions = Environment.getDeviceOptions(Environment.getDevice("device"));
        MobilePlatformName mobilePlatformName = MobilePlatformName.getMobilePlatformName(deviceOptions);
        scenarioContext.driver = new MobileDriver(mobilePlatformName, deviceOptions);
        PageObjectHelper.initAllPageObjects(scenarioContext, AppBasePage.class, new Class<?>[]{MobileDriver.class}, new Object[]{scenarioContext.driver});
    }

    @After("@App")
    public void tearDownApp() {
        if (!Objects.isNull(scenarioContext.driver)) {
            scenario.attach(scenarioContext.driver.getByteScreenshot(), "image/png", scenario.getName());
            scenarioContext.driver.closeDriver();
        }
    }
}
