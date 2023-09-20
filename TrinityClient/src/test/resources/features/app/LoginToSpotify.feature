@App
Feature: Login to user account

  As a user
  I want navigate to the login page
  So that I can log in and have access to my spotify account

  Scenario: Login to the user spotify account and validate the correct account was used
    Given I start the app for the first time
    When I walk through the first start of Spotify
    And I login with a valid username and password
    Then I should see my spotify dashboard
