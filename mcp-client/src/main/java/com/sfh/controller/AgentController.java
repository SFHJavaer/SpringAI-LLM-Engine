package com.sfh.controller;

import com.sfh.pojo.ChatEntity;
import com.sfh.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat")
public class AgentController {


    @Autowired
    private AgentService agentService;

    @PostMapping("doChat")
    public void chat(@RequestBody ChatEntity chatEntity) {
        agentService.chat(chatEntity);
    }


}
