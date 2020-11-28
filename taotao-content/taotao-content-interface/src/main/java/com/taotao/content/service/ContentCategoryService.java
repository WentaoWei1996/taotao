package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

/**
 * @author wwt
 * @date 2019/9/20 11:58
 */
public interface ContentCategoryService {
    //通过节点的id查询该节点的子节点列表
    public List<EasyUITreeNode> getContentCategoryList(Long parentId);

    /**
     * 添加内容分类
     * @param parentId 父节点的id
     * @param name 新增节点的内容
     * @return
     */
    public TaotaoResult createContentCategory(Long parentId, String name);

    /**
     * 更新节点name
     * @param id
     * @param name
     * @return
     */
    public TaotaoResult updateContentCategory(Long id, String name);

    /**
     * 删除节点
     * @param id
     * @return
     */
    public TaotaoResult deleteContentCategory(Long id);

}
