package com.soundmentor.soundmentorpojo.DTO.ppt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * PPT 任务 DTO
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
public class PptTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 原始 PPT 文件 URL
     */
    private String originalPptFileUrl;

    /**
     * 任务状态
     */
    private Integer taskStatus;

    /**
     * 音频 PPT 文件 URL
     */
    private String audioPptFileUrl;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
