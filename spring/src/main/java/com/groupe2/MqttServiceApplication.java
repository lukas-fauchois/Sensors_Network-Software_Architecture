package com.groupe2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.AsyncConfigurer;

public class MqttServiceApplication extends SpringBootServletInitializer implements AsyncConfigurer, CommandLineRunner {

    @Autowired
    Mqtt mqtt;

    @Override
    public void run(String... args) throws Exception {
    }

}
