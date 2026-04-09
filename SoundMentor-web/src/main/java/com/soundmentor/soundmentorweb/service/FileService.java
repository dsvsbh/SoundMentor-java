package com.soundmentor.soundmentorweb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import com.soundmentor.soundmentorpojo.DO.FileDO;
import com.soundmentor.soundmentorpojo.DTO.file.FileUploadResDTO;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

public interface FileService extends IService<FileDO> {
    /**
     * 前端用户上传文件
     * @param file
     * @return
     */
    FileUploadResDTO uploadFile(MultipartFile file);

    /**
     * minio通用文件上传
     * @param inputStream
     * @param fileType
     * @param md5
     * @return
     */
    String uploadFileToMinio(InputStream inputStream, FileTypeEnum fileType, String md5) throws Exception;
}
