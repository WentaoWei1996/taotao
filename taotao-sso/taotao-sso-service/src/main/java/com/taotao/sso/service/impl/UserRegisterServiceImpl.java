package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserRegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: wwt
 * @Date: 2019/10/21 21:15
 */
@Service
public class UserRegisterServiceImpl implements UserRegisterService {

    @Autowired
    private TbUserMapper userMapper;

    @Override
    public TaotaoResult checkData(String param, int type) {
        //1 从tb_user中查数据
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //2 查询条件根据type动态生成
        if (type == 1){
            criteria.andUsernameEqualTo(param);
        } else if (type == 2){
            criteria.andPhoneEqualTo(param);
        } else if (type == 3){
            criteria.andEmailEqualTo(param);
        }
        List<TbUser> users = userMapper.selectByExample(example);
        //3 判断查询结果，如果查询到返回false,如果没查询到返回true
        if (users == null || users.size() == 0){
            return TaotaoResult.ok(true);
        }
        return TaotaoResult.ok(false);
    }

    @Override
    public TaotaoResult register(TbUser user) {
        //1 校验用户名和密码不能为空
        if (StringUtils.isEmpty(user.getUsername())){
            return TaotaoResult.build(400,"注册失败，请校验后再提交数据");
        }
        if (StringUtils.isEmpty(user.getPassword())){
            return TaotaoResult.build(400, "注册失败，请校验后再提交数据");
        }

        //2 校验数据可用性
        //2.1 校验用户名是否可用
        if (!(Boolean) checkData(user.getUsername(),1).getData()){
            return TaotaoResult.build(400,"用户名已经被注册");
        }
        //2.2 校验手机号码是否可用
        if (StringUtils.isNotBlank(user.getPhone())){
            if (!(Boolean) checkData(user.getPhone(),2).getData()){
                return TaotaoResult.build(400,"手机号码已经被注册");
            }
        }
        //2.3 校验email是否可用
        if (StringUtils.isNotBlank(user.getEmail())){
            if (!(Boolean) checkData(user.getEmail(),3).getData()){
                return TaotaoResult.build(400,"email已经被注册");
            }
        }

        //密码用MD5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        user.setCreated(new Date());
        user.setUpdated(user.getCreated());

        userMapper.insertSelective(user);

        return TaotaoResult.ok();
    }
}
