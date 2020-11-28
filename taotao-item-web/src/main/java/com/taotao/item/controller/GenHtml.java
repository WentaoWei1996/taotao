package com.taotao.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wwt
 * @Date: 2019/10/20 16:27
 */
@Controller
public class GenHtml {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/genhtml")
    @ResponseBody
    public String genHtml() throws Exception{
        //1 从spring中获取FreeMarkerConfigurer对象
        //2 从FreeMarkerConfigurer对象中获取Configuration对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //3 使用Configurer对象获取Template对象
        Template template = configuration.getTemplate("hello.ftl");
        //4 创建数据集
        Map data = new HashMap();
        data.put("hello","1000");
        //5 创建输出文件的Writer对象
        Writer writer = new FileWriter(new File("/Users/weiwentao/文档/freemarker/temp/spring-freemarker.html"));
        //6 调用模板对象的process方法，生成文件
        template.process(data,writer);
        //7 关闭流
        writer.close();
        return "ok";
    }
}
