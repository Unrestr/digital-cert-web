package com.greatmap.digital.config;


import com.greatmap.digital.aop.CommonInterceptor;
import com.greatmap.digital.util.SnowFlakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author gaorui
 */
@Configuration
public class CommonConfig extends WebMvcConfigurationSupport {

    @Autowired
    private CommonInterceptor commonInterceptor;

    /**
     * 雪花id生成器
     * @param workId
     * @param dataCenterId
     * @return
     */
    @Bean
    public SnowFlakeIdWorker snowFlakeIdWorker(@Value("${snowFlakeIdWorker.workerId}") long workId,
                                               @Value("${snowFlakeIdWorker.dataCenterId}") long dataCenterId){
        return new SnowFlakeIdWorker(workId,dataCenterId);
    }

   /* @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }*/


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateConverter());
        super.addFormatters(registry);
    }
}
