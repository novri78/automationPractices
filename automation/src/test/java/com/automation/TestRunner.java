package com.automation;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {
                "com/automation/steps",
                "com/automation/hooks"
        },
        //tags = "@smoke and not @flaky",
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "html:target/cucumber-reports/cucumber-reports.html",
                "json:target/cucumber-reports/cucumber.json",
                "rerun:target/rerun.txt",
                "junit:target/junit-report.xml",
        },
        monochrome=true
)
public class TestRunner {
}
