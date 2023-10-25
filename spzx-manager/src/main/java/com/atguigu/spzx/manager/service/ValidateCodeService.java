package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.vo.system.ValidateCodeVo;

public interface ValidateCodeService {
    // 图片验证码生成
    public ValidateCodeVo generateValidateCode();
}
