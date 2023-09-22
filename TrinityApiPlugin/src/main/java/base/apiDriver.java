package base;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.*;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/*
The methods in the ApiCore class have a return type of ApiCore because they use a technique called "method chaining" or "fluent interface."
 This design pattern allows you to chain method calls, which makes the code more readable and easier to understand.
  By returning ApiCore (the current class), you can chain multiple method calls together in a single line, like this:

  apiCore.setHeader("Content-Type", "application/json")
       .setBody("{\"name\":\"John\", \"job\":\"developer\"}")
       .setAuthToken(authenticationScheme);

Each method returns the current instance of the class (this),
enabling you to call another method on the same instance without having to write multiple lines of code.
 This design pattern is commonly used in API design to make the code more concise and expressive.
 */

public class apiDriver {

    private RequestSpecification request;
    private RequestSpecBuilder requestSpecBuilder;

    public apiDriver() {
        requestSpecBuilder = new RequestSpecBuilder();
    }

    public void setBaseUrl(String baseUrl) {
        RestAssured.baseURI = baseUrl;
        requestSpecBuilder.setBaseUri(baseUrl);
    }

    public apiDriver setHeader(String headerName, String headerValue) {
        requestSpecBuilder.addHeader(headerName, headerValue);
        return this;
    }

    public apiDriver setHeaders(Map<String, String> headers) {
        requestSpecBuilder.addHeaders(headers);
        return this;
    }

    public apiDriver setContentType(ContentType contentType) {
        requestSpecBuilder.setContentType(contentType);
        return this;
    }

    public apiDriver setBody(String body) {
        requestSpecBuilder.setBody(body);
        return this;
    }

    public apiDriver setBodyFromFile(String filePath) {
        try {
        JSONParser jsonParser = new JSONParser();
        FileReader fileReader = new FileReader(filePath);
        JSONObject jsonObject = null;
            jsonObject = (JSONObject) jsonParser.parse(fileReader);
            requestSpecBuilder.setBody(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return this;
    }

    public apiDriver setAuthToken(AuthenticationScheme authentication) {
        requestSpecBuilder.setAuth(authentication);
        return this;
    }

    public apiDriver setQueryParams(Map<String, String> queryParams) {
        requestSpecBuilder.addQueryParams(queryParams);
        return this;
    }

    public apiDriver setPathParams(Map<String, String> pathParams) {
        requestSpecBuilder.addPathParams(pathParams);
        return this;
    }

    public apiDriver setSSLCert(String pathToCert, String keystorePassword) {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.config = RestAssured.config().sslConfig(
                RestAssured.config().getSSLConfig().with().keyStore(pathToCert, keystorePassword));
        return this;
    }

    public apiDriver setSSLCert(String pathToCert) {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.config = RestAssured.config().sslConfig(RestAssured.config().getSSLConfig().with().keyStore(pathToCert, "changeit"));
        return this;
    }

    public Response get(String path) {
        request = RestAssured.given().spec(requestSpecBuilder.build());
        return request.get(path);
    }

    public Response post(String path) {
        request = RestAssured.given().spec(requestSpecBuilder.build());
        return request.post(path);
    }

    public Response put(String path) {
        request = RestAssured.given().spec(requestSpecBuilder.build());
        return request.put(path);
    }

    public Response delete(String path) {
    request = RestAssured.given().spec(requestSpecBuilder.build());
        return request.delete(path);
}

    public Response patch(String path) {
        request = RestAssured.given().spec(requestSpecBuilder.build());
        return request.patch(path);
    }

    public Response options(String path) {
        request = RestAssured.given().spec(requestSpecBuilder.build());
        return request.options(path);
    }

    public Response head(String path) {
        request = RestAssured.given().spec(requestSpecBuilder.build());
        return request.head(path);
    }
}
