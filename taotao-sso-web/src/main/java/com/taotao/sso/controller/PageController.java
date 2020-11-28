package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: wwt
 * @Date: 2019/10/22 10:21
 */

@Controller
public class PageController {

    @RequestMapping(value = "/page/{page}", method = RequestMethod.GET) ///page/login?redirect=localhost:8092/order/order-cart.html
    public String showPage(@PathVariable String page, String redirect, Model model){
        model.addAttribute("redirect", redirect);
        return page;
    }
}
