package com.starrysky.ai.repository;

import java.util.List;

/**
 * @author StarrySky
 * @date 2026/4/19 19:18 星期日
 */

public interface ChatHistoryRepository {
    /**
     * 保存会话id
     * @param type   会话类型
     * @param chatId 会话id
     */
    void save(String type,String chatId);
    /**
     * 获取会话id
     * @param type 会话类型
     */
    List<String> getChatId(String type);
}
