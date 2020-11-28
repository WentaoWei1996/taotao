package com.taotao.search.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

/**
 * @author wwt
 * @date 2019/9/24 13:56
 */
public class SolrjTest {
    @Test
    public void add() throws IOException, Exception {
        //1 创建连接
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr");
        //2 创建solrinputdocument
        SolrInputDocument document = new SolrInputDocument();
        //3 向文档中添加域
        document.addField("id","test001");
        document.addField("item_title","test");
        //4 将文档提交到索引库中
        solrServer.add(document);
        //5 提交
        solrServer.commit();
    }

    @Test
    public void testQuery() throws Exception {
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr");
        SolrQuery query = new SolrQuery();
        query.setQuery("云石白");
        query.setFilterQueries("item_price:[0 TO 3000000000]");
        query.set("df","item_title");

        QueryResponse response = solrServer.query(query);

        SolrDocumentList results = response.getResults();
        System.out.println("查询的总记录数："+results.getNumFound());

        for (SolrDocument solrDocument : results){
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
        }
    }
}
