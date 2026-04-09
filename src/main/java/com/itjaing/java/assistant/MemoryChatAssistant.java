package com.itjaing.java.assistant;

import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

/**
 * package com.itjaing.java.assistant
 * author 江小康
 * date 2026/4/1 20:49
 * description
 */

@AiService(
        wiringMode = EXPLICIT,
        chatModel = "qwenChatModel",
        chatMemory = "chatMemory"
)
public interface MemoryChatAssistant {
    String chat(String message);
}