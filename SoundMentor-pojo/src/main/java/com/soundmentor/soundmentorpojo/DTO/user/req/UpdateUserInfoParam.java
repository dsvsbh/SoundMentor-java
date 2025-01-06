package com.soundmentor.soundmentorpojo.DTO.user.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class UpdateUserInfoParam implements Serializable {
    /**
     * 用户名称
     */
    private String name;


    /**
     * 用户手机号
     */
    private String phone;


    /**
     * 头像路径
     */
    private String headImg;
}
