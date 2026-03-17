package com.soundmentor.soundmentorbase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PPT 任务状态枚举
 *
 * @author liuzhicheng
 * @since 2025-01-04
 */
@Getter
@AllArgsConstructor
public enum PptTaskStatusEnum {
    
    /**
     * 创建
     */
    CREATED(0, "创建"),
    
    /**
     * 讲解生成中
     */
    EXPLANATION_GENERATING(1, "讲解生成中"),
    
    /**
     * 讲解生成失败
     */
    EXPLANATION_GENERATION_FAILED(2, "讲解生成失败"),
    
    /**
     * 讲解已生成
     */
    EXPLANATION_GENERATED(3, "讲解已生成"),
    
    /**
     * 讲解语音生成中
     */
    EXPLANATION_VOICE_GENERATING(4, "讲解语音生成中"),
    
    /**
     * 讲解语音生成失败
     */
    EXPLANATION_VOICE_GENERATION_FAILED(5, "讲解语音生成失败"),
    
    /**
     * 讲解语音已生成
     */
    EXPLANATION_VOICE_GENERATED(6, "讲解语音已生成"),
    
    /**
     * 有声 PPT 生成中
     */
    AUDIO_PPT_GENERATING(7, "有声 PPT 生成中"),
    
    /**
     * 有声 PPT 生成失败
     */
    AUDIO_PPT_GENERATION_FAILED(8, "有声 PPT 生成失败"),
    
    /**
     * 有声 PPT 已生成
     */
    AUDIO_PPT_GENERATED(9, "有声 PPT 已生成");
    
    private final Integer code;
    private final String description;
    
    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return PPT 任务状态枚举，如果不存在则返回 null
     */
    public static PptTaskStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PptTaskStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
