package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * @author wwt
 * @date 2019/9/19 11:29
 */
public interface ItemCatService {
    /**
     * 根据父节点的id 查询该节点的子类目列表
     * @param parentId
     * @return
     */
    public List<EasyUITreeNode> getItemCatList(Long parentId);
}
