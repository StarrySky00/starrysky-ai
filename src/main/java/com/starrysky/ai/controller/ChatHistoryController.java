package com.starrysky.ai.controller;

/**
 * @author StarrySky
 * @date 2026/4/19 20:17 星期日
 */

import com.starrysky.ai.entity.MessageVO;
import com.starrysky.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/history")
public class ChatHistoryController {
    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatMemory chatMemory;

    @GetMapping("/{type}")
    public List<String> getChatId(@PathVariable("type") String type) {
        return chatHistoryRepository.getChatId(type);
    }

    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> getChatHistory(@PathVariable("type") String type,
                                          @PathVariable("chatId") String chatId) {
        List<Message> messages = chatMemory.get(chatId);
        if (messages.isEmpty()) {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();
    }
}
