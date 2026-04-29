package com.spring.ai.firstproject.Service.DataTransformService;

import org.springframework.ai.document.Document;

import java.util.List;

public interface DataTransform {

    List<Document> transform(List<Document> documents);
}
