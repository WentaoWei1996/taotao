package com.taotao.search.test;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * @author wwt
 * @date 2019/9/25 14:40
 */
public class SolrCloudTest {

    @Test
    public void testAdd() throws Exception{
        CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.128:2181,192.168.25.128:2182,192.168.25.128:2183");

        cloudSolrServer.setDefaultCollection("collection2");
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","testCloudId");
        document.addField("item_title","今天好天气");

        cloudSolrServer.add(document);

        cloudSolrServer.commit();
    }
}
