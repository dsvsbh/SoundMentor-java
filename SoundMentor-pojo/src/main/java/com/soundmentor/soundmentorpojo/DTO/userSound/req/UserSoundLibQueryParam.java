package com.soundmentor.soundmentorpojo.DTO.userSound.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserSoundLibQueryParam implements Serializable {
    /**
     * 查询类型，0查询用户自训练声音，1查询系统预设声音，2查询所有
     **/
    @NotNull(message = "查询类型不能为空")
    private Integer type;

    /**
     * 当前页码
     **/
    @NotNull(message = "当前页码不能为空")
    private Integer current;

    /**
     * 每页条数
     **/
    @NotNull(message = "每页条数不能为空")
    private Integer size;

    /**
     * 用户id
     **/
    private Integer userId;

    /**
     * 状态：0 创建 1 执行中 2 成功 3 失败
     **/
    private  Integer status;
    /**
     * 声音名称
     **/
    private String soundName;
}
