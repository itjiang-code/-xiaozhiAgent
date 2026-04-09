package com.itjaing.java.test;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * package com.itjaing.java.test
 * author 江小康
 * date 2026/4/2 12:30
 * description
 */
@SpringBootTest
public class EmbeddingTest {
    @Autowired
    private EmbeddingModel embeddingModel;

    @Test
    public void testEmbeddingModel(){
        //通过我们的向量模型,将文字转为向量
        Response<Embedding> embed = embeddingModel.embed("你好");
        System.out.println("向量维度：" + embed.content().vector().length);
        System.out.println("向量输出：" + embed.toString());
    }

    @Autowired
    private EmbeddingStore embeddingStore;
    /**
     * 将文本转换成向量，然后存储到pinecone中
     *
     * 参考：
     * https://docs.langchain4j.dev/tutorials/embedding-stores
     */
    @Test
    public void testPineconeEmbeded() {
        //将文本转换成向量
        TextSegment segment1 = TextSegment.from("我喜欢羽毛球");
        Embedding embedding1 = embeddingModel.embed(segment1).content();
        //存入向量数据库
        embeddingStore.add(embedding1, segment1);

        TextSegment segment2 = TextSegment.from("今天天气很好");
        Embedding embedding2 = embeddingModel.embed(segment2).content();
        embeddingStore.add(embedding2, segment2);
    }

    /**
     * Pinecone-相似度匹配
     */
    @Test
    public void embeddingSearch() {
        //提问，并将问题转成向量数据
        Embedding queryEmbedding = embeddingModel.embed("你最喜欢的运动是什么？").content();
        //创建搜索请求对象
        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(1) //匹配最相似的一条记录
                .minScore(0.8)
                .build();

        //根据搜索请求 searchRequest 在向量存储中进行相似度搜索
        EmbeddingSearchResult<TextSegment> searchResult =
                embeddingStore.search(searchRequest);
        List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();
        matches.forEach(match -> {
            System.out.println(match.embedded().text());
            System.out.println("match = " + match);
            System.out.println("match.score() = " + match.score());
        });
    }

    @Test
    public void testUploadKnowledgeLibrary() {
        //使用FileSystemDocumentLoader读取指定目录下的知识库文档
        //并使用默认的文档解析器对文档进行解析
        Document document1 = FileSystemDocumentLoader.loadDocument("D:\\JAVA_FILE\\knowledge\\hospitalknowledge\\医院信息.md");
        Document document2 = FileSystemDocumentLoader.loadDocument("D:\\JAVA_FILE\\knowledge\\hospitalknowledge\\科室信息.md");
        Document document3 = FileSystemDocumentLoader.loadDocument("D:\\JAVA_FILE\\knowledge\\hospitalknowledge\\神经内科.md");

        List<Document> documents = Arrays.asList(document1, document2, document3);
        //文本向量化并存入向量数据库：将每个片段进行向量化，得到一个嵌入向量
        EmbeddingStoreIngestor
                .builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .build()
                .ingest(documents);
    }
}
