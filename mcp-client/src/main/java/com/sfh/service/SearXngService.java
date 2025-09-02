package com.sfh.service;



import com.sfh.pojo.SearchResult;

import java.util.List;

public interface SearXngService {

    /**
     * @Description: 调用本地搜索引擎searxng进行搜索
     * @Author sfh
     * @param query
     * @return List<SearchResult>
     */
    public List<SearchResult> search(String query);

}
