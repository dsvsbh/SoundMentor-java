package com.soundmentor.soundmentorpojo.DTO.tts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 预设文本朗读生成请求DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NormalTtsGenerateReqDTO {
    
    /**
     * 文本内容（最多1000字）
     */
    @NotBlank(message = "文本内容不能为空")
    private String content;
    
    /**
     * 声音名称（枚举值）
     */
    @NotBlank(message = "声音不能为空")
    private String voiceName;
    
    /**
     * 语速（0.5-2.0）
     */
    @NotNull(message = "语速不能为空")
    private Float speed;
}
