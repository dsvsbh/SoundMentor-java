package com.soundmentor.soundmentorweb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soundmentor.soundmentorpojo.DO.FileDO;
import com.soundmentor.soundmentorpojo.DO.OrganizationDO;
import com.soundmentor.soundmentorpojo.DTO.file.FileUploadResDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService extends IService<FileDO> {
    FileUploadResDTO uploadFile(MultipartFile file);
}
