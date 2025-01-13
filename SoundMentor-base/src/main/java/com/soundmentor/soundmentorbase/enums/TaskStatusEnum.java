package com.soundmentor.soundmentorbase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatusEnum {
    CREATED(0, "创建"),
    RUNNING(1, "执行中"),
    SUCCESS(2, "成功"),
    FAIL(3, "失败"),
    ;
    private final Integer code;
    private final String desc;
}
