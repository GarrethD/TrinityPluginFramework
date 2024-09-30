@api
Feature: Delete a user from the ReqRes API

  Scenario: Delete a user by ID
    Given the base URL is "https://reqres.in/api"
    When I send a DELETE request to "/users/2"
    Then the response status code should be 204
