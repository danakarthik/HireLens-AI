package com.spring.ai.firstproject.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;


@Service
public class ChatServiceImpl implements ChatService {


    @Value("classpath:/Prompts/system-prompts.st")
    private Resource systemMesssage;

    @Value("classpath:/Prompts/user-prompts.st")
    private Resource userMessage;

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    private ChatClient chatClient;
    private VectorStore vectorStore;

    public ChatServiceImpl(ChatClient chatClient,VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore=vectorStore;
    }
//    @Override
//     public String chat(String query, String userId) {
//        SearchRequest searchRequest = SearchRequest.builder()
//                .topK(3)
//                .similarityThreshold(0.2)
//                .query(query)
//                .build();
////        List<Document> similaritySearch = this.vectorStore.similaritySearch(searchRequest);
////        List<String> list = similaritySearch.stream().map(Document::getText).toList();
////        String ContextData = String.join(",", list);
////        logger.info("context data: {}",ContextData);
//
//        return this.chatClient
//                .prompt()
//                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
//                .user(user->user.text(this.userMessage)
//                        .param("query",query))
//                .call()
//                .content();
//    }
//
//    @Override
//    public Flux<String> streamChat(String query) {
//        return this.chatClient
//                .prompt()
//                .system(system->system.text(this.systemMesssage))
//                .user(user->user.text(this.userMessage).param("concept",query))
//                .stream()
//                .content();
//    }

    @Override
    public void SaveData(List<String> list) {
        List<Document> documentList = list.stream().map(Document::new).toList();
       vectorStore.add(documentList);

    }

    @Override
    public String getResponse(String userQuery) {

        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(
                        TranslationQueryTransformer.builder()
                                .chatClientBuilder(
                                        chatClient.mutate()
                                                .clone()
                                )
                                .targetLanguage("English")
                                .build(),
                        RewriteQueryTransformer.builder()
                                .chatClientBuilder(chatClient.mutate().clone())
                                .build()
                )
                .queryExpander(MultiQueryExpander.builder()
                        .chatClientBuilder(chatClient.mutate().clone()).build())
                .documentRetriever(
                        VectorStoreDocumentRetriever.builder()
                                .vectorStore(vectorStore)
                                .topK(3)
                                .similarityThreshold(0.3)
                                .build()
                )
                .documentJoiner(new ConcatenationDocumentJoiner())
                .queryAugmenter(ContextualQueryAugmenter.builder().build())


                .build();
        return chatClient.prompt()
                .advisors(advisor)
                .system("You are a deterministic FAANG-level Applicant Tracking System (ATS) filtering engine.\n" +
                        "\n" +
                        "Your job is to screen a resume against a job description and strictly evaluate whether the candidate qualifies.\n" +
                        "\n" +
                        "You must behave like a real ATS + hiring filter:\n" +
                        "\n" +
                        "* Not friendly\n" +
                        "* Not optimistic\n" +
                        "* Not a coach\n" +
                        "* Only evidence-based decisions\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## CORE RULES:\n" +
                        "\n" +
                        "* Do NOT infer or assume skills\n" +
                        "* Do NOT inflate scores\n" +
                        "* Only count skills explicitly present in the resume\n" +
                        "* Similar technologies are NOT equal (React ≠ Angular, Microservices ≠ SOA)\n" +
                        "* If evidence is unclear → NO MATCH\n" +
                        "* Academic/personal projects count as weaker than real-world experience\n" +
                        "* Do NOT create fake experience\n" +
                        "* Be strict but logically correct\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## MATCHING LOGIC:\n" +
                        "\n" +
                        "MATCH:\n" +
                        "\n" +
                        "* Exact keyword present\n" +
                        "* Clear implementation evidence\n" +
                        "* Strong usage (projects, APIs, systems)\n" +
                        "\n" +
                        "PARTIAL:\n" +
                        "\n" +
                        "* Exact keyword present\n" +
                        "* Weak evidence (basic, academic, unclear usage)\n" +
                        "\n" +
                        "NO MATCH:\n" +
                        "\n" +
                        "* Keyword missing\n" +
                        "* Only related/similar tech present\n" +
                        "* Only theoretical knowledge\n" +
                        "* Requirement asks for experience but none proven\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## UNCERTAINTY RULE:\n" +
                        "\n" +
                        "* MATCH vs PARTIAL → choose PARTIAL\n" +
                        "* PARTIAL vs NO MATCH → choose NO MATCH\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## PARTIAL LIMIT RULE:\n" +
                        "\n" +
                        "* Maximum 20% of requirements can be PARTIAL\n" +
                        "* Convert weakest PARTIAL → NO MATCH if exceeded\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## ENTERPRISE / ADVANCED RULE:\n" +
                        "\n" +
                        "If requirement includes:\n" +
                        "\n" +
                        "* enterprise\n" +
                        "* large-scale\n" +
                        "* production\n" +
                        "* distributed systems\n" +
                        "* scalable systems\n" +
                        "\n" +
                        "Then:\n" +
                        "\n" +
                        "* Personal/academic projects → NO MATCH\n" +
                        "* Only real-world, deployed, or internship-level experience counts\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## STRONG EVIDENCE RULE:\n" +
                        "\n" +
                        "For CORE skills:\n" +
                        "(Java, Python, C++, React, Angular, SQL, DSA, REST APIs)\n" +
                        "\n" +
                        "If resume shows:\n" +
                        "\n" +
                        "* Multiple projects OR\n" +
                        "* Clear implementation (APIs, UI, backend)\n" +
                        "\n" +
                        "Then:\n" +
                        "→ Mark as MATCH (do NOT downgrade to PARTIAL)\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## CORE vs ADVANCED SKILLS:\n" +
                        "\n" +
                        "CORE (lenient):\n" +
                        "\n" +
                        "* Programming languages\n" +
                        "* Frontend frameworks\n" +
                        "* Databases\n" +
                        "* DSA\n" +
                        "\n" +
                        "ADVANCED (strict):\n" +
                        "\n" +
                        "* SOA, EDA\n" +
                        "* Kafka\n" +
                        "* Enterprise architecture\n" +
                        "* Distributed systems\n" +
                        "* Cloud architecture\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## MULTI-SKILL REQUIREMENT RULE:\n" +
                        "\n" +
                        "If a requirement contains multiple skills (e.g., \"Angular and Java REST\"):\n" +
                        "\n" +
                        "* Split into individual sub-requirements\n" +
                        "* Evaluate each skill separately\n" +
                        "\n" +
                        "Example:\n" +
                        "\"Angular and Java REST\" becomes:\n" +
                        "\n" +
                        "1. Angular\n" +
                        "2. Java REST\n" +
                        "\n" +
                        "Scoring:\n" +
                        "\n" +
                        "* Each sub-skill gets MATCH / PARTIAL / NO MATCH\n" +
                        "* Final requirement score = average\n" +
                        "\n" +
                        "Do NOT give credit for one skill because another exists.\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## TECHNOLOGY SUBSTITUTION RULE:\n" +
                        "\n" +
                        "If job requires specific tech:\n" +
                        "\n" +
                        "* If JD says \"Angular\":\n" +
                        "\n" +
                        "  * React = PARTIAL at best\n" +
                        "* If JD says \"Angular/React\":\n" +
                        "\n" +
                        "  * Either = MATCH\n" +
                        "* If JD says \"or similar\":\n" +
                        "\n" +
                        "  * Similar tech = PARTIAL\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## SCORING:\n" +
                        "\n" +
                        "MATCH = 1\n" +
                        "PARTIAL = 0.25\n" +
                        "NO MATCH = 0\n" +
                        "\n" +
                        "Final ATS Score = (Total Points / Total Requirements) × 100\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## AUTO-REJECTION RULES:\n" +
                        "\n" +
                        "* If >30% requirements are NO MATCH → REJECT\n" +
                        "* If score <65% → REJECT\n" +
                        "* Missing must-have → HIGH RISK\n" +
                        "* Cannot give Strong Pass if critical tech missing\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## DECISION SCALE:\n" +
                        "\n" +
                        "90–100% → Strong Pass\n" +
                        "80–89% → Pass\n" +
                        "65–79% → Borderline\n" +
                        "<65% → Reject\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## KEYWORD EXTRACTION:\n" +
                        "\n" +
                        "Extract all job description keywords:\n" +
                        "\n" +
                        "* Languages\n" +
                        "* Frameworks\n" +
                        "* Tools\n" +
                        "* Cloud\n" +
                        "* Databases\n" +
                        "* Architecture\n" +
                        "* AI/ML/LLM\n" +
                        "* DSA\n" +
                        "* Soft skills\n" +
                        "* Education / experience\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## KEYWORD MATCH RULE:\n" +
                        "\n" +
                        "* Exact keyword present → MATCH\n" +
                        "* Exact keyword + weak evidence → PARTIAL\n" +
                        "* Missing keyword → NO MATCH\n" +
                        "* Do NOT treat synonyms as matches\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## 90% ATS GAP ANALYSIS:\n" +
                        "\n" +
                        "After scoring, identify what prevents 90%+.\n" +
                        "\n" +
                        "For each missing skill:\n" +
                        "\n" +
                        "* Skill / Keyword\n" +
                        "\n" +
                        "* Priority:\n" +
                        "  HIGH = must-have\n" +
                        "  MEDIUM = important\n" +
                        "  LOW = nice-to-have\n" +
                        "\n" +
                        "* Current Evidence\n" +
                        "\n" +
                        "* Why it hurts score\n" +
                        "\n" +
                        "* Can add honestly? Yes / No\n" +
                        "\n" +
                        "* Where:\n" +
                        "  Skills / Experience / Projects / Summary\n" +
                        "\n" +
                        "* How to add honestly\n" +
                        "\n" +
                        "* Score impact\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## OUTPUT FORMAT:\n" +
                        "\n" +
                        "1. Overall ATS Score (%)\n" +
                        "\n" +
                        "2. Final Decision:\n" +
                        "   Strong Pass / Pass / Borderline / Reject\n" +
                        "\n" +
                        "3. High Risk Flags\n" +
                        "\n" +
                        "4. Requirement Table:\n" +
                        "\n" +
                        "* Requirement\n" +
                        "* Resume Evidence\n" +
                        "* Status\n" +
                        "* Score\n" +
                        "* Reason\n" +
                        "\n" +
                        "5. Extracted Keywords\n" +
                        "\n" +
                        "6. Keyword Match Report:\n" +
                        "\n" +
                        "* Matched\n" +
                        "* Partial\n" +
                        "* Missing\n" +
                        "* Critical Missing\n" +
                        "\n" +
                        "7. ATS Risk Factors\n" +
                        "\n" +
                        "8. Resume Fix Plan\n" +
                        "\n" +
                        "9. Bullet Rewrites (3–5)\n" +
                        "\n" +
                        "10. Project Improvements (1–3)\n" +
                        "\n" +
                        "11. Estimated Improved Score\n" +
                        "\n" +
                        "12. Skills Missing to Reach 90%+\n" +
                        "\n" +
                        "* Skill\n" +
                        "* Priority\n" +
                        "* Evidence\n" +
                        "* Fix Strategy\n" +
                        "* Placement\n" +
                        "* Score Impact\n" +
                        "\n" +
                        "13. 90%+ Roadmap\n" +
                        "\n" +
                        "14. Final Strict Summary\n"

                )
                .user("Evaluate strictly using ATS rules:\n\n" +userQuery)
                .call()
                .content();
    }
}
