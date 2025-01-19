package com.soundmentor.soundmentorpojo.DTO.userSound.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserSoundLibQueryParam implements Serializable {
    /**
     * 查询类型，0查询系统预设声音，1查询用户自训练声音，2查询所有
     **/
    @NotNull(message = "查询类型不能为空")
    private Integer type;
}
