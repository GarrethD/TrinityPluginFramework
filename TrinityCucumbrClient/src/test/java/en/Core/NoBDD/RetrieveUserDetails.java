package en.Core.NoBDD;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RetrieveUserDetails {
    @Test
    public void getUserDetails() {
        // Set the base URL
        String baseUrl = "https://reqres.in/api";

        // Send GET request
        Response response = RestAssured
                .given()
                .baseUri(baseUrl)
                .get("/users/2")
                .then()
                .extract().response();

        // Print response (optional)
        System.out.println(response.prettyPrint());

        // Validate status code
        Assert.assertEquals(response.statusCode(), 200, "Status code mismatch");

        // Validate first name
        String firstName = response.jsonPath().getString("data.first_name");
        Assert.assertEquals(firstName, "Janet", "First name mismatch");

        // Validate email field is not null
        Assert.assertNotNull(response.jsonPath().getString("data.email"), "Email is null");
    }
}
