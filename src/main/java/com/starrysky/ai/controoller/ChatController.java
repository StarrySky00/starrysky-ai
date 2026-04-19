package com.starrysky.ai.controoller;

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
public class ChatController {
    private final ChatClient chatClient;
    private final InMemoryChatHistoryRepository chatHistoryRepository;
    @RequestMapping(value = "/chat",produces = "text/html;charset=UTF-8")
    public Flux<String> chat(String prompt,String chatId){
        chatHistoryRepository.save(TypeEnum.CHAT.getType(),chatId);
        return chatClient.prompt()
                .user(prompt)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,chatId))
                .stream()
                .content();
    }
}
