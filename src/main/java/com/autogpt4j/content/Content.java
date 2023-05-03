package com.autogpt4j.content;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class Content  {

    private String contentId;

    private String text;

    private List<Chunk> chunks = new ArrayList<>();

    public List<Chunk> getChunks() {
        if(chunks.size() > 0) {
            return chunks;
        }

        return List.of(new Chunk()
                .setSubContentId(contentId)
                .setSubText(text));
    }

}
