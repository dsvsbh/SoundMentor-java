package com.soundmentor.soundmentorpojo.DTO.user.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ForgetPasswordParam implements Serializable {
    /**
     * 邮箱
     **/
    @NotNull(message = "邮箱不能为空")
    private String email;
    /**
     * 验证码
     **/
    @NotNull(message = "验证码不能为空")
    private Integer verifyCode;
    /**
     * 密码
     **/
    @NotNull(message = "密码不能为空")
    private String password;
}
