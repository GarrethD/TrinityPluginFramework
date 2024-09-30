@api
Feature: Update user details in the ReqRes API

  Scenario: Update user name and job
    Given the base URL is "https://reqres.in/api"
    When I send a PUT request to "/users/2" with the payload
      | name | job        |
      | Jane | Engineer   |
    Then the response status code should be 200
    And the response should contain the updated name "Jane"
