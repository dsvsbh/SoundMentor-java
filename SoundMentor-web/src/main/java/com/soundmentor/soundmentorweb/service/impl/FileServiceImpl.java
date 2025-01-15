package com.soundmentor.soundmentorweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorpojo.DO.FileDO;
import com.soundmentor.soundmentorpojo.DO.UserFileDO;
import com.soundmentor.soundmentorpojo.DTO.file.FileUploadResDTO;
import com.soundmentor.soundmentorweb.config.MinioConfig.MinioConfig;
import com.soundmentor.soundmentorweb.mapper.FileMapper;
import com.soundmentor.soundmentorweb.service.FileService;
import com.soundmentor.soundmentorweb.service.IUserFileService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import io.minio.DownloadObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDO> implements FileService {

    @Resource
    private MinioClient minioClient;
    @Resource
    private MinioConfig minioConfig;
    @Resource
    private UserInfoApi userInfoApi;
    @Resource
    private IUserFileService userFileService;

    /**
     * 上传文件到 MinIO,并返回文件路径 todo 关联用户和文件
     * @param file
     * @return
     */
    @Transactional
    @Override
    public FileUploadResDTO uploadFile(MultipartFile file) {
        String md5=null;
        try {
            md5 = DigestUtils.md5Hex(file.getBytes());
        } catch (IOException e) {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "获取文件md5值失败");
        }
        FileDO one = lambdaQuery().eq(FileDO::getMd5, md5).one();
        if (one != null) {// 文件已存在，直接返回文件路径
            //关联用户
            UserFileDO userFileDO = new UserFileDO();
            userFileDO.setFileId(one.getId());
            userFileDO.setUserId(userInfoApi.getUser().getId());
            userFileDO.setCreateTime(LocalDateTime.now());
            userFileService.bindUserFile(userFileDO);
            return new FileUploadResDTO(file.getOriginalFilename(),one.getPath());
        }
        // 遍历文件类型枚举，找到文件对应类型和对应的 bucket
        String bucketName = null;
        FileTypeEnum fileTypeEnum = null;
        for (FileTypeEnum value : FileTypeEnum.values()) {
            if (value.getSuffix().equals(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")))) {
                bucketName = value.getBucket();
                fileTypeEnum = value;
                break;
            }
        }
        if (bucketName == null) {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "暂不支持该文件");
        }
        // 上传文件
        try(InputStream inputStream = file.getInputStream())
        {
            String objectName = System.currentTimeMillis() + "_" + md5 + fileTypeEnum.getSuffix();
            // 上传文件到 MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .build());
            String path = minioConfig.getMinioUrl()+"/"+bucketName + "/" + objectName;
            // 保存文件到数据库
            FileDO fileDO = new FileDO();
            fileDO.setFileSize(file.getSize());
            fileDO.setPath(path);
            fileDO.setOriginName(file.getOriginalFilename());
            fileDO.setCreateTime(LocalDateTime.now());
            fileDO.setFileType(fileTypeEnum.getCode());
            fileDO.setMd5(md5);
            this.save(fileDO);
            //关联用户
            UserFileDO userFileDO = new UserFileDO();
            userFileDO.setFileId(fileDO.getId());
            userFileDO.setUserId(userInfoApi.getUser().getId());
            userFileDO.setCreateTime(LocalDateTime.now());
            userFileService.bindUserFile(userFileDO);
            return new FileUploadResDTO(file.getOriginalFilename(),path);
        } catch (Exception e)
        {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), ResultCodeEnum.FILE_ERROR.getMsg());
        }
    }
}
