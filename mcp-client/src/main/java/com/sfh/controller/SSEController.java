package com.sfh.controller;

import com.sfh.enums.SSEMsgType;
import com.sfh.server.SSEServer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("sse")
public class SSEController {


    @GetMapping(path = "connect", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter connect(@RequestParam String userId){
        return SSEServer.connect(userId);
    }


    @GetMapping("sendMessage")
    public Object sendMessage(@RequestParam String userId, @RequestParam String message){
        SSEServer.sendMsg(userId, message, SSEMsgType.MESSAGE);
        return "OK";
    }


    @GetMapping("sendMessageAdd")
    public Object sendMessageAdd(@RequestParam String userId, @RequestParam String message) throws Exception {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(300);
            SSEServer.sendMsg(userId, message, SSEMsgType.ADD);
        }
        return "OK";
    }


    @GetMapping("sendMessageAll")
    public Object sendMessageAll(@RequestParam String message){
        SSEServer.sendMsgToAllUsers(message);
        return "OK";
    }

}
