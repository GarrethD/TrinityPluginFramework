package en.Core.stepdefinition.web;

import en.Core.stepdefinition.CucumberScenario;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import en.Core.context.ScenarioContext;
import utilities.FileHelper;

import utilities.excelutil.ExcelDataReader;
import utilities.excelutil.ExcelFile;

import java.io.FileInputStream;
import java.io.IOException;

import static configuration.Environment.LoginProp;

import static utilities.JsonObjectMapper.ReadJsonFile;

public class ReadingDataFromDataFiles extends CucumberScenario
{
    private final ScenarioContext scenarioContext;

    public ReadingDataFromDataFiles(ScenarioContext scenarioContext)
    {
        this.scenarioContext = scenarioContext;
    }

    //Excel file reading example
    @When("^The user enters Data into the excel sheet")
    public void UserEntersDataIntoExcelSheet()
    {
        System.out.println("Manually enter data into the excel sheet");
    }
    @Then("^The user should be able to read the excel data sheet and it's values in tests")
    public void UserCanReadDataFromExcelFile()
    {
        ExcelDataReader reader = new ExcelDataReader(new ExcelFile("Kdd", "TheSheet1"), "Ball1");
        System.out.println(reader.getParameterValue("Username"));
        System.out.println(reader.getParameterValue("Password"));
        System.out.println(reader.getParameterValue("EmailAddress"));
        System.out.println(reader.getParameterValue("Name"));

    }

    //Json file reading example
    @When("^The user enters Data into the Json file")
    public void UserEntersDataIntoJsonFile()
    {
        System.out.println("Manually enter data into the json file");
    }

    @Then("^The user should be able to read the json file and it's values in tests")
    public void UserCanReadDataFromJsonFile()
    {
        JsonPath userConfig = ReadJsonFile(FileHelper.getFileFromResource(ClassLoader.getSystemClassLoader(), "jsonfiles/usercreds.json").getAbsolutePath());

        System.out.println(userConfig.getString("Garreth_D.Name"));
        System.out.println(userConfig.getString("Garreth_D.Surname"));
        System.out.println(userConfig.getString("Garreth_D.DOB"));
        System.out.println(userConfig.getString("Garreth_D.PlaceOfBirth"));
        System.out.println(userConfig.getString("Garreth_D.Occupation"));
    }

    //Prop file reading example
    @When("^The user enters Data into the properites file")
    public void UserEntersDataIntoPropertiesFile()
    {
        System.out.println("Manually enter data into the json file");
    }

    @Then("^The user should be able to read the properties file and it's values in tests")
    public void UserCanReadDataFromPropFile()
    {
        String username = null;
        String password = null;
        try {
            LoginProp.load(new FileInputStream(FileHelper.getFileFromResource(ClassLoader.getSystemClassLoader(), "properties/login.properties")));

            username  = LoginProp.getProperty("Username");
            password  = LoginProp.getProperty("Password");

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(username);
        System.out.println(password);
    }
}
