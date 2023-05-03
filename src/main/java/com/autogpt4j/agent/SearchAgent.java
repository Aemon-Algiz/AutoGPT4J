package com.autogpt4j.agent;

import com.autogpt4j.content.Chunk;
import com.autogpt4j.content.Content;
import com.autogpt4j.context.Context;
import com.autogpt4j.llm.LLMManager;
import com.autogpt4j.memory.MemoryManager;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

import java.util.List;

public class SearchAgent extends Agent {

    private final LLMManager llmManager;
    private final MemoryManager memoryManager;
    private final Context context;

    public SearchAgent(LLMManager llmManager, MemoryManager memoryManager, Context context) {
        this.llmManager = llmManager;
        this.memoryManager = memoryManager;
        this.context = context;
    }

    public List<ChatCompletionChoice> runAgent(ChatMessage userMessage) {
        context.addToContext(userMessage);

        Content content = llmManager.populateEmbeddings(new Content().setText(userMessage.getContent()));
        List<Chunk> chunks = memoryManager.getChunks(content);

        chunks.forEach(chunk -> context.addToContext(new ChatMessage(ChatMessageRole.USER.value(), chunk.getSubText())));

        List<ChatCompletionChoice> completionChoices = llmManager.getCompletion(context.getCurrentContext());

        return completionChoices;
    }

    public Context getContext() {
        return context;
    }

}
