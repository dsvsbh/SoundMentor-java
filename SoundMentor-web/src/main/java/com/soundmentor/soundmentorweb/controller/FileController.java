package com.soundmentor.soundmentorweb.controller;

import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.file.FileUploadResDTO;
import com.soundmentor.soundmentorweb.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件相关接口
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    /**
     * 文件上传接口,任务执行前需要上传文件返回文件地址
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseDTO<FileUploadResDTO> upload(@RequestParam("file") MultipartFile file){
        return ResponseDTO.OK(fileService.uploadFile(file));
    }
}
