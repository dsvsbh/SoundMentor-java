package com.soundmentor.soundmentorpojo.DTO.user.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: Make
 * @DATE: 2025/01/06
 **/
@Data
public class UpdateUserPasswordParam implements Serializable {
    /**
     * 旧密码
     **/
    @NotNull(message = "旧密码不能为空")
    private String oldPassword;
    /**
     * 新密码
     **/
    @NotNull(message = "新密码不能为空")
    private String newPassword;
}
