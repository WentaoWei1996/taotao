package com.taotao.test.jedis;

import com.taotao.content.jedis.JedisClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wwt
 * @date 2019/9/23 14:38
 */
public class JedisTest {
    @Autowired
    private JedisClient jedisClient;

    @Test
    public void testCart(){
        Jedis jedis = new Jedis("192.168.25.128",6379);
        System.out.println(jedis.hgetAll("TT_CART_REDIS:39"));
        System.out.println(jedis.hget("TT_CART_REDIS:39","1039296"));
//        jedis.del("TT_CART_REDIS:39");
    }

    //单机版
    @Test
    public void testJedis(){
        //创建jedis对象需要指定连接的地址和端口
        Jedis jedis = new Jedis("192.168.25.128",6379);
        //直接操作jedis
        jedis.set("key1234","value");
        System.out.println(jedis.get("key1234"));
        //关闭jedis
        jedis.close();
    }

    //连接池
    @Test
    public void testJedisPool(){
        //创建jedispool对象需要指定的端口和地址
        JedisPool pool = new JedisPool("192.168.25.128",6379);
        //获取jedis的对象
        Jedis jedis = pool.getResource();
        //直接操作redis
        jedis.set("keypool","keypool");
        System.out.println(jedis.get("keypool"));
        //关闭jedis（释放资源到连接池）
        jedis.close();
        //关闭连接池
        pool.close();
    }

    //测试集群
    @Test
    public void testJedisCluster(){
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.25.128",7001));
        nodes.add(new HostAndPort("192.168.25.128",7002));
        nodes.add(new HostAndPort("192.168.25.128",7003));
        nodes.add(new HostAndPort("192.168.25.128",7004));
        nodes.add(new HostAndPort("192.168.25.128",7005));
        nodes.add(new HostAndPort("192.168.25.128",7006));
        //创建jedisclustert对象
        JedisCluster cluster = new JedisCluster(nodes);
        //直接根据jediscluster对象操作redis集群
        cluster.set("keycluster","clustervalue");
        System.out.println("keycluster");
        //关闭jediscluster对象 封装了连接池
        cluster.close();
    }
}
