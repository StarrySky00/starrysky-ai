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
 * @date 2026/4/20 10:39 星期一
 */

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class GameController {
    private final ChatClient gameChatClient;
    private final InMemoryChatHistoryRepository chatHistoryRepository;
    @RequestMapping(value = "/game",produces = "text/html;charset=UTF-8")
    public Flux<String> chat(String prompt, String chatId){
        return gameChatClient.prompt()
                .user(prompt)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,chatId))
                .stream()
                .content();
    }
}
