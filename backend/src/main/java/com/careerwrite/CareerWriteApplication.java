package com.careerwrite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This is the entry point of the whole backend.
// Running this file starts an embedded Tomcat server on port 8080
// and wires up all our @Controller, @Service and @Repository beans.
@SpringBootApplication
public class CareerWriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareerWriteApplication.class, args);
    }
}
