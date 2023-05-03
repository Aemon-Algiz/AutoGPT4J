package com.autogpt4j.memory;

import com.autogpt4j.content.Chunk;
import com.autogpt4j.content.Content;

import java.util.List;

public abstract class MemoryManager {

    public abstract void upsertContentAndEmbedding(Content content);

    public abstract List<Chunk> getChunks(Content content);
}
