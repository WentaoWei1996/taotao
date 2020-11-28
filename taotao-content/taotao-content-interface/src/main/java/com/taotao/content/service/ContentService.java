package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * 内容处理的接口
 * @author wwt
 * @date 2019/9/20 16:44
 */
public interface ContentService {
    /**
     * 得到内容list
     * @param id
     * @param page
     * @param rows
     * @return
     */
    public EasyUIDataGridResult getContentList(Long id, int page, int rows);

    /**
     * 插入内容表
     * @param content
     * @return
     */
    public TaotaoResult saveContent(TbContent content);

    /**
     * 编辑
     * @param content
     * @return
     */
    public TaotaoResult editContent(TbContent content);

    /**
     * 删除
     * @param id
     * @return
     */
    public TaotaoResult deleteContent(List<Long> id);

    /**
     * 根据分类id查询内容列表
     * @param categoryId
     * @return
     */
    public List<TbContent> getContentListByCatId(Long categoryId);
}
