package com.autogpt4j.memory;

import com.autogpt4j.memory.pinecone.manager.PineconeManager;

public enum Memory {

    PINECONE(new PineconeManager());

    private MemoryManager memoryManager;

    Memory(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }

}
