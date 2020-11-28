package com.taotao.cart.controller;

import com.taotao.cart.service.CartService;
import com.taotao.cart.utils.CookieUtils;
import com.taotao.cart.utils.JsonUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.ItemService;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wwt
 * @Date: 2019/10/22 20:38
 */
@Controller
public class CartController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/cart/add/{itemId}", method = RequestMethod.GET)
    public String addCart(@PathVariable Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        TaotaoResult result = userLoginService.getUserByToken(token);
        if (result.getStatus() == 200){
            TbItem tbItem = itemService.getItemById(itemId);
            TbUser user = (TbUser) result.getData();

            System.out.println(user.getId());
            System.out.println(tbItem);
            System.out.println(num);

            cartService.addItemCart(user.getId(), tbItem, num);
        }else {
            //用户未登陆 存放于cookie中
            addCookieCartItem(itemId, num, request,response);
        }
        return "cartSuccess";
    }

    @RequestMapping(value = "/cart/cart", method = RequestMethod.GET)
    public String showCart(HttpServletRequest request, HttpServletResponse response){
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        TaotaoResult result = userLoginService.getUserByToken(token);
        if (result.getStatus() == 200){
            TbUser user = (TbUser) result.getData();

            /*//同步cookie到redis中
            List<TbItem> cookieCartList = getCookieCartList(request);
            for (TbItem item : cookieCartList) {
                cartService.updateCartItemByUserIdAndItemId(user.getId(), item.getId(), item.getNum());
                deleteCookieCartItem(item.getId(), request, response);
            }*/

            List<TbItem> itemList = cartService.queryCartListByUserId(user.getId());

            System.out.println(itemList);

            request.setAttribute("cartList", itemList);
        }else {
            //用户未登陆
            List<TbItem> cartList = getCookieCartList(request);
            request.setAttribute("cartList", cartList);
        }
        return "cart";
    }

    @RequestMapping(value = "/cart/update/num/{itemId}/{num}", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response){
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        TaotaoResult result = userLoginService.getUserByToken(token);
        if (result.getStatus() == 200){
            TbUser user = (TbUser) result.getData();
            cartService.updateCartItemByUserIdAndItemId(user.getId(), itemId, num);
            return TaotaoResult.ok();
        }else {
            //没有登陆
            updateCookieCartItem(itemId, num, request, response);
            return TaotaoResult.ok();
        }
    }

    @RequestMapping(value = "/cart/delete/{itemId}", method = RequestMethod.GET)
    @ResponseBody
    public String  deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        TaotaoResult result = userLoginService.getUserByToken(token);
        if (result.getStatus() == 200){
            TbUser user = (TbUser) result.getData();
            cartService.deleteByUserIdAndItemId(user.getId(), itemId);
        }else {
            //没有登陆
            deleteCookieCartItem(itemId, request, response);
        }

        response.sendRedirect("/cart/cart.html");

        return "";
    }

    //cookie中操作购物车的方法

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

    /**
     * 使用cookie添加购物车
     * @param itemId
     * @param num
     * @param request
     * @param response
     */
    private void addCookieCartItem(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
        List<TbItem> cartItemList = getCookieCartList(request);

        //判断商品是否在cookie中
        boolean flag = false;
        for (int i = 0; i < cartItemList.size(); i++) {
            if (cartItemList.get(i).getId() == itemId.longValue()){
                Integer Num = cartItemList.get(i).getNum();
                cartItemList.get(i).setNum(Num + num);
                flag = true;
                break;
            }
        }

        if (flag){
            //如果在，更新数量
            CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartItemList), 604800, true);
        }else {
            //如果不在，查找商品信息，添加到cookie中
            TbItem tbItem = itemService.getItemById(itemId);
            tbItem.setNum(num);
            if (StringUtils.isNotBlank(tbItem.getImage())){
                tbItem.setImage(tbItem.getImage().split(",")[0]);
            }
            cartItemList.add(tbItem);
            CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartItemList), 604800, true);
        }
    }

    /**
     *  在cookie中更新购物车
     * @param itemId
     * @param num
     * @param request
     * @param response
     */
    private void updateCookieCartItem(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
        List<TbItem> cartItemList = getCookieCartList(request);
        //判断商品是否在cookie中
        boolean flag = false;
        for (int i = 0; i < cartItemList.size(); i++) {
            if (cartItemList.get(i).getId() == itemId.longValue()){
                cartItemList.get(i).setNum(num);
                flag = true;
                break;
            }
        }
        if (flag){
            CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartItemList), 604800, true);
        }
    }

    /**
     * 在cookie中删除商品
     * @param itemId
     * @param request
     * @param response
     */
    private void deleteCookieCartItem(Long itemId, HttpServletRequest request, HttpServletResponse response){
        List<TbItem> cartItemList = getCookieCartList(request);

        boolean flag = false;
        for (int i = 0; i < cartItemList.size(); i++) {
            if (cartItemList.get(i).getId() == itemId.longValue()){
                cartItemList.remove(cartItemList.get(i));
                flag = true;
                break;
            }
        }

        if (flag){
            CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartItemList));
        }
    }
}
