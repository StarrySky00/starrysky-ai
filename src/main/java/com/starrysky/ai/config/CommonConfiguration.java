package com.starrysky.ai.config;

import com.starrysky.ai.constants.SystemConstants;
import com.starrysky.ai.tools.CourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.observation.conventions.VectorStoreProvider;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
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

    // pdf搜索ChatClient
    @Bean
    public ChatClient pdfChatClient(OpenAiChatModel model, ChatMemory chatMemory, VectorStore vectorStore) {
        return ChatClient
                .builder(model)// 大模型
                .defaultSystem("请根据上下文回答问题，遇到上下文没有的问题不要随意编造")//系统角色
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), //环绕日志增强
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),//环绕记忆增强
                        QuestionAnswerAdvisor
                                .builder(vectorStore)// 添加向量搜索
                                .searchRequest(
                                        SearchRequest.builder()//搜索请求
                                                .similarityThreshold(0.6d)//相似度
                                                .topK(2) //两页
                                                .build())
                                .build()
                )
                .build();
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
