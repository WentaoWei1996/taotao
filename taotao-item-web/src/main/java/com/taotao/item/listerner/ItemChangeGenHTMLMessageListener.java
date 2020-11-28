package com.taotao.item.listerner;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wwt
 * @Date: 2019/10/20 16:56
 */
public class ItemChangeGenHTMLMessageListener implements MessageListener {

    @Autowired
    private FreeMarkerConfigurer configurer;
    @Autowired
    private ItemService itemService;

    @Override
    public void onMessage(Message message){
        //1 接受消息
        if (message instanceof TextMessage){
            TextMessage message2 = (TextMessage) message;
            try {
                String text = message2.getText();
                if (StringUtils.isNotBlank(text)){
                    //2 通过接收到的消息转换成商品id，查询商品的信息
                    Long itemId = Long.valueOf(text);
                    TbItem tbItem = itemService.getItemById(itemId);
                    System.out.println(tbItem.getId());
                    Item item = new Item(tbItem);
                    TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);

                    //3 生成静态页面
                    this.genHtml("item.ftl",item,tbItemDesc);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void genHtml(String templateName, TbItem item, TbItemDesc itemDesc) throws Exception{
        Configuration configuration = configurer.getConfiguration();
        Template template = configuration.getTemplate(templateName);
        Map map = new HashMap();
        map.put("item",item);
        map.put("itemDesc",itemDesc);
        Writer writer = new FileWriter(new File("/Users/weiwentao/文档/freemarker/temp/" + item.getId() + ".html"));
        template.process(map, writer);
        writer.close();
    }
}
