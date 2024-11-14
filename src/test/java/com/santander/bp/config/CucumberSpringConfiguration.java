package com.santander.bp.config;

import com.santander.bp.ArsenalApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = ArsenalApplication.class)
public class CucumberSpringConfiguration {}
