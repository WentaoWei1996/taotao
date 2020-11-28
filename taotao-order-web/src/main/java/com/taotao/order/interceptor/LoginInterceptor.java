package com.taotao.order.interceptor;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.utils.utils.CookieUtils;
import com.taotao.sso.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: wwt
 * @Date: 2019/10/24 21:37
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserLoginService userLoginService;


    /**
     * 这里进行用户身份的认证，在进入目标方法之前执行该方法
     * 如果返回false表示拦截，不让访问，如果是true，表示放行
     * @param request
     * @param response
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //1 通过sso服务获取用户的信息
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        TaotaoResult result = userLoginService.getUserByToken(token);

        if (result.getData() == null){
            //2 用户不存在，重定向到sso的登陆页面    //http://localhost:8088/page/login?redirect=localhost:8092/order/order-cart.html
            response.sendRedirect("http://localhost:8088/page/login?redirect=" + request.getRequestURI());
        }else {
            //3 用户存在，放行
            //通过token获取用户信息设置到request域中
            request.setAttribute("USER-INFO",result.getData());
            return true;
        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
