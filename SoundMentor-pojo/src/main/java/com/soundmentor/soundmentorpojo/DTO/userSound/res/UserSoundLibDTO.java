package com.soundmentor.soundmentorpojo.DTO.userSound.res;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSoundLibDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * code
     **/
    public  String code;
    /**
     * 声音名
     **/
    public  String name;
    /**
     * 传给python的参数
     * 如果是用户训练声音，则参数为userTrain
     **/
    public  String paramName;
    /**
     * 声音样例地址
     **/
    public  String SoundUrl;
}
