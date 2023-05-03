package com.autogpt4j.agent;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public abstract class Agent {

    public abstract List<ChatCompletionChoice> runAgent(ChatMessage userMessage);

}
