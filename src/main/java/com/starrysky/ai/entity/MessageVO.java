package com.starrysky.ai.entity;

import lombok.Data;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

/**
 * @author StarrySky
 * @date 2026/4/19 20:26 星期日
 */

@Data
public class MessageVO {
    private String role;
    private String content;
    public MessageVO(Message message){
        MessageType messageType = message.getMessageType();//这是一个枚举类，包含了对话的角色类型
        switch (messageType){
            case USER -> role = "user";
            case ASSISTANT -> role = "assistant";
            default -> role = "";
        }
        content = message.getText();
    }
}
