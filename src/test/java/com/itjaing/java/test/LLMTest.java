package com.itjaing.java.test;

import com.itjaing.java.XiaoZhiApp;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * LLM 测试类
 * package com.itjaing.java.test
 * author 江小康
 * date 2026/4/1 20:04
 * description 用于测试 LangChain4j 对接不同大语言模型的功能，包括直接初始化和 Spring Boot 整合方式
 */
@SpringBootTest(classes = XiaoZhiApp.class)
public class LLMTest {

    /**
     * gpt-4o-mini 语言模型接入测试
     * 通过手动构建 OpenAiChatModel 实例，使用 LangChain4j 提供的演示代理服务器进行测试
     */
    @Test
    public void testGPTDemo() {
        // 初始化 OpenAI 聊天模型实例
        OpenAiChatModel model = OpenAiChatModel.builder()
                // 设置基础 URL：LangChain4j 提供的代理服务器，该代理服务器会将演示密钥替换成真实密钥，再将请求转发给 OpenAI API
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                // 设置模型 apiKey：使用演示密钥 "demo"
                .apiKey("demo")
                // 设置模型名称：指定使用 gpt-4o-mini 模型
                .modelName("gpt-4o-mini")
                // 构建模型实例
                .build();

        // 向模型发送提问消息："你好"
        String answer = model.chat("你好");
        // 输出模型返回的回答结果到控制台
        System.out.println(answer);
    }

    /**
     * 整合 Spring Boot 测试：使用自动注入的 OpenAiChatModel 实例
     * 该实例由 Spring 容器根据配置文件自动创建和管理
     */
    @Autowired
    private OpenAiChatModel openAiChatModel;

    /**
     * 测试 Spring Boot 整合后的 OpenAI 模型调用
     * 验证通过依赖注入方式使用 OpenAiChatModel 是否正常工作
     */
    @Test
    public void testSpringBootOpenAI() {
        // 向自动注入的模型发送提问消息："你好"
        String answer = openAiChatModel.chat("你好");
        // 输出模型返回的回答结果到控制台
        System.out.println(answer);
    }

    /**
     * 整合 Spring Boot 测试：使用自动注入的通用 ChatModel 接口
     * 该接口可适配多种聊天模型实现（如 DeepSeek、OpenAI 等），由 Spring 容器根据配置决定具体实现
     */
//    @Autowired
//    private ChatModel chatModel;
//
//    /**
//     * 测试 Spring Boot 整合后的通用 ChatModel 调用（例如 DeepSeek 模型）
//     * 验证通过依赖注入方式使用 ChatModel 接口是否正常工作
//     */
//    @Test
//    public void testSpringBootDeepSeek() {
//        // 向自动注入的通用模型发送提问消息："你好"
//        // 下方注释代码为使用特定 OpenAiChatModel 的示例，当前使用更通用的 ChatModel 接口
//        // String answer = openAiChatModel.chat("你好");
//        String answer = chatModel.chat("你好");
//        // 输出模型返回的回答结果到控制台
//        System.out.println(answer);
//    }

    /**
     * 通义千问大模型
     */
    @Autowired
    private QwenChatModel qwenChatModel;

    @Test
    public void testDashScopeQwen() {
        //向模型提问
        String answer = qwenChatModel.chat("你好");
        //输出结果
        System.out.println(answer);
    }

    @Test
    public void testDashScopeWanx(){
        // 构建通义万相图像生成模型
        WanxImageModel wanxImageModel = WanxImageModel.builder()
                .modelName("wanx2.1-t2i-plus")
                .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                .build();

        // 提示词必须写在一行字符串里，不能直接换行
        String prompt = "奇幻森林精灵：在一片弥漫着轻柔薄雾的古老森林深处，阳光透过茂密枝叶洒下金色光斑。一位身材娇小、" +
                "长着透明薄翼的精灵少女站在一朵硕大的蘑菇上。她有着海藻般的绿色长发，发间点缀着蓝色的小花，皮肤泛着珍珠般的微光。" +
                "身上穿着由翠绿树叶和白色藤蔓编织而成的连衣裙，手中捧着一颗散发着柔和光芒的水晶球，周围环绕着五彩斑斓的蝴蝶，脚下是铺满苔藓的地面，" +
                "蘑菇和蕨类植物丛生，营造出神秘而梦幻的氛围。";

        // 生成图片
        Response<Image> response = wanxImageModel.generate(prompt);

        // 输出图片URL
        System.out.println("生成的图片地址：" + response.content().url());
    }
}
