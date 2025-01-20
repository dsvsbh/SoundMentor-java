package com.soundmentor.soundmentorbase.enums;

import com.soundmentor.soundmentorbase.constants.SoundMentorConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 声音类型枚举，分为用户自训练和预设声音
 * @Author: Make
 * @DATE: 2025/01/19
 **/
@Getter
@AllArgsConstructor
public enum SoundLibEnum {
    USER_TRAIN("0","用户自定义训练","userTrain",""),
    XIAO_MENG("1","小萌","zh-CN-XiaomengNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-XiaomengNeural.mp3"),

    XIAO_MO("2","小墨","zh-CN-XiaomoNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-XiaomoNeural.mp3"),

    XIAO_QIU("3","小球","zh-CN-XiaoqiuNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-XiaoqiuNeural.mp3"),

    XIAO_RUI("4","小蕊","zh-CN-XiaoruiNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-XiaoruiNeural.mp3"),

    XIAO_SHUANG("5","小爽","zh-CN-XiaoshuangNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-XiaoshuangNeural.mp3"),

    XIAO_XIAO("6","小小","zh-CN-XiaoxiaoNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-XiaoxiaoNeural.mp3"),

    XIAO_XUAN("7","小玄","zh-CN-XiaoxuanNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-XiaoxuanNeural.mp3"),

    XIAO_YAN("8","小言","zh-CN-XiaoyanNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-XiaoyanNeural.mp3"),

    XIAO_YI("9","小艺","zh-CN-XiaoyiNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-XiaoyiNeural.mp3"),

    XIAO_YOU("10","小右","zh-CN-XiaoyouNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-XiaoyouNeural.mp3"),

    XIAO_ZHEN("11","小真","zh-CN-XiaozhenNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-XiaozhenNeural.mp3"),

    YUN_HAO("12","允浩","zh-CN-YunhaoNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-YunhaoNeural.mp3"),

    YUN_XI("13","云熙","zh-CN-YunxiNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-YunxiNeural.mp3"),

    YUN_YANG("14","云杨","zh-CN-YunyangNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-YunyangNeural.mp3"),

    YUN_YE("15","云野","zh-CN-YunyeNeural",
            SoundMentorConstant.MINIO_MP3_PREFIX + "zh-CN-YunyeNeural.mp3");


    /**
     * code
     **/
    public final String code;
    /**
     * 声音名
     **/
    public final String name;
    /**
     * 传给python的参数
     **/
    public final String paramName;
    /**
     * 声音样例地址
     **/
    public final String SoundUrl;

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public String getParamName() {
        return paramName;
    }
    public String getSoundUrl() {
        return SoundUrl;
    }
    public static final Map<String, SoundLibEnum> MAP = new HashMap<>(Stream.of(values()).collect(Collectors.toMap(SoundLibEnum::getCode, Function.identity())));
    public static SoundLibEnum valueOfCode(String code) {
        return MAP.get(code);
    }
}
