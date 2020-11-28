package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;

/**
 * @Author: wwt
 * @Date: 2019/10/21 23:53
 */
public interface UserLoginService {

    /**
     * @param username
     * @param password
     * @return
     */
    public TaotaoResult login(String username, String password);

    /**
     * 根据token得到用户信息
     * @param token
     * @return
     */
    public TaotaoResult getUserByToken(String token);

    /**
     * 在redis中根据token删除对应的key
     * @param token
     * @return
     */
    public TaotaoResult quit(String token);
}
