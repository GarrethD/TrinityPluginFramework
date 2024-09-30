package en.Core.stepdefinition.api;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;

import java.util.Map;

public class CreateNewUserStepDef {
    private Response response;
    private String baseUrl;

    @Given("the base URL is {string}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    @When("I send a PUT request to {string} with the payload")
    public void sendPutRequestWithPayload(String endpoint, Map<String, String> dataTable) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", dataTable.get("name"));
        requestBody.put("job", dataTable.get("job"));

        response = RestAssured
                .given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .put(endpoint)
                .then()
                .extract().response();
    }

    @When("I send a POST request to {string} with the payload")
    public void sendPostRequestWithPayload(String endpoint, Map<String, String> dataTable) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", dataTable.get("name"));
        requestBody.put("job", dataTable.get("job"));

        response = RestAssured
                .given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .post(endpoint)
                .then()
                .extract().response();
    }
    @When("I send a DELETE request to {string}")
    public void sendDeleteRequest(String endpoint) {
        response = RestAssured
                .given()
                .baseUri(baseUrl)
                .delete(endpoint)
                .then()
                .extract().response();
    }

    @Then("the response status code should be {int}")
    public void validateStatusCode(int statusCode) {
        Assert.assertEquals(response.statusCode(), statusCode, "Status code mismatch");
    }

    @Then("the response should contain the name {string}")
    public void validateName(String name) {
        String actualName = response.jsonPath().getString("name");
        Assert.assertEquals(actualName, name, "Name mismatch");
    }

    @Then("the response should have a non-null ID")
    public void validateId() {
        Assert.assertNotNull(response.jsonPath().getString("id"), "ID should not be null");
    }

    @And("the response should contain the updated name {string}")
    public void theResponseShouldContainTheUpdatedName(String name)
    {
        String actualName = response.jsonPath().getString("name");
        Assert.assertEquals(actualName, name, "Name mismatch");
    }

}
