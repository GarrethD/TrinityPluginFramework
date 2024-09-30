package base;

import io.restassured.RestAssured;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;



public class apiDriver {

    private static final Logger log = LoggerFactory.getLogger(apiDriver.class);
    private String baseUrl;
    private String bearerToken;

    public apiDriver(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setBearerToken(String token) {
        this.bearerToken = token;
    }

    public Response sendGetRequest(String endpoint) {
        try {
            return RestAssured
                    .given()
                    .baseUri(baseUrl)
                    .auth().oauth2(bearerToken)
                    .contentType(ContentType.JSON)
                    .get(endpoint)
                    .then()
                    .extract()
                    .response();
        } catch (Exception e) {
            log.error("GET request to " + endpoint + " failed.", e);
            Assert.fail("GET request failed. See logs for details.");
            return null;
        }
    }

    public Response sendPostRequest(String endpoint, Object body) {
        try {
            return RestAssured
                    .given()
                    .baseUri(baseUrl)
                    .auth().oauth2(bearerToken)
                    .contentType(ContentType.JSON)
                    .body(body)
                    .post(endpoint)
                    .then()
                    .extract()
                    .response();
        } catch (Exception e) {
            log.error("POST request to " + endpoint + " failed.", e);
            Assert.fail("POST request failed. See logs for details.");
            return null;
        }
    }

    // Example method for validating response status code
    public void validateStatusCode(Response response, int expectedStatusCode) {
        try {
            Assert.assertEquals(response.getStatusCode(), expectedStatusCode,
                    "Expected status code " + expectedStatusCode + " but got " + response.getStatusCode());
        } catch (AssertionError e) {
            log.error("Status code validation failed.", e);
            throw e;  // Re-throw the exception for further handling
        }
    }

    // Example method for extracting a field from JSON response
    public String extractFieldFromResponse(Response response, String fieldName) {
        try {
            return response.jsonPath().getString(fieldName);
        } catch (Exception e) {
            log.error("Failed to extract field " + fieldName + " from response.", e);
            Assert.fail("Field extraction failed. See logs for details.");
            return null;
        }
    }

    // Example method for extracting Bearer Token from response
    public String extractBearerToken(Response response) {
        return extractFieldFromResponse(response, "access_token");  // Assuming the token is in 'access_token' field
    }

    // Generic method for logging the response (for debugging or reporting)
    public void logResponse(Response response) {
        log.info("Response Status Code: " + response.getStatusCode());
        log.info("Response Body: " + response.getBody().asPrettyString());
    }

    // Method to validate response contains a specific field
    public void validateResponseContainsField(Response response, String fieldName) {
        try {
            Assert.assertNotNull(response.jsonPath().get(fieldName),
                    "Expected field " + fieldName + " was not found in the response.");
        } catch (AssertionError e) {
            log.error("Field validation failed: " + fieldName, e);

            throw e;  // Re-throw for further handling
        }
    }
}
