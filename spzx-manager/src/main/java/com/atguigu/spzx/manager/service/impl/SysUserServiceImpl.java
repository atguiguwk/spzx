package com.atguigu.spzx.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //用户登录
    @Override
    public LoginVo login(LoginDto loginDto) {
        //1.获取提交用户名
        String userName = loginDto.getUserName();
        //2.根据用户名查询用户表sys_user表
        SysUser sysUser = sysUserMapper.selectByUserName(userName);
        //3.如果查不到，返回错误信息
        if (sysUser == null){
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }
        //4.如果能查到，用户存在
        //5.获取输入密码，比较密码
        String input_Password = loginDto.getPassword();
        String database_Password = sysUser.getPassword();
        //把输入的密码加密，在进行比较
        input_Password = DigestUtils.md5DigestAsHex(input_Password.getBytes());
        if (!input_Password.equals(database_Password)){
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }
        //6.如果一致，成功，否则失败
        //7.登录成功，生成唯一标识token
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        //8.把登录成功后的信息放到redis里面
        redisTemplate.opsForValue().set("user:login"+token,
                JSON.toJSONString(sysUser),
                7,
                TimeUnit.DAYS);

        //9.返回token
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }
}
