@api
Feature: Create a new user in the ReqRes API

  Scenario: Create a user with name and job
    Given the base URL is "https://reqres.in/api"
    When I send a POST request to "/users" with the payload
      | name | job       |
      | John | Developer |
    Then the response status code should be 201
    And the response should contain the name "John"
    And the response should have a non-null ID