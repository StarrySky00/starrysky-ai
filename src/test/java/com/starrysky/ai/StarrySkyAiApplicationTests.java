package com.starrysky.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@SpringBootTest
class StarrySkyAiApplicationTests {

    @Autowired
    private VectorStore vectorStore;



    @Test
    public void testVectorStore() {
        // 1. 创建一个PDF文件路径
        FileSystemResource resource = new FileSystemResource("交通运输生涯发展报告.pdf");
        // 2. 创建一个PDF文件读取器
        //resource:"classpath:/sample1.pdf"
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource,
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())//文本格式化器
                        .withPagesPerDocument(1)//每几页作为一个document
                        .build());
        // 3. 读取pdf，拆分成document
        List<Document> documents = pdfReader.read();
        // 4. 写入向量数据库
        vectorStore.add(documents);
        // 5. 搜索
        SearchRequest request = SearchRequest.builder()
                .query("交通运输生涯如何扩展行业认知")
                .topK(1) //前几页
                .similarityThreshold(0.5) //评分阈值，大于几
                .filterExpression("file_name == '交通运输生涯发展报告.pdf'")//过滤仅查看符合条件的
                .build();
        List<Document> docs = vectorStore.similaritySearch(request);


        for (Document doc : docs) {
            System.out.println(doc.getId());
            System.out.println(doc.getScore());
            System.out.println(doc.getText());
        }


    }

}
