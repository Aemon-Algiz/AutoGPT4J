package com.autogpt4j.llm.tokenization;

import com.autogpt4j.content.Content;

/**
 * Credit to eisber (Markus Cozowicz) for this, check out his GitHub! https://github.com/eisber/tiktoken
 */
public abstract class TokensAndChunking {

    public abstract void chunkContent(Content content);

    public abstract Integer getEmbeddingTokenCount(String content);

    public abstract Integer getContextTokenCount(String content);

    public abstract Integer getMaximumContextTokens();

    public abstract String getEmbeddingModelName();
}