package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 内容分类处理
 * @author wwt
 * @date 2019/9/20 13:50
 */
@Controller
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;
    /**
     * url : '/content/category/list',
     * animate: true,
     * method : "GET",
     */
    @RequestMapping(value = "/content/category/list",method = RequestMethod.GET)
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value = "id", defaultValue = "0") Long parentId){
        return contentCategoryService.getContentCategoryList(parentId);
    }

    /**
     * 添加节点
     * /content/category/create
     * post
     * parentId:node.parentId,name:node.text
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping(value = "/content/category/create", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult creatContentCategory(Long parentId,String name){
        return contentCategoryService.createContentCategory(parentId,name);
    }

    /**
     * 更新name
     * @param id
     * @param name
     * @return
     */
    @RequestMapping(value = "/content/category/update", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateContentCategory(Long id, String name){
        return contentCategoryService.updateContentCategory(id,name);
    }

    @RequestMapping(value = "/content/category/delete", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContentCategory(Long id){
        return contentCategoryService.deleteContentCategory(id);
    }
}
