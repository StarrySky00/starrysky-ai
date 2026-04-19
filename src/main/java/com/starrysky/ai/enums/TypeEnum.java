package com.starrysky.ai.enums;

/**
 * @author StarrySky
 * @date 2026/4/19 20:03 星期日
 */

public enum TypeEnum {
    /**
     * 会话类型枚举
     */
    CHAT("chat"),
    ;

    private final String type;

    TypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
