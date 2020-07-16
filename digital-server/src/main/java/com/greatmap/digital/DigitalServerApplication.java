package com.greatmap.digital;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author gaorui
 */
@EnableDubbo()
@DubboComponentScan()
@SpringBootApplication
public class DigitalServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DigitalServerApplication.class,args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DigitalServerApplication.class);
    }
}
