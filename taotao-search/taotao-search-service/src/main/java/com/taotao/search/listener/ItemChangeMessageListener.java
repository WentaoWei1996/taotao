package com.taotao.search.listener;

import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 接收消息的监听器
 * @author wwt
 * @date 2019/9/26 17:16
 */
public class ItemChangeMessageListener implements MessageListener {
    //注入service 直接调用方法更新即可
    @Autowired
    private SearchService service;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = null;
            Long itemId = null;
            //判断消息的类型是否为textMessage
            if (message instanceof TextMessage){
                textMessage = (TextMessage) message;
                //如果是获取商品的id
                itemId = Long.parseLong(textMessage.getText());
            }
            //通过商品的id查询数据 需要开发mapper 通过id查询商品搜索时的数据
            //更新索引库
            service.updateItemById(itemId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
