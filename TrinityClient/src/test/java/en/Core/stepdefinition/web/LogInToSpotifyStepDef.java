package en.Core.stepdefinition.web;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import en.Core.context.ScenarioContext;
import en.Core.enums.URLS;
import utilities.FileHelper;
import utilities.GoogleAuthentication.GoogleAuthTOTP;
import java.io.FileInputStream;
import java.io.IOException;

import static configuration.Environment.LoginProp;


public class LogInToSpotifyStepDef {

    private final ScenarioContext scenarioContext;
    GoogleAuthTOTP googleAuthTOTP = new GoogleAuthTOTP();
    String authToken = "";


    public LogInToSpotifyStepDef(ScenarioContext scenarioContext)
    {
        this.scenarioContext = scenarioContext;
        try {
            LoginProp.load(new FileInputStream(FileHelper.getFileFromResource(ClassLoader.getSystemClassLoader(), "properties/login.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        authToken = googleAuthTOTP.getTotpForSecret(LoginProp.getProperty("TOTP"));
    }

    @Given("^I navigated to the the spotify login page")
    public void navigateToSpotifyLoginPage()
    {
        scenarioContext.driver.navigateToURL(URLS.Example_page.getURL());
    }

    @When("^I enter my username and password and submit the login form")
    public void LogIntoSpotifyWithValidUsernameAndPassword()
    {
        scenarioContext.loginpage.userSignIn(LoginProp.getProperty("SpotifyUsername"),LoginProp.getProperty("SpotifyPassword"));
    }
    @Then("^I should be logged in and on my spotify dashboard")
    public void IShouldBeLoggedInAndOnMySpotifyDashboard()
    {
    //Add validation code here.
    }

}
