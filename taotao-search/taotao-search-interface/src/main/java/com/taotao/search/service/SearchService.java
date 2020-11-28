package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;

/**
 * @author wwt
 * @date 2019/9/24 13:51
 */
public interface SearchService {
    //导入所有的商品数据到索引库
    public TaotaoResult importAllSearchItems() throws Exception;


    /**
     * 根据搜索的条件查询结果
     * @param queryString 查询主条件
     * @param page 查询的当前页码
     * @param rows 每页显示的行数，在controller中写死
     * @return
     * @throws Exception
     */
    public SearchResult search(String queryString , Integer page, Integer rows) throws Exception;

    /**
     * 根据商品的id查询商品的数据，并且更新到索引库中
     * @param itemId
     * @return
     */
    public TaotaoResult updateItemById(Long itemId) throws Exception;
}
