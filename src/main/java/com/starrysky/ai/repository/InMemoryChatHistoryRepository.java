package com.starrysky.ai.repository;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author StarrySky
 * @date 2026/4/19 19:52 星期日
 */
@Component
public class InMemoryChatHistoryRepository implements ChatHistoryRepository {
    private final Map<String, List<String>> chatHistoryMap = new HashMap<>();
    @Override
    public void save(String type, String chatId) {
        // 这里可以实现将会话记录保存到内存中的逻辑，例如使用一个Map<String, List<String>>来存储不同类型的会话记录
        List<String> chatIds = chatHistoryMap.computeIfAbsent(type, k->new ArrayList<>());
        if(chatIds.contains(chatId)){
            return;
        }
        chatIds.add(chatId);
    }

    @Override
    public List<String> getChatId(String type) {
        // 这里可以实现从内存中获取会话记录的逻辑，例如根据type从Map中获取对应的List<String>
        return chatHistoryMap.getOrDefault(type, List.of());
    }
}
