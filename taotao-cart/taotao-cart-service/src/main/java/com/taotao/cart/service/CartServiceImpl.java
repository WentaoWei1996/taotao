package com.taotao.cart.service;

import com.taotao.cart.utils.jedis.JedisClient;
import com.taotao.cart.utils.json.JsonUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: wwt
 * @Date: 2019/10/22 17:57
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;

    @Override
    public TaotaoResult addItemCart(Long userId, TbItem item, Integer num) {

        TbItem itemRedis = queryTbItemByUserIdAndItemId(userId, item.getId());

        if (itemRedis == null){
            String image = item.getImage();
            if (StringUtils.isNotBlank(image)){
                item.setImage(image.split(",")[0]);
            }
            item.setNum(num);
            jedisClient.hset("TT_CART_REDIS" + ":" + userId, item.getId()+"", JsonUtils.objectToJson(item));
        }else {
            itemRedis.setNum(itemRedis.getNum() + num);
            jedisClient.hset("TT_CART_REDIS" + ":" + userId, item.getId()+"", JsonUtils.objectToJson(itemRedis));
        }

        return TaotaoResult.ok();
    }

    @Override
    public List<TbItem> queryCartListByUserId(Long userId) {
        Map<String, String> map = jedisClient.hgetAll("TT_CART_REDIS:" + userId + "");
        Set<Map.Entry<String, String>> set = map.entrySet();
        if (set != null){
            List<TbItem> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : set){
                TbItem item = JsonUtils.jsonToPojo(entry.getValue(), TbItem.class);
                list.add(item);
            }
            return list;
        }
        return null;
    }

    @Override
    public TbItem queryTbItemByUserIdAndItemId(Long userId, Long itemId) {
        String tbItemJson = jedisClient.hget("TT_CART_REDIS" + ":" + userId, itemId+"");
        if (StringUtils.isNotBlank(tbItemJson)){
            TbItem tbItem = JsonUtils.jsonToPojo(tbItemJson,TbItem.class);
            return tbItem;
        }
        return null;
    }

    @Override
    public TaotaoResult updateCartItemByUserIdAndItemId(Long userId, Long itemId, Integer num) {
       /* String tbItemJson = jedisClient.hget("TT_CART_REDIS" + ":" + userId, itemId+"");
        if (StringUtils.isNotBlank(tbItemJson)){
            TbItem tbItem = JsonUtils.jsonToPojo(tbItemJson, TbItem.class);
            tbItem.setNum(num);
            jedisClient.hset("TT_CART_REDIS" + ":" + userId, itemId+"", JsonUtils.objectToJson(tbItem));
        }*/
        TbItem item = queryTbItemByUserIdAndItemId(userId, itemId);
        if (item != null){
            item.setNum(num);
            jedisClient.hset("TT_CART_REDIS" + ":" + userId, itemId+"", JsonUtils.objectToJson(item));
            return TaotaoResult.ok();
        }
        return null;
    }

    @Override
    public TaotaoResult deleteByUserIdAndItemId(Long userId, Long itemId) {
        jedisClient.hdel("TT_CART_REDIS" + ":" + userId, itemId+"");
        return TaotaoResult.ok();
    }
}
