package en.Core.stepdefinition.mobile;

import configuration.Environment;
import en.Core.stepdefinition.CucumberScenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import en.Core.context.MobileScenarioContext;
import org.testng.Assert;

@Slf4j
public class LoginToUserPortalStepDef extends CucumberScenario {
    private final MobileScenarioContext scenarioContext;

    public LoginToUserPortalStepDef(MobileScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Given("^I start the app for the first time$")
    public void appStartFirstTime() {
//        scenarioContext.driver.resetApp();
    }

    @When("^I walk through the first start of Spotify")
    public void acceptNewAppFeatures(String country) {
        scenarioContext.appCookiePage.agreeWithCookieConsent();
    }

    @And("^I login with a valid username and password$")
    public void loginWithValidUsernamePassword() {
        scenarioContext.appLoginPage.login(Environment.getUsername(), Environment.getPassword());
    }

    @Then("^I should see my spotify dashboard")
    public void LoginValidDetails() {
       //Add code to validate login to spotify
    }
}
