package com.spring.ai.firstproject.Service;

import org.jspecify.annotations.Nullable;
import reactor.core.publisher.Flux;

import java.util.List;


public interface ChatService {


    void SaveData(List<String> lsit);


    public String getResponse(String userQuery);
}
