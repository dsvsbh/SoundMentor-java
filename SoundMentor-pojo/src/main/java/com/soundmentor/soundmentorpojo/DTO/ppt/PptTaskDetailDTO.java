package com.soundmentor.soundmentorpojo.DTO.ppt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * PPT 任务详情 DTO
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PptTaskDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 详情 ID
     */
    private Long id;

    /**
     * 任务 ID
     */
    private Long taskId;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 图片 URL
     */
    private String imgUrl;

    /**
     * 讲解文本
     */
    private String explanationText;

    /**
     * 讲解音频 URL
     */
    private String explanationAudioUrl;
}
