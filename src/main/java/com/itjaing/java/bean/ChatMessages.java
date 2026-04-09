package com.itjaing.java.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * package com.itjaing.java.bean
 * author 江小康
 * date 2026/4/1 21:06
 * description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("chat_messages")
public class ChatMessages {
    //唯一标识，映射到 MongoDB 文档的 _id 字段
    @Id
    private ObjectId id;
    private Object messageId;
    private String content; //存储当前聊天记录列表的json字符串
}
