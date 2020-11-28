package com.taotao.search.dao;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 从索引库中搜索商品的dao
 * @author wwt
 * @date 2019/9/24 16:22
 */
@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    @Autowired
    private SearchItemMapper mapper;

    /**
     * 根据查询的条件查询商品结果集
     * @param query
     * @return
     * @throws Exception
     */
    public SearchResult search(SolrQuery query) throws Exception{
        SearchResult searchResult = new SearchResult();

        //1 创建solrserver，有spring管理
        //2 直接执行查询
        QueryResponse response = solrServer.query(query);
        //3 获取结果集
        SolrDocumentList results = response.getResults();
        //  设置SearchResult的总记录数
        searchResult.setRecordCount(results.getNumFound());
        //4 遍历结果集
        List<SearchItem> itemList = new ArrayList<>();
        //取高亮
        Map<String,Map<String,List<String>>> highlingting = response.getHighlighting();
        for (SolrDocument solrDocument : results){
            //将solrdocument中的属性一个个设置到searchitem中
            SearchItem item = new SearchItem();
            item.setCategory_name(solrDocument.get("item_category_name").toString());
            item.setId(Long.parseLong(solrDocument.get("id").toString()));
            item.setImage(solrDocument.get("item_image").toString());
            item.setPrice((Long) solrDocument.get("item_price"));
            item.setSell_point(solrDocument.get("item_sell_point").toString());
            //取高亮
            List<String> list = highlingting.get(solrDocument.get("id")).get("item_title");
            //判断list是否为空
            String gaoliangstr = "";
            if (list!=null && list.size()>0){
                gaoliangstr = list.get(0);
            }else {
                gaoliangstr = solrDocument.get("item_title").toString();
            }
            item.setTitle(gaoliangstr);
            //searchitem封装到SearchResult中是itemList属性中
            itemList.add(item);
        }
        //5 设置SearchResult属性
        searchResult.setItemList(itemList);
        return searchResult;
    }

    /**
     * 更新索引库
     * @param itemId
     * @return
     * @throws Exception
     */
    public TaotaoResult updateSearchItemById(Long itemId) throws Exception{
        SearchItem searchItem = mapper.getSearchItemById(itemId);

        //把记录更新到索引库
        SolrInputDocument document = new SolrInputDocument();

        document.addField("id",searchItem.getId());
        document.addField("item_title",searchItem.getTitle());
        document.addField("item_sell_point",searchItem.getSell_point());
        document.addField("item_price",searchItem.getPrice());
        document.addField("item_image",searchItem.getImage());
        document.addField("item_category_name",searchItem.getCategory_name());
        document.addField("item_desc",searchItem.getItem_desc());

        solrServer.add(document);

        solrServer.commit();
        return TaotaoResult.ok();
    }
}
