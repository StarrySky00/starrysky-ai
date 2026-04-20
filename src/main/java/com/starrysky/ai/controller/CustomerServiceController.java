package com.starrysky.ai.controller;

import com.starrysky.ai.enums.TypeEnum;
import com.starrysky.ai.repository.InMemoryChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


/**
 * @author StarrySky
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class CustomerServiceController {
    private final ChatClient serviceChatClient;
    private final InMemoryChatHistoryRepository chatHistoryRepository;
    @RequestMapping(value = "/service",produces = "text/html;charset=UTF-8")
    public Flux<String> service(String prompt,String chatId){
        chatHistoryRepository.save(TypeEnum.SERVICE.getType(),chatId);
        return serviceChatClient.prompt()
                .user(prompt)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,chatId))
                .stream()
                .content();
    }
}
