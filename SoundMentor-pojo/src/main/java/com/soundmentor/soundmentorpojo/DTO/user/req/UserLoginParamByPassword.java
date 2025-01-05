package com.soundmentor.soundmentorpojo.DTO.user.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description: 用户登录参数
 * @Author: Make
 * @DATE: 2025/01/05
 **/
@Data
public class UserLoginParamByPassword implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     **/
    @NotNull(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     **/
    @NotNull(message = "密码不能为空")
    private String password;
}
