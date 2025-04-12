package com.soundmentor.soundmentorbase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentTypeEnum {
    WORD(1, "词语，单词"),
    POETRY(2, "诗词"),
    ;
    private final Integer code;
    private final String name;
}
