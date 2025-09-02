package com.sfh.controller;


import com.sfh.pojo.ChatEntity;
import com.sfh.service.AgentService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HelloController
 * @Author sfh
 * @Version 1.0
 * @Description HelloController
 **/
@RestController
@RequestMapping("internet")
public class SearxngController {


    @Resource
    private AgentService agentService;

    @PostMapping("/search")
    public void search(@RequestBody ChatEntity chatEntity, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        agentService.doInternetSearch(chatEntity);
    }

}
