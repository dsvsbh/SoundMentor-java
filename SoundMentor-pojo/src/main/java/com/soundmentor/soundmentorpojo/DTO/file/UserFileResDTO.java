package com.soundmentor.soundmentorpojo.DTO.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFileResDTO {
    private Integer fileId;
    private String fileName;
    private String filePath;
    private String fileType;
    /**
     * 文件大小 单位为B，前端展示时可单位转换
     */
    private String fileSize;
    /**
     * 上传时间
     */
    private String createTime;
}
