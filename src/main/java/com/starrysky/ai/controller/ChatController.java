package com.starrysky.ai.controller;

import com.starrysky.ai.enums.TypeEnum;
import com.starrysky.ai.repository.InMemoryChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.content.Media;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * @author StarrySky
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatController {
    private final ChatClient chatClient;
    private final InMemoryChatHistoryRepository chatHistoryRepository;

    @RequestMapping(value = "/chat", produces = "text/html;charset=UTF-8")
    public Flux<String> chat(@RequestParam("prompt") String prompt,
                             @RequestParam("chatId") String chatId,
                             @RequestParam(value = "files",required = false) List<MultipartFile> files
    ) {
        chatHistoryRepository.save(TypeEnum.CHAT.getType(), chatId);

        if (files == null || files.isEmpty()) {
            // 没有文件,纯文本
            return testChat(prompt, chatId);
        } else {
            // 有文件，多模态聊天
            return multiModalChat(prompt, chatId, files);
        }
    }

    private Flux<String> multiModalChat(String prompt, String chatId, List<MultipartFile> files) {
        //1. 解析多媒体
        List<Media> medias = files.stream()
                .map(f -> new Media(
                                MimeType.valueOf(Objects.requireNonNull(f.getContentType())),
                                f.getResource()
                        )
                )
                .toList();

        return chatClient.prompt()
                .user(p->p.text(prompt).media(medias.toArray(new Media[]{})))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();

    }

    private Flux<String> testChat(String prompt, String chatId) {
        return chatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }
}
