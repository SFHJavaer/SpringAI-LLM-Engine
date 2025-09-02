package com.sfh.service.impl;


import com.sfh.service.DocumentService;
import com.sfh.util.CustomTextSplitter;
import com.sfh.util.MyTextSpliter;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName DocumentServiceImpl
 * @Author sfh
 * @Version 1.0
 * @Description DocumentServiceImpl
 **/
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private RedisVectorStore redisVectorStore;

    @Override
    public List<Document> loadText(Resource resource, String fileName) {

        // 加载读取文档
        TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("fileName", fileName);
        List<Document> documentList = textReader.get();


//        默认的文本切分器
//        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
//        List<Document> list = tokenTextSplitter.apply(documentList);

        List<String> splitList = Arrays.asList("。", "！", "？", System.lineSeparator());
        List<Document> splitDocuments  = new MyTextSpliter(800, 350, 5, 10000, true, splitList).apply(documentList);

        //换行切分
//        CustomTextSplitter tokenTextSplitter = new CustomTextSplitter();
//        List<Document> list = tokenTextSplitter.apply(documentList);


        // 向量存储
        redisVectorStore.add(splitDocuments);

        return documentList;
    }

    @Override
    public List<Document> doSearch(String question) {
        return redisVectorStore.similaritySearch(question);
    }
}
