package com.autogpt4j.memory.pinecone.repository;

import com.autogpt4j.content.Chunk;
import com.autogpt4j.content.Content;
import com.google.protobuf.Struct;
import com.theokanning.openai.embedding.Embedding;
import io.pinecone.PineconeClient;
import io.pinecone.PineconeClientConfig;
import io.pinecone.PineconeConnection;
import io.pinecone.PineconeConnectionConfig;
import io.pinecone.proto.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PineconeRepository {

    private final PineconeClient pineconeClient;

    @Value("${PINECONE_API_KEY}")
    private String apiKey;

    @Value("${PINECONE_ENVIRONMENT}")
    private String environment;

    @Value("${PINECONE_PROJECT_NAME}")
    private String projectName;

    @Value("${PINECONE_INDEX_NAME}")
    private String indexName;

    @Value("${PINECONE_NAMESPACE_INDEX}")
    private String nameSpace;

    public PineconeRepository() {
        PineconeClientConfig pineconeClientConfig = new PineconeClientConfig()
                .withApiKey(apiKey)
                .withEnvironment(environment)
                .withProjectName(projectName)
                .withServerSideTimeoutSec(10);

        pineconeClient = new PineconeClient(pineconeClientConfig);
    }

    public void upsertContentAndEmbedding(Content content) {
        PineconeConnection conn = getPineconeConnection();

        content.getChunks().forEach(chunk -> {
            Vector upsertData = Vector.newBuilder()
                    .addAllValues(toFloatEmbeddings(chunk.getEmbedding()))
                    .setMetadata(Struct.newBuilder()
                            .putFields(Chunk.CHUNK, getMetaValue(chunk.getSubText()))
                            .build())
                    .setId(content.getContentId())
                    .build();

            UpsertRequest request = UpsertRequest.newBuilder()
                    .addVectors(upsertData)
                    .setNamespace(nameSpace)
                    .build();

            conn.getBlockingStub().upsert(request);
        });
    }

    public List<Chunk> getChunks(Embedding queryEmbedding) {
        PineconeConnection pineconeConnection = getPineconeConnection();

        QueryVector queryVector = QueryVector.newBuilder()
                .addAllValues(toFloatEmbeddings(queryEmbedding))
                .setNamespace(nameSpace)
                .build();

        QueryRequest queryRequest = QueryRequest.newBuilder()
                .addQueries(queryVector)
                .setNamespace(nameSpace)
                .setTopK(2)
                .setIncludeMetadata(true)
                .build();

        QueryResponse queryResponse = pineconeConnection.getBlockingStub().query(queryRequest);

        return new ArrayList<>();
    }

    private PineconeConnection getPineconeConnection() {
        return pineconeClient.connect(
                new PineconeConnectionConfig()
                        .withIndexName(indexName));
    }

    private com.google.protobuf.Value getMetaValue(String value) {
        return com.google.protobuf.Value.newBuilder()
                .setStringValue(value)
                .build();
    }

    private List<Float> toFloatEmbeddings(Embedding embedding) {
        return embedding.getEmbedding().stream()
                .map(it -> it.floatValue())
                .collect(Collectors.toList());
    }
}
