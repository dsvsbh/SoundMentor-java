package com.soundmentor.soundmentorpojo.DTO.tts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 预设文本朗读记录响应DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NormalTtsRecordResDTO {
    
    /**
     * 记录ID
     */
    private Integer id;
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 音频文件名
     */
    private String fileName;
    
    /**
     * 音频文件URL
     */
    private String fileUrl;
    
    /**
     * 语速
     */
    private Float speed;
    
    /**
     * 声音名
     */
    private String voiceName;
    
    /**
     * 文本内容
     */
    private String content;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
