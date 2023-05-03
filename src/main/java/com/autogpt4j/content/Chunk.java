package com.autogpt4j.content;

import com.theokanning.openai.embedding.Embedding;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Chunk {

    public static final String CHUNK = "Chunk";

    private String subContentId;

    private String subText;

    private Embedding embedding;

}