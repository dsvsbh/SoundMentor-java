package com.soundmentor.soundmentorbase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum FileTypeEnum {
    MP3(0,".mp3", "mp3"),
    DOC(1,".doc", "doc"),
    DOCX(2,".docx", "doc"),
    PDF(3,".pdf", "doc"),
    PPT(4,".ppt", "ppt"),
    PPTX(5,".pptx", "ppt"),
    PNG(6,".png", "img"),
    JPG(7,".jpg", "img"),
    ZIP(8,".zip", "zip"),
    ;
    private final Integer code;
    private final String suffix;
    private final String bucket;
    
    // 静态 Map，用于快速根据后缀名获取枚举
    private static final Map<String, FileTypeEnum> SUFFIX_MAP = new HashMap<>();
    
    static {
        for (FileTypeEnum fileType : values()) {
            SUFFIX_MAP.put(fileType.getSuffix(), fileType);
        }
    }
    
    /**
     * 根据文件后缀名获取文件类型枚举
     * @param suffix 文件后缀名（包含点，如 .mp3）
     * @return 文件类型枚举，如果不存在则返回 null
     */
    public static FileTypeEnum getBySuffix(String suffix) {
        return SUFFIX_MAP.get(suffix);
    }
}
