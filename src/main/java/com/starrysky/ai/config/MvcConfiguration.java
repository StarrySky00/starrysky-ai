package com.starrysky.ai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author StarrySky
 * @date 2026/4/19 18:30 星期日
 */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //允许的请求路径：匹配所有后端接口（如 /api/chat、/user/login 等）
        registry.addMapping("/**")
                //允许请求的域名：只信任本地 Vite 开发服务器（默认端口 5173）
                .allowedOrigins("http://localhost:5173")
                //允许的请求方法：覆盖 RESTful 常用操作 + OPTIONS（预检请求必需）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                //请求头任意：方便开发时携带 Content-Type、Authorization 等
                .allowedHeaders("*");
    }
}
