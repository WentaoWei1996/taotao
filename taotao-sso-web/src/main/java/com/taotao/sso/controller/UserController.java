package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.util.CookieUtils;
import com.taotao.sso.service.UserLoginService;
import com.taotao.sso.service.UserRegisterService;
import com.taotao.sso.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: wwt
 * @Date: 2019/10/21 21:26
 */

@Controller
public class UserController {

    @Autowired
    private UserRegisterService userRegisterService;
    @Autowired
    private UserLoginService userLoginService;

    @RequestMapping(value = "/user/check/{param}/{type}",method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult checkData(@PathVariable String param, @PathVariable int type){
        return userRegisterService.checkData(param, type);
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser user){
        return userRegisterService.register(user);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String username, String password, HttpServletRequest request, HttpServletResponse response){
        TaotaoResult result = userLoginService.login(username, password);
        String token = result.getData().toString();

        CookieUtils.setCookie(request, response, "TT_TOKEN",token);
        return result;
    }

    @RequestMapping(value = "/user/token/{token}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String getUserByToken(@PathVariable String token, String callback){
        TaotaoResult result = userLoginService.getUserByToken(token);

        if (StringUtils.isNotBlank(callback)){
            String strResult = callback + "(" + JsonUtils.objectToJson(result) + ");";
            return strResult;
        }

        return JsonUtils.objectToJson(result);
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    @ResponseBody
    public String quit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        userLoginService.quit(token);
        response.sendRedirect("http://localhost:8082/");
        return "";
    }
}
