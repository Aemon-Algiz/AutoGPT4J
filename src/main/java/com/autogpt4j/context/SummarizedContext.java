package com.autogpt4j.context;

import com.autogpt4j.llm.LLMManager;
import com.autogpt4j.prompts.Prompt;
import com.google.common.collect.Lists;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SummarizedContext extends Context {

    private final List<ChatMessage> chatMessages;
    private final LLMManager llmManager;

    public SummarizedContext(List<ChatMessage> chatMessages, LLMManager llmManager) {
        this.chatMessages = chatMessages;
        this.llmManager = llmManager;
    }

    @Override
    public void addToContext(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);

        summarizeChat();
    }

    @Override
    public List<ChatMessage> getCurrentContext() {
        return chatMessages;
    }

    private void summarizeChat() {
        String fullString = chatMessages.stream()
                .map(it -> it.getContent())
                .collect(Collectors.joining());

        Integer tokenCount = llmManager.getTokenAndChunking().getContextTokenCount(fullString);

        if(tokenCount > llmManager.getTokenAndChunking().getMaximumContextTokens()) {
            String currentContext = chatMessages.stream()
                    .filter(it -> it.getRole().equals(ChatMessageRole.USER)
                                || it.getRole().equals(ChatMessageRole.SYSTEM))
                    .map(it -> it.getContent())
                    .collect(Collectors.joining());

            String summary = llmManager.getCompletion(getSummaryContext(currentContext))
                    .get(0).getMessage().getContent();

            chatMessages.removeAll(getNonSystemMessages());
            addToContext(new ChatMessage(ChatMessageRole.USER.value(), summary));
        }
    }

    private List<ChatMessage> getNonSystemMessages() {
        return chatMessages.stream()
                .filter(chatMessage -> chatMessage.getRole().equals(ChatMessageRole.USER.value())
                        || chatMessage.getRole().equals(ChatMessageRole.SYSTEM.value()))
                .collect(Collectors.toList());
    }

    private List<ChatMessage> getSummaryContext(String currentContext) {
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), Prompt.SUMMARY_PROMPT);
        ChatMessage summaryMessage = new ChatMessage(ChatMessageRole.USER.value(), currentContext);

        return Lists.newArrayList(systemMessage, summaryMessage);
    }
}
