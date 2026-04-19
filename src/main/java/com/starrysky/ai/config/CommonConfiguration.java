package com.starrysky.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author LIU
 */
@Configuration
public class CommonConfiguration {

    /*
     * 配置模型，也可以用其他的模型，openai的
     */
    @Bean
    public ChatClient chatClient(OllamaChatModel model){
        return ChatClient
                .builder(model)
                .defaultSystem("你是一个热心的智能助手，你的名字叫做小团团。请用温柔可爱的语气回答用户。")
                .defaultAdvisors(new SimpleLoggerAdvisor())//环绕日志增强
                .build();
    }
}
