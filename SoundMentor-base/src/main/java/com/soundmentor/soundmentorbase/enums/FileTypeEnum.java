package com.soundmentor.soundmentorbase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileTypeEnum {
    MP3(0,".mp3", "mp3"),
    DOC(1,".doc", "doc"),
    DOCX(2,".docx", "doc"),
    PDF(3,".pdf", "doc"),
    PPTX(5,".pptx", "ppt"),
    PNG(6,".png", "img"),
    JPG(7,".jpg", "img"),
    ;
    private final Integer code;
    private final String suffix;
    private final String bucket;
}
