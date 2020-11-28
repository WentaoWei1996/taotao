package com.taotao.order.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.jedis.JedisClient;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: wwt
 * @Date: 2019/10/24 23:07
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;


    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {
        //1 接收表单的数据
        //2 生成订单id
        if (!jedisClient.exists("ORDER_GEN_KEY")){
            //设置初始值
            jedisClient.set("ORDER_GEN_KEY", 100544+"");
        }

        String orderId = jedisClient.incr("ORDER_GEN_KEY").toString();
        orderInfo.setOrderId(orderId);
        orderInfo.setPostFee("0");
        //1 未付款 2 已付款 3 未发货 4 已发货 5 交易成功 6 交易关闭
        orderInfo.setStatus(1);
        Date date = new Date();
        orderInfo.setCreateTime(date);
        orderInfo.setUpdateTime(date);
        //3 向订单表插入数据
        orderMapper.insert(orderInfo);

        //4 向订单明细表中插入数据
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem tbOrderItem : orderItems) {
            //生成明细id
            Long orderItemId = jedisClient.incr("GEN_ORDER_ITEM_ID_KEY");
            tbOrderItem.setId(orderItemId.toString());
            tbOrderItem.setOrderId(orderId);
            //插入数据
            orderItemMapper.insert(tbOrderItem);
        }

        //5 向订单物流表中插入数据
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        orderShippingMapper.insert(orderShipping);

        /*//6 删除购物车中的items
        for(TbOrderItem item : orderItems){
            cartService.deleteByUserIdAndItemId(orderInfo.getUserId(), Long.parseLong(item.getItemId()));
        }*/

        //7 返回TaotaoResult
        return TaotaoResult.ok(orderId);
    }
}
