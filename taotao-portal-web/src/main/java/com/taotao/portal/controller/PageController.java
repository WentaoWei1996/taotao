package com.taotao.portal.controller;

import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.util.JsonUtils;
import com.taotao.portal.pojo.Ad1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wwt
 * @date 2019/9/20 10:52
 */

@Controller
public class PageController {

    @Autowired
    private ContentService contentService;

    @Value("${AD1_CATEGORY_ID}")
    private Long categoryId;

    @Value("${AD1_HEIGHT}")
    private String AD1_HEIGHT;

    @Value("${AD1_HEIGHT_B}")
    private String AD1_HEIGHT_B;

    @Value("${AD1_WIDTH}")
    private String AD1_WIDTH;

    @Value("${AD1_WIDTH_B}")
    private String AD1_WIDTH_B;

    @RequestMapping("/index")
    public String showIndex(Model model){
        //引入服务
        //添加业务逻辑，根据内容分类的id，查询内容列表
        List<TbContent> contentList = contentService.getContentListByCatId(categoryId);
        //转成自定义的pojo的列表
        List<Ad1Node> nodes = new ArrayList<>();
        for(TbContent tbContent : contentList){
            Ad1Node node = new Ad1Node();
            node.setAlt(tbContent.getSubTitle());
            node.setHeight(AD1_HEIGHT);
            node.setHeightB(AD1_HEIGHT_B);
            node.setHref(tbContent.getUrl());
            node.setSrc(tbContent.getPic());
            node.setSrcB(tbContent.getPic2());
            node.setWidth(AD1_WIDTH);
            node.setWidthB(AD1_WIDTH);

            nodes.add(node);
        }
        //转递数据给jsp
        model.addAttribute("ad1",JsonUtils.objectToJson(nodes));

        return "index";
    }
}
