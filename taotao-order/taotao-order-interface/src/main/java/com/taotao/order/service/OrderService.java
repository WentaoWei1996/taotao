package com.taotao.order.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;

/**
 * @Author: wwt
 * @Date: 2019/10/24 23:03
 */

public interface OrderService {

    /**
     * 创建订单
     * @param orderInfo
     * @return
     */
    public TaotaoResult createOrder(OrderInfo orderInfo);
}
