package com.soundmentor.soundmentorpojo.DTO.organization;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrganizationFileListResDTO {
    private Integer fileId;
    private String fileName;
    private String filePath;
    private Integer fileType;
    /**
     * 分享者名称
     */
    private String sharerName;
    /**
     * 分享者头像
     */
    private String sharerHeaderImg;
    /**
     * 下载次数
     */
    private Integer downloadCount;
    /**
     * 文件大小 单位为B，前端展示时可单位转换
     */
    private Integer fileSize;
    /**
     * 分享时间
     */
    private LocalDateTime createTime;
}
