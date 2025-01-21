package com.soundmentor.soundmentorpojo.DTO.userSound.res;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSoundLibDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 声音名
     **/
    private  String soundName;

    /**
     * 声音类型:0 用户训练声音，1 系统声音
     **/
    private Integer type;
    /**
     * 状态：0 创建 1 执行中 2 成功 3 失败
     **/
    private Integer status;
    /**
     * 传给python的参数
     * 如果是用户训练声音，则参数为self-defined
     **/
    private  String apiParam;
    /**
     * 声音样例地址
     **/
    private  String SoundUrl;
    /**
     * 声音描述
     **/
    private  String description;
    /**
     * 是否收藏
     **/
    private Boolean isFavorite;

}
