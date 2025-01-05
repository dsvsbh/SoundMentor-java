package com.soundmentor.soundmentorpojo.DTO.user.res;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户token
     */
    private String token;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户名/账号
     */
    private String username;


    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
