package com.santander.bp.api;

import com.santander.bp.ArsenalApplication;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@CucumberContextConfiguration
@CucumberOptions(
    features = "src/test/resources/features",
    plugin = {"pretty", "html:target/reports/api.html"},
    glue = {"com.santander.bp.api.steps", "com.santander.bp.config"})
@SpringBootTest(
    classes = ArsenalApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
@AutoConfigureObservability
public class RunCucumberTest {}
