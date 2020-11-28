package com.taotao.test.jedis;

import com.taotao.content.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wwt
 * @date 2019/9/23 15:22
 */
public class TestJedisClient {
    @Test
    public void testDanji(){
        //初始化spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        //获取实现类实例
        JedisClient bean = context.getBean(JedisClient.class);
        //调用方法操作
        bean.set("ksdfkj1","fsa;ksdaf");
        System.out.println(bean.get("ksdfkj1"));
    }
}
