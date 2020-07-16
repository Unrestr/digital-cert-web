package com.greatmap.digital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author greatmap
 */
@SpringBootApplication(scanBasePackages = "com.greatmap.digital")
public class DigitalApiProviderApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DigitalApiProviderApplication.class,args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DigitalApiProviderApplication.class);
    }
}
