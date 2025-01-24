package com.soundmentor.soundmentorpojo.DTO.task;

import java.io.Serializable;

/**
 * 文本转语音通用参数
 * @Author: Make
 * @DATE: 2025/01/20
 **/
public class Text2VoiceCommonParam implements Serializable {
    /**
     * 声音名
     **/
    private String name;
    /**
     * 音量大小：0-100
     **/
    private Integer volume;
    /**
     * 语速：正常是100
     **/
    private Integer rate;
    /**
     * 语调：-100~100
     **/
    private Integer pitch;
}
