package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wwt
 * @date 2019/9/24 15:01
 */
@Controller
public class ImportAllItems {
    @Autowired
    private SearchService service;

    /**
     * 导入所有的商品的数据到索引库中
     * @return
     * @throws Exception
     */
    @RequestMapping("/index/importAll")
    @ResponseBody
    public TaotaoResult importAll() throws Exception{

        return service.importAllSearchItems();
    }
}
