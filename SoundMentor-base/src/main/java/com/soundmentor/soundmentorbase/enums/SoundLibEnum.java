package com.soundmentor.soundmentorbase.enums;

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
    SOFT("1","温柔型","测试","测试");
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
