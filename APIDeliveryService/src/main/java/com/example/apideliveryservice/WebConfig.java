package com.example.apideliveryservice;

import com.example.apideliveryservice.interceptor.ExceptionResponseInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ExceptionResponseInterceptor())
            .order(1)
            .addPathPatterns("/**");
    }
}
