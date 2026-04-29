package com.spring.ai.firstproject.Service.DataLoaderServer;

import org.springframework.ai.document.Document;


import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfLoader implements DataLoader{

    @Value("classpath:Karthik_Bharatapu_Java_Developer.pdf")
    private Resource pdfResource;

    @Value(("classpath:JAVA_Developer_KARTHIK_BHARATAPU.docx"))
    private Resource wordResource;

    @Override
    public List<Document> loadPdf() {
//        PagePdfDocumentReader documentReader = new PagePdfDocumentReader(resource,
//                PdfDocumentReaderConfig.builder()
//                        .withPageTopMargin(0)
//                        .withPageExtractedTextFormatter(
//                                ExtractedTextFormatter.builder()
//                                        .withNumberOfTopTextLinesToDelete(0)
//                                        .build()
//                        )
//
//
//                        .build()
//        );
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(wordResource);


        return tikaDocumentReader.read();
    }
}
