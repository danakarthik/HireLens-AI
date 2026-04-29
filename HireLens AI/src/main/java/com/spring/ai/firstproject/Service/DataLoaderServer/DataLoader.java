package com.spring.ai.firstproject.Service.DataLoaderServer;

import org.springframework.ai.document.Document;

import java.util.List;

public interface DataLoader {
    List<Document> loadPdf();
}
