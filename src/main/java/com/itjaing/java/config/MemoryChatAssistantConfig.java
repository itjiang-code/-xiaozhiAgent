package com.itjaing.java.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * package com.itjaing.java.config
 * author 江小康
 * date 2026/4/1 20:47
 * description
 */
@Configuration
public class MemoryChatAssistantConfig {
    @Bean
    ChatMemory chatMemory(){
        return MessageWindowChatMemory.withMaxMessages(10);
    }
}
