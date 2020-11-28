package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParam;

/**
 * 商品相关的处理的service接口
 * @author wwt
 * @date 2019/9/17 16:59
 */
public interface ItemService {
    /**
     * page row 分页查询
     * @param page
     * @param rows
     * @return
     */
    public EasyUIDataGridResult getItemList(int page, int rows);
    /**
     * 根据商品的基础数据和商品的描述信息插入商品（插入商品基础表和商品描述表）
     * @param item
     * @param desc
     * @return
     */
    public TaotaoResult saveItem(TbItem item, String desc);

    /**
     * 更新
     * @param item
     * @param desc
     * @return
     */
    public TaotaoResult updateItem(TbItem item, String desc);

    /**
     * 删除
     * @param itemId
     * @return
     */
    public TaotaoResult deleteItem(Long itemId);

    /**
     * 下架
     * @param ids
     * @return
     */
    public TaotaoResult instockItem(Long ids);

    /**
     * 下架
     * @param ids
     * @return
     */
    public TaotaoResult reshelfItem(Long ids);


    public TbItem getItemById(Long itemId);


    public TbItemDesc getItemDescById(Long itemId);

    public TbItemParam getItemParam(Long id);
}
