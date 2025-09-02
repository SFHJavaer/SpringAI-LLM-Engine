package com.sfh.util;

import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.List;

/**
 * @ClassName CustomTextSplitter
 * @Author sfh
 * @Version 1.0
 * @Description CustomTextSplitter
 **/
public class CustomTextSplitter extends TextSplitter {

    @Override
    protected List<String> splitText(String text) {
        return List.of(split(text));
    }

    public String[] split(String text) {
        return text.split("\\s*\\R\\s*\\R\\s*");
    }
}
