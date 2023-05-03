package com.autogpt4j.llm.openai.manager;

import com.autogpt4j.content.Content;
import com.autogpt4j.llm.LLMManager;
import com.autogpt4j.llm.openai.repository.OpenAIRepository;
import com.autogpt4j.llm.tokenization.TokensAndChunking;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAIManager extends LLMManager {

    private final OpenAIRepository openAIRepository;

    public OpenAIManager() {
        this.openAIRepository = new OpenAIRepository();
    }

    @Override
    public Content populateEmbeddings(Content content) {
        return openAIRepository.getEmbeddings(content);
    }

    @Override
    public List<ChatCompletionChoice> getCompletion(List<ChatMessage> chatMessages) {
        return openAIRepository.getCompletion(chatMessages);
    }

    @Override
    public TokensAndChunking getTokenAndChunking() {
        return openAIRepository.getTokenAndChunking();
    }

}
