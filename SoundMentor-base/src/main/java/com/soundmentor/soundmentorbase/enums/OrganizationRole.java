package com.soundmentor.soundmentorbase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrganizationRole {
    CREATOR(2, "Creator"),
    ADMIN(1, "Admin"),
    USER(0, "User"),
    ;
    private int code;
    private String desc;
}
