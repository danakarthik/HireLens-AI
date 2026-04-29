package com.spring.ai.firstproject.Service.DataTransformService;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class DataTransformImpl implements DataTransform {
    @Override
    public List<Document> transform(List<Document> documents) {

        TokenTextSplitter splitter = TokenTextSplitter.builder()

                .build();
        return splitter.transform(documents);
    }
}
