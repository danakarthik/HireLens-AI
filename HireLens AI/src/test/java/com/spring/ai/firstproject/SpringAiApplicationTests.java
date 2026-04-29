package com.spring.ai.firstproject;

import com.spring.ai.firstproject.Service.ChatService;
import com.spring.ai.firstproject.Service.DataLoaderServer.DataLoader;
import com.spring.ai.firstproject.Service.DataTransformService.DataTransform;
import com.spring.ai.firstproject.helper.Helper;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.mariadb.autoconfigure.MariaDbStoreAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest

class SpringAiApplicationTests {

    @Autowired
    private ChatService chatService;

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private DataTransform dataTransform;

    @Autowired
    private VectorStore vectorStore;



    @Test
    void pdfLoads() {
        System.out.println("loading has been started");
        List<Document> documents = dataLoader.loadPdf();
        documents.forEach(doc -> System.out.println(doc.getText()));
        System.out.println("loding has finished..now starting the transform");
        List<Document> transformed = dataTransform.transform(documents);
        transformed.forEach(doc -> System.out.println(doc.getText()));
        System.out.println(transformed.size());

        System.out.println("data is saving to the vectorstore database.....");
        vectorStore.add(transformed);
        System.out.println("data stored in the vector data base................................................");


    }

}
