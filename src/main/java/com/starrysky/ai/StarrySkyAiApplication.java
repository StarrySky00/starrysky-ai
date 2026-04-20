package com.starrysky.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author StarrySky
 */
@SpringBootApplication
@MapperScan("com.starrysky.ai.mapper")
public class StarrySkyAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarrySkyAiApplication.class, args);
    }

}
