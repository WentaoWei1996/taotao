package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

/**
 * @Author: wwt
 * @Date: 2019/10/21 21:13
 */
public interface UserRegisterService {

    /**
     * @param param
     * @param type  1 2 3分别代表username phone email
     * @return
     */
    public TaotaoResult checkData(String param, int type);

    public TaotaoResult register(TbUser user);
}
