@api
Feature: Retrieve user details from ReqRes API

  Scenario: Get user details by user ID
    Given the base URL is "https://reqres.in/api"
    When I send a GET request to "/users/2"
    Then the response status code should be 200
    And the response should contain the first name "Janet"
    And the response should have a non-null email