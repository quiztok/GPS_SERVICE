package com.quiztok.gps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

//@PropertySource("classpath:application-${profile:prd-sg-idc}.properties")
@PropertySource("classpath:application-${profile:dev}.properties")
@SpringBootApplication
public class GpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GpsApplication.class, args);
    }

}
