package com.soundmentor.soundmentorpojo.DTO.userSound.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserFavoriteQueryParam implements Serializable {
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

}
