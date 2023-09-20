@Web
Feature: Adjust Bezorgvoorkeuren through web

  As a user
  I want to store data in a files
  and then use the data in my tests for keyword driven development (KDD)

  @TestngScenario
  Scenario: Read testing Data from Excel sheet
    When The user enters Data into the excel sheet
    Then The user should be able to read the excel data sheet and it's values in tests

  @TestngScenario
  Scenario: Read testing Data from json file
    When The user enters Data into the Json file
    Then The user should be able to read the json file and it's values in tests

  @TestngScenario
  Scenario: Read testing Data from properties file
    When The user enters Data into the properites file
    Then The user should be able to read the properties file and it's values in tests
