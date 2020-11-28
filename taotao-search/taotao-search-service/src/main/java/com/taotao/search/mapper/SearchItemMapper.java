package com.taotao.search.mapper;

import com.taotao.common.pojo.SearchItem;

import java.util.List;

/**
 * 定义Mapper关联查询3张表，查询出搜索时的商品数据
 * @author wwt
 * @date 2019/9/24 11:50
 */
public interface SearchItemMapper {
    //查询所有商品数据
    public List<SearchItem> getSearchItemList();

    //根据商品的id查询商品是数据
    public SearchItem getSearchItemById(Long itemId);
}
