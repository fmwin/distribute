package com.assess.config;

import com.assess.filter.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private UserInterceptor userInterceptor;

    /**
     * 配置拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor).addPathPatterns("/back/**");
        registry.addInterceptor(userInterceptor).addPathPatterns("/api/**");
        registry.addInterceptor(userInterceptor).addPathPatterns("/manager/**");
        registry.addInterceptor(userInterceptor).addPathPatterns("/web/**");
    }
}
