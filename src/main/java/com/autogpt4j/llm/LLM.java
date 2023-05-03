package com.autogpt4j.llm;

import com.autogpt4j.llm.LLMManager;
import com.autogpt4j.llm.openai.manager.OpenAIManager;
import lombok.Getter;

@Getter
public enum LLM {

    OPENAI(new OpenAIManager());

    private LLMManager llmManager;

    LLM(LLMManager llmManager) {
        this.llmManager = llmManager;
    }

}
