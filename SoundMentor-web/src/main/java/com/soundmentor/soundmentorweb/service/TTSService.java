package com.soundmentor.soundmentorweb.service;

import com.alibaba.dashscope.aigc.multimodalconversation.AudioParameters;

public interface TTSService {
    /**
     * 文本转语音
     * @param voice 声音类型
     * @param languageType 语言类型
     * @param text 文本内容
     * @param speed 语速
     * @return 语音文件URL
     */
    String textToSpeech(AudioParameters.Voice voice, String text, float speed);
}