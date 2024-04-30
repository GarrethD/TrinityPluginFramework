@Web
Feature: Login with Okta

  As a user
  I want to log into spotify web
  So that I can listen to music or podcasts


  @TestngScenario
  Scenario: Log into spotify web using valid username and password
    Given I navigated to the the spotify login page
    When I enter my username and password and submit the login form
    Then I should be logged in and on my spotify dashboard
