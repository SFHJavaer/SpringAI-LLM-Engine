package com.sfh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName SearchResult
 * @Author sfh
 * @Version 1.0
 * @Description SearchResult
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {

    private String title;
    private String url;
    private String content;
    private double score;

}
