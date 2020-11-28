package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.IDUtils;
import com.taotao.manager.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import com.taotao.service.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

/**
 * @author wwt
 * @date 2019/9/18 14:09
 */

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private TbItemParamMapper itemParamMapper;

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "topicDestination")
    private Destination destination;

    private String ITEM_INFO_KEY = "ITEM_INFO";
    private Integer ITEM_INFO_KEY_EXPIRE = 28800;


    @Override
    public EasyUIDataGridResult  getItemList(int page, int rows) {
        if (page==0)page=1;
        if (rows==0)rows=30;
        PageHelper.startPage(page,rows);

        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);

        PageInfo<TbItem> pageInfo = new PageInfo<>(list);

        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(pageInfo.getList());

        return result;
    }

    @Override
    public TaotaoResult saveItem(TbItem item, String desc) {
        final long itemId = IDUtils.genItemId();
        item.setId(itemId);
        //商品状态，1-正常，2-下架，3-删除
        item.setStatus((byte)1);
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);

        itemMapper.insert(item);

        TbItemDesc itemDesc = new TbItemDesc();

        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);

//      向商品描述表插入数据
        itemDescMapper.insert(itemDesc);

        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(itemId+"");
            }
        });

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateItem(TbItem item, String desc) {
        Date date = new Date();
        item.setUpdated(date);

        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();

        criteria.andIdEqualTo(item.getId());
        itemMapper.updateByExample(item,example);

        TbItemDesc itemDesc = new TbItemDesc();

        TbItemDescExample example1 = new TbItemDescExample();
        TbItemDescExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andItemIdEqualTo(item.getId());

        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);

        itemDescMapper.updateByExample(itemDesc,example1);

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteItem(Long ids) {
        TbItem item = itemMapper.selectByPrimaryKey(ids);
        item.setStatus((byte) 3);

        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(ids);

        itemMapper.updateByExample(item,example);

        return TaotaoResult.ok();
      /*  itemMapper.deleteByPrimaryKey(itemId);
        return TaotaoResult.ok();*/
    }

    @Override
    public TaotaoResult instockItem(Long ids) {

        TbItem item = itemMapper.selectByPrimaryKey(ids);
        item.setStatus((byte) 2);

        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(ids);

        itemMapper.updateByExample(item,example);

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult reshelfItem(Long ids) {
        TbItem item = itemMapper.selectByPrimaryKey(ids);
        item.setStatus((byte) 1);

        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(ids);

        itemMapper.updateByExample(item,example);

        return TaotaoResult.ok();
    }

    @Override
    public TbItem getItemById(Long itemId) {

        try {
            if (itemId!=null){
                String jsonString = jedisClient.get(ITEM_INFO_KEY + ":" + itemId + ":BASE");
                if (StringUtils.isNotBlank(jsonString)){
                    System.out.println("有缓存");
                    jedisClient.expire(ITEM_INFO_KEY + ":" + itemId + ":BASE",ITEM_INFO_KEY_EXPIRE);
                    return JsonUtils.jsonToPojo(jsonString,TbItem.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbItem item = itemMapper.selectByPrimaryKey(itemId);

        try {
            if (itemId != null){
                jedisClient.set(ITEM_INFO_KEY + ":" + itemId + ":BASE",JsonUtils.objectToJson(item));
                jedisClient.expire(ITEM_INFO_KEY + ":" + itemId + ":BASE",ITEM_INFO_KEY_EXPIRE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return item;
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {

        try {
            if (itemId!=null){
                String jsonString = jedisClient.get(ITEM_INFO_KEY + ":" + itemId + ":DESC");
                if (StringUtils.isNotBlank(jsonString)){
                    jedisClient.expire(ITEM_INFO_KEY + ":" + itemId + ":DESC",ITEM_INFO_KEY_EXPIRE);
                    return JsonUtils.jsonToPojo(jsonString,TbItemDesc.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);

        try {
            if (itemId != null){
                jedisClient.set(ITEM_INFO_KEY + ":" + itemId + ":DESC",JsonUtils.objectToJson(itemDesc));
                jedisClient.expire(ITEM_INFO_KEY + ":" + itemId + ":DESC",ITEM_INFO_KEY_EXPIRE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemDesc;
    }

    @Override
    public TbItemParam getItemParam(Long id) {
        TbItemParam itemParam = itemParamMapper.selectByPrimaryKey(id);
        return itemParam;
    }
}
