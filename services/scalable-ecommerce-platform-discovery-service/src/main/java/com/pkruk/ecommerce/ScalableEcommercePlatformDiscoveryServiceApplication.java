package com.pkruk.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ScalableEcommercePlatformDiscoveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScalableEcommercePlatformDiscoveryServiceApplication.class, args);
    }

}
