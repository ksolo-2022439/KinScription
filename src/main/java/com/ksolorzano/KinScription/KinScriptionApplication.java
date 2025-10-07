package com.ksolorzano.KinScription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class KinScriptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(KinScriptionApplication.class, args);
    }

}
