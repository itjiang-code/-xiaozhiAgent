package com.itjaing.java.assistant;

import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

/**
 * package com.itjaing.java.assistant
 * author 江小康
 * date 2026/4/1 20:24
 * description 这是一个聊天的智能通用助手
 */
@AiService(wiringMode = EXPLICIT
        ,chatModel = "qwenChatModel"
)
public interface Assistant {
    String chat(String message);
}
