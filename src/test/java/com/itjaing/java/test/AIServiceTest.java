package com.itjaing.java.test;

import com.itjaing.java.assistant.Assistant;
import com.itjaing.java.assistant.MemoryChatAssistant;
import com.itjaing.java.assistant.SeparateChatAssistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.internal.chat.UserMessage;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * package com.itjaing.java.test
 * author 江小康
 * date 2026/4/1 20:25
 * description
 */
@SpringBootTest
public class AIServiceTest {
    @Autowired
    private QwenChatModel qwenChatModel;

    @Test
    public void testChat() {
        //创建AIService
        Assistant aiService = AiServices.create(Assistant.class, qwenChatModel);
        //调用service的接口
        String answer = aiService.chat("你好");
        System.out.println(answer);

    }
    @Autowired
    private Assistant assistant;

    @Test
    public void testAssistant() {
        String answer = assistant.chat("Hello");
        System.out.println(answer);
    }

    @Test
    public void testChatMemory(){
        //创建chatMemory
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        //创建AIService
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(qwenChatModel)
                .chatMemory(chatMemory)
                .build();
        String answer = assistant.chat("你好");
        System.out.println(answer);
    }

    @Autowired
    private MemoryChatAssistant memoryChatAssistant;

    @Test
    public void testChatMemory4() {

        String answer1 = memoryChatAssistant.chat("我是环环");
        System.out.println(answer1);

        String answer2 = memoryChatAssistant.chat("我是谁");
        System.out.println(answer2);

    }
    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void testChatMemory5() {

        String answer1 = separateChatAssistant.chat(1,"我是张三");
        System.out.println(answer1);

        String answer2 = separateChatAssistant.chat(1,"我是谁");
        System.out.println(answer2);

        String answer3 = separateChatAssistant.chat(2,"我是谁");
        System.out.println(answer3);

    }
}
