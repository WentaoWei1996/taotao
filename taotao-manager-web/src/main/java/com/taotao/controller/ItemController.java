package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParam;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wwt
 * @date 2019/9/18 14:48
 */

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/item/list",method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGridResult getItemList(int page, int rows){
        EasyUIDataGridResult result = itemService.getItemList(page,rows);
        return result;
    }

    @RequestMapping("/item/save")
    @ResponseBody
    public TaotaoResult saveItem(TbItem item,String desc){
        TaotaoResult result = itemService.saveItem(item,desc);

        return result;
    }

    @RequestMapping("/page/item-edit")
    @ResponseBody
    public TbItem loadItem(Long id){
        TbItem result = itemService.getItemById(id);

        return result;
    }

    @RequestMapping("/item/desc")
    @ResponseBody
    public TbItemDesc getItemDesc(Long id){
        TbItemDesc itemDesc = itemService.getItemDescById(id);

        return itemDesc;
    }

    @RequestMapping("/item/param")
    @ResponseBody
    public TbItemParam getItemParam(Long id){
        TbItemParam itemParam = itemService.getItemParam(id);

        return itemParam;
    }

    @RequestMapping("/item/update")
    @ResponseBody
    public TaotaoResult updateItem(TbItem item,String desc){
        TaotaoResult result = itemService.updateItem(item,desc);

        return result;
    }

    @RequestMapping("/item/delete")
    @ResponseBody
    public TaotaoResult deleteItem(Long ids){
        TaotaoResult result = itemService.deleteItem(ids);
        return result;
    }

    @RequestMapping("/item/instock")
    @ResponseBody
    public TaotaoResult instockItem(Long ids){
        TaotaoResult result = itemService.instockItem(ids);
        return result;
    }

    @RequestMapping("/item/reshelf")
    @ResponseBody
    public TaotaoResult reshelfItem(Long ids){
        TaotaoResult result = itemService.reshelfItem(ids);
        return result;
    }
}
