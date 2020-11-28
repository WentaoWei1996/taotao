package com.taotao.order.controller;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.order.utils.utils.CookieUtils;
import com.taotao.order.utils.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wwt
 * @Date: 2019/10/24 20:49
 */

@Controller
public class OrderController {

    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    /**
     * 展示购物车中的商品列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/order/order-cart")
    public String showOrderCart(HttpServletRequest request, HttpServletResponse response){
        //1 获取token，调用sso服务获取用户信息
//        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
//        TbUser user = null;
//        if (StringUtils.isNotBlank(token)){
//            TaotaoResult result = userLoginService.getUserByToken(token);
//            if (result.getStatus() == 200){
//                user = (TbUser) result.getData();
//            }
//        }

        TbUser user = (TbUser) request.getAttribute("USER-INFO");

        //2 展示用户的地址列表数据：根据用户信息中的用户id查询，这里暂时使用静态数据
        //3 展示支付的方式列表数据：从数据库中查询支付的方式列表，这里暂时使用静态数据

        //4 从cookie中获取购物车的商品列表
        List<TbItem> cookieCartList = getCookieCartList(request);

        //5 从redis中获取该用户的购物车的商品列表
        List<TbItem> redisCartList = cartService.queryCartListByUserId(user.getId());
//        List<TbItem> redisCartList = cartService.queryCartListByUserId(user == null ? -1 : user.getId());

        //6 合并数据到redis中
        for (TbItem cookieItem : cookieCartList){
            boolean flag = false;
            if (redisCartList != null){
                for (TbItem redisItem : redisCartList){
                    if (cookieItem.getId() == redisItem.getId()){
                        redisItem.setNum(redisItem.getNum() + cookieItem.getNum());
                        cartService.updateCartItemByUserIdAndItemId(user.getId(), redisItem.getId(), redisItem.getNum());
                        flag = true;
                    }
                }
            }
            if (flag == false){
                cartService.addItemCart(user.getId(), cookieItem, cookieItem.getNum());
            }
        }

        //7 合并数据后删除cookie
        if (!cookieCartList.isEmpty()){
            CookieUtils.deleteCookie(request, response, "TT_CART");
        }

        request.setAttribute("cartList", redisCartList);

        return "order-cart";
    }

    /**
     * 创建订单
     * @param orderInfo
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public String creatOrder(OrderInfo orderInfo, HttpServletRequest request){

        //1 接收表单提交的数据orderinfo
        //2 补全用户信息
        TbUser user = (TbUser) request.getAttribute("USER-INFO");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());

        //3 调用service创建订单
        TaotaoResult result = orderService.createOrder(orderInfo);

        //4 清空购物车
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for(TbOrderItem item : orderItems){
            cartService.deleteByUserIdAndItemId(orderInfo.getUserId(), Long.parseLong(item.getItemId()));
        }

        //取订单号
        String orderId = result.getData().toString();
        request.setAttribute("orderId", orderId);
        request.setAttribute("payment",orderInfo.getPayment());

        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(3);
        request.setAttribute("date", dateTime.toString("yyyy-MM-dd"));

        //4 返回逻辑视图展示成功页面
        return "success";
    }

    /**
     * 从cookie中获取商品列表
     * @param request
     * @return
     */
    private List<TbItem> getCookieCartList(HttpServletRequest request){
        String jsonString = CookieUtils.getCookieValue(request, "TT_CART", true);

        List<TbItem> list = new ArrayList<>();
        if (StringUtils.isNotBlank(jsonString)){
            list = JsonUtils.jsonToList(jsonString,TbItem.class);
        }
        return list;
    }
}
