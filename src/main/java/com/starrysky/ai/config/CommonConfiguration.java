package com.starrysky.ai.config;

import com.starrysky.ai.constants.SystemConstants;
import com.starrysky.ai.tools.CourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.observation.conventions.VectorStoreProvider;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
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
    // 本地部署
    @Bean
    public ChatClient chatClient(OllamaChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultSystem("你是一个热心的智能助手，你的名字叫做小团团。请用温柔可爱的语气回答用户。")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), //环绕日志增强
                        MessageChatMemoryAdvisor.builder(chatMemory).build()//环绕记忆增强
                )
                .build();
    }

    // 游戏模型配置
    @Bean
    public ChatClient gameChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultSystem(SystemConstants.GAME_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), //环绕日志增强
                        MessageChatMemoryAdvisor.builder(chatMemory).build()//环绕记忆增强
                )
                .build();
    }

    // 客服模型配置
    @Bean
    public ChatClient serviceChatClient(OpenAiChatModel model, ChatMemory chatMemory, CourseTools tools) {
        return ChatClient
                .builder(model)
                .defaultSystem(SystemConstants.SERVICE_SYSTEM_PROMPT)//系统角色
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), //环绕日志增强
                        MessageChatMemoryAdvisor.builder(chatMemory).build()//环绕记忆增强
                )
                .defaultTools(tools) // 添加工具
                .build();
    }

    //向量库和向量模型配置
    @Bean
    public VectorStore vectorStore(OpenAiEmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    /*
     * 配置记忆库，使用默认实现的内存存储记忆
     *
     * 也可以实现ChatMemory接口，实现add，get，clear方法，自定义存储记忆的方式
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().build();
    }
}
