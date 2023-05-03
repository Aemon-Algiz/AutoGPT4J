package com.autogpt4j.llm.openai.repository;

import com.autogpt4j.content.Content;
import com.autogpt4j.llm.tokenization.TokensAndChunking;
import com.autogpt4j.llm.tokenization.model.OpenAIChunker;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.embedding.Embedding;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.List;

public class OpenAIRepository {

    private final TokensAndChunking tokensAndChunking;

    private final OpenAiService openAiService;

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    @Value("${OPEN_AI_MODEL}")
    private String model;

    public OpenAIRepository() {
        this.tokensAndChunking = new OpenAIChunker();
        this.openAiService = new OpenAiService(apiKey);
    }

    public Content getEmbeddings(Content content) {
        OpenAiService service = new OpenAiService(apiKey);

        tokensAndChunking.chunkContent(content);

        content.getChunks().stream()
                .forEach(chunk -> {
                    EmbeddingRequest embeddingRequest = EmbeddingRequest.builder()
                            .model(tokensAndChunking.getEmbeddingModelName())
                            .input(Collections.singletonList(chunk.getSubText()))
                            .build();

                    Embedding embedding = service.createEmbeddings(embeddingRequest).getData().get(0);

                    chunk.setEmbedding(embedding);
                });

        return content;
    }

    public List<ChatCompletionChoice> getCompletion(List<ChatMessage> chatMessages) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(model)
                .messages(chatMessages)
                .build();

        var request = openAiService.createChatCompletion(chatCompletionRequest);

        return request.getChoices();
    }

    public TokensAndChunking getTokenAndChunking() {
        return tokensAndChunking;
    }
}
