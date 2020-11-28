package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wwt
 * @date 2019/9/20 16:48
 */
@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/content/query/list",method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGridResult getContentList(Long categoryId, int page, int rows){
        EasyUIDataGridResult result = contentService.getContentList(categoryId,page,rows);
        return result;
    }

    //$.post("/content/save",$("#contentAddForm").serialize(), function(data){
    @RequestMapping(value = "/content/save", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult saveContent(TbContent content){
        return contentService.saveContent(content);
    }

    //$.post("/content/edit",$("#contentEditForm").serialize(), function(data)
    @RequestMapping(value = "/content/edit" ,method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult editContent(TbContent content){
        return contentService.editContent(content);
    }

    //$.post("/content/delete",params, function(data)
    @RequestMapping(value = "/content/delete" ,method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContent(@RequestParam("ids") String ids){
        String[] id = ids.split(",");
        List<Long> idss = new ArrayList<>();
        for (int i = 0; i < id.length; i++) {
            idss.add(Long.parseLong(id[i]));
        }
        return contentService.deleteContent(idss);
    }
}
