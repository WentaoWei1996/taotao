package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserLoginService;
import com.taotao.sso.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * @Author: wwt
 * @Date: 2019/10/21 23:54
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;

    @Override
    public TaotaoResult login(String username, String password) {

        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);

        List<TbUser> users = userMapper.selectByExample(example);

        //检测用户名
        if (users == null || users.size() == 0){
            return TaotaoResult.build(400, "用户名或密码错误");
        }
        //判断密码
        if (!users.get(0).getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
            return TaotaoResult.build(400, "用户名或密码错误");
        }

        //登陆成功后生成token，token相当于sessionid，可以用uuid生成
        String token = UUID.randomUUID().toString();

        //把用户登陆信息保存在redis中，密码为空，key为SESSION:token
        users.get(0).setPassword(null);
        jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(users.get(0)));

        //设置过期时间
        jedisClient.expire("SESSION:" + token, 1800);

        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        String json = jedisClient.get("SESSION:" + token);
        if (StringUtils.isBlank(json)){
            return TaotaoResult.build(400, "用户登陆信息过期，重新登陆");
        }
        jedisClient.expire("SESSION:" + token, 1800);

        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);

        return TaotaoResult.ok(user);
    }

    @Override
    public TaotaoResult quit(String token) {

        jedisClient.del("SESSION:" + token);
        return TaotaoResult.ok();
    }
}
