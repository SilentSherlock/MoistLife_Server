package com.program.moist.others.cfgs;

import com.program.moist.others.interceptors.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Date: 2021/3/21
 * Author: SilentSherlock
 * Description: describe the class features
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 两个星号递归匹配子目录
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor()).addPathPatterns(
                "/info/**",
                "/community/**",
                "/user/**",
                "/manage/**"
        );
    }

}
