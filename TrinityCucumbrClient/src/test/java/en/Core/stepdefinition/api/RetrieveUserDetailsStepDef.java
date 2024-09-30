package en.Core.stepdefinition.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;

import java.util.Map;

public class RetrieveUserDetailsStepDef {
    private Response response;
    private String baseUrl;

    @Given("the base URL is {string}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @When("I send a GET request to {string}")
    public void sendGetRequest(String endpoint) {
        response = RestAssured
                .given()
                .baseUri(baseUrl)
                .get(endpoint)
                .then()
                .extract().response();
    }

    @Then("the response status code should be {int}")
    public void validateStatusCode(int statusCode) {
        Assert.assertEquals(response.statusCode(), statusCode, "Status code mismatch");
    }

    @Then("the response should contain the first name {string}")
    public void validateFirstName(String firstName) {
        String actualFirstName = response.jsonPath().getString("data.first_name");
        Assert.assertEquals(actualFirstName, firstName, "First name mismatch");
    }

    @Then("the response should have a non-null email")
    public void validateEmail() {
        Assert.assertNotNull(response.jsonPath().getString("data.email"), "Email is null");
    }
}
