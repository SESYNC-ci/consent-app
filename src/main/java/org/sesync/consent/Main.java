package org.sesync.consent;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author msmorul
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Main extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
