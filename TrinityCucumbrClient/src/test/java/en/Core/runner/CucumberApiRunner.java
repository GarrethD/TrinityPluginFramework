package en.Core.runner;

import groovy.util.logging.Slf4j;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@Slf4j
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "en.Core.stepdefinition",
        plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        tags = "@api",
        publish = false)

public class CucumberApiRunner extends AbstractTestNGCucumberTests {
}
