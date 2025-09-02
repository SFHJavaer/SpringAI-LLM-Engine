package com.sfh.controller;

import com.sfh.pojo.ChatEntity;
import com.sfh.service.AgentService;
import com.sfh.service.DocumentService;
import com.sfh.util.CommonResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName HelloController
 * @Author sfh
 * @Version 1.0
 * @Description HelloController
 **/
@RestController
@RequestMapping("rag")
public class RagController {

    @Resource
    private DocumentService documentService;

    @Resource
    private AgentService agentService;

    @PostMapping("/uploadRagDoc")
    public CommonResult uploadRagDoc(@RequestParam("file") MultipartFile file ){
        List<Document> documentList =  documentService.loadText(file.getResource(), file.getOriginalFilename());
        return CommonResult.ok(documentList);
    }

    @PostMapping("/search")
    public void search(@RequestBody ChatEntity chatEntity, HttpServletResponse response) {

        List<Document> list = documentService.doSearch(chatEntity.getMessage());
        response.setCharacterEncoding("UTF-8");
        agentService.doChatRagSearch(chatEntity, list);
    }

}
