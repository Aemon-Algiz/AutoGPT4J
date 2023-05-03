package com.autogpt4j.memory.pinecone.manager;

import static java.util.function.

import com.autogpt4j.content.Chunk;
import com.autogpt4j.content.Content;
import com.autogpt4j.memory.MemoryManager;
import com.autogpt4j.memory.pinecone.repository.PineconeRepository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PineconeManager extends MemoryManager {

    private final PineconeRepository pineconeRepository;

    public PineconeManager() {
        this.pineconeRepository = new PineconeRepository();
    }

    @Override
    public void upsertContentAndEmbedding(Content content) {
        pineconeRepository.upsertContentAndEmbedding(content);
    }

    @Override
    public List<Chunk> getChunks(Content content) {
        return content.getChunks().stream()
                .flatMap(chunk -> pineconeRepository.getChunks(chunk.getEmbedding()).stream())
                .filter(distinctByKey(p -> p.getSubContentId()))
                .collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
