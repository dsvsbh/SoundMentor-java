package com.soundmentor.soundmentorpojo.DTO.ppt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PPTTaskResultDTO {
    /**
     * 用户pptid
     */
    private Integer userPptId;
    /**
     * ppt页码
     */
    private Integer pptPage;
    /**
     * 生成的讲解
     */
    private String summary;
    /**
     * 生成的音频地址
     */
    private String soundUrl;
    /**
     * 预览图片地址
     */
    private String imgUrl;
}
