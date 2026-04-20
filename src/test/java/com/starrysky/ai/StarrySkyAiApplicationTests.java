package com.starrysky.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class StarrySkyAiApplicationTests {

    @Autowired
    private OpenAiEmbeddingModel embeddingModel;

    @Test
    void contextLoads() {
        float[] floats = embeddingModel.embed("学Spring Ai就到哔哩哔哩");
        System.out.println(Arrays.toString(floats));
    }

}
