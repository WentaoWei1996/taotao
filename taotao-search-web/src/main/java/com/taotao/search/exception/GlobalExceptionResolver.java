package com.taotao.search.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器的类
 * @author wwt
 * @date 2019/9/26 11:18
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object hanlder, Exception ex) {
        //1 日志文件
        System.out.println(ex.getMessage());
        ex.printStackTrace();
        //2 通知开发人员
        System.out.println("发短信");
        //3 友好提示界面
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("error/exception");
        modelAndView.addObject("message","您的网络有异常，请重试");

        return modelAndView;
    }
}
