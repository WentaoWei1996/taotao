package com.taotao.cart.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

import java.util.List;

/**
 * @Author: wwt
 * @Date: 2019/10/22 17:52
 */
public interface CartService {

    /**
     * 根据商品的Id查询商品的信息
     * @param userId 哪个用户的购物车
     * @param item 商品
     * @param num 商品数量
     * @return
     */
    public TaotaoResult addItemCart(Long userId, TbItem item, Integer num);

    /**
     * @param userId
     * @return
     */
    public List<TbItem> queryCartListByUserId(Long userId);

    /**
     * 根据用户id和商品id判断商品是否在redis中
     * @param userId
     * @param itemId
     * @return
     */
    public TbItem queryTbItemByUserIdAndItemId(Long userId, Long itemId);

    /**
     *
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    public TaotaoResult updateCartItemByUserIdAndItemId(Long userId, Long itemId, Integer num);

    /**
     *
     * @param userId
     * @param itemId
     * @return
     */
    public TaotaoResult deleteByUserIdAndItemId(Long userId, Long itemId);
}
