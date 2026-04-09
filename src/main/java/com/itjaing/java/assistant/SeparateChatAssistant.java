package com.itjaing.java.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

/**
 * package com.itjaing.java.assistant
 * author 江小康
 * date 2026/4/1 20:52
 * description
 */
@AiService(
        wiringMode = EXPLICIT,
        chatModel = "qwenChatModel",
        //chatMemory = "chatMemory",
        chatMemoryProvider = "chatMemoryProvider"
)
public interface SeparateChatAssistant {
    /**
     * 分离聊天记录
     * @param memoryId 聊天id
     * @param userMessage 用户消息
     * @return
     */
    @SystemMessage("你是我的好朋友，请用东北话回答问题。")//系统消息提示词
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);
}