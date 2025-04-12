package com.soundmentor.soundmentorbase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : lzc
 * @version : 1.0
 * @description : 语言枚举类
 */
@Getter
@AllArgsConstructor
public enum LanguageEnum {
    CHINESE(1, "中文"),
    ENGLISH(2, "英文"),
    ;
    private final Integer code;
    private final String name;
}
