package com.atguigu.spzx.manager.service;


import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.vo.system.LoginVo;
import org.springframework.stereotype.Service;


public interface  SysUserService {

    //用户登录
    LoginVo login(LoginDto loginDto);
}
