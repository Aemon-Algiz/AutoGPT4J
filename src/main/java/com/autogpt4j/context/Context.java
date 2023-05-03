package com.autogpt4j.context;

import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public abstract class Context {

    public abstract void addToContext(ChatMessage chatMessage);

    public abstract List<ChatMessage> getCurrentContext();

}
