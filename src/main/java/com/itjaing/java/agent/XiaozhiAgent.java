package com.itjaing.java.agent;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

/**
 * package com.itjaing.java.agent
 * author 江小康
 * date 2026/4/1 21:43
 * description
 */
@AiService(
        wiringMode =EXPLICIT,
        chatModel = "qwenChatModel",
        chatMemoryProvider = "chatMemoryProviderXiaozhi",
        streamingChatModel="qwenStreamingChatModel",
        tools = "appointmentTools",
        //contentRetriever = "contentRetrieverXiaozhi" //配置向量存储
        contentRetriever = "contentRetrieverXiaozhiPincone"
)
public interface XiaozhiAgent {
    @SystemMessage(fromResource = "xiaozhi-prompt-template.txt")
    Flux<String> chat(@MemoryId Long memoryId, @UserMessage String message);
}
