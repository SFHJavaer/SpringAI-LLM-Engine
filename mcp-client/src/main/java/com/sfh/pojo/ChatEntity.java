package com.sfh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatEntity {
    private String botMsgId;
    private String currentUserName;
    private String message;
    private String modelName;

}
