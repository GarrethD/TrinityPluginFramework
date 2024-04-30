package en.Core.runner;

import groovy.util.logging.Slf4j;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@Slf4j
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "en.core.stepdefinition",
        plugin = {"pretty", "json:target/cucumber-reports/cucumber.json", "html:target/cucumber-reports/cucumberreport.html",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        tags = "@Web",
        publish = false)

public class CucumberRunnerTests extends AbstractTestNGCucumberTests{

}
