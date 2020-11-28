package com.taotao.content.service.Impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类
 * @author wwt
 * @date 2019/9/20 12:01
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        List<EasyUITreeNode> nodes = new ArrayList<>();
        for (TbContentCategory tbContentCategory : list){
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            node.setText(tbContentCategory.getName());
            nodes.add(node);
        }
        return nodes  ;
    }

    @Override
    public TaotaoResult createContentCategory(Long parentId, String name) {
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setParentId(parentId);
        contentCategory.setCreated(new Date());
        contentCategory.setIsParent(false);
        contentCategory.setName(name);
        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);
        contentCategory.setUpdated(contentCategory.getCreated());
        contentCategoryMapper.insertSelective(contentCategory);

        //判断如果要添加的节点的父节点为叶子节点，需要更新其为父节点
        TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (parentNode.getIsParent()==false){
            parentNode.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKeySelective(parentNode);
        }

        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public TaotaoResult updateContentCategory(Long id, String name) {
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        contentCategory.setName(name);
        contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContentCategory(Long id) {
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        if (!contentCategory.getIsParent()){
            contentCategoryMapper.deleteByPrimaryKey(id);
            TbContentCategoryExample example = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(contentCategory.getParentId());
            List<TbContentCategory> categoryList = contentCategoryMapper.selectByExample(example);
            if (categoryList.size()==0){
                TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
                category.setIsParent(false);
                contentCategoryMapper.updateByPrimaryKeySelective(category);
            }
        }
        return TaotaoResult.ok();
    }
}