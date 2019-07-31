package com.dodo.system.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dodo.system.interceptor.Interceptor;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Configuration
@MapperScan(value= {"com.dodo.system.mapper"})
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    Interceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/admin/**","/home/**","/system/**")
                .excludePathPatterns("/","/*"
                		,"/home/emp/download-img"
                		,"/home/emp/download-img/**"
                        ,"/home/emp/upload-img"
                        ,"/home/docs/find/dept"
                );
    }
    
}
