package com.soundmentor.soundmentorpojo.DTO.user.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AddUserParam implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名称
     */
    @NotNull(message = "用户名称不能为空")
    private String name;

    /**
     * 用户邮箱
     */
    @NotNull(message = "用户邮箱不能为空")
    private String email;

    /**
     * 验证码
     **/
    @NotNull(message = "验证码不能为空")
    private Integer code;

    /**
     * 用户手机号
     */
    @NotNull(message = "用户手机号不能为空")
    private String phone;

    /**
     * 用户名/账号
     */
    @NotNull(message = "用户名/账号不能为空")
    private String username;

    /**
     * 用户密码
     */
    @NotNull(message = "用户密码不能为空")
    private String password;
}
