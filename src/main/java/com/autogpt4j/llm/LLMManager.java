package com.autogpt4j.llm;

import com.autogpt4j.content.Content;
import com.autogpt4j.llm.tokenization.TokensAndChunking;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public abstract class LLMManager {

    public abstract Content populateEmbeddings(Content content);

    public abstract List<ChatCompletionChoice> getCompletion(List<ChatMessage> chatMessages);

    public abstract TokensAndChunking getTokenAndChunking();
}
