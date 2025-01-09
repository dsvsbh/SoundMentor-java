package com.soundmentor.soundmentorweb.service.impl;

import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorweb.service.FileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;

@Service
public class FileServiceImpl implements FileService {

    @Resource
    private MinioClient minioClient;

    // 根据文件类型选择 bucket
    public String uploadFile(MultipartFile file) {
        // 遍历文件类型枚举，找到文件对应类型和对应的 bucket
        String bucketName = null;
        for (FileTypeEnum value : FileTypeEnum.values()) {
            if (value.getSuffix().equals(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")))) {
                bucketName = value.getBucket();
                break;
            }
        }
        if (bucketName == null) {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "暂不支持该文件");
        }
        // 上传文件
        try(InputStream inputStream = file.getInputStream())
        {
            String objectName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            // 上传文件到 MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .build());

            return "/"+bucketName + "/" + objectName;
        } catch (Exception e)
        {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), ResultCodeEnum.FILE_ERROR.getMsg());
        }
    }
}
