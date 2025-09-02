package com.sfh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

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
public class SearXNGResponse {

    private String query;
    private List<SearchResult> results;

}
