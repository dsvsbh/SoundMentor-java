package com.soundmentor.soundmentorweb.controller;

import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
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
     * 文件上传接口（测试用，前端不用管）
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseDTO<String> upload(@RequestParam("file") MultipartFile file){
        String filePath = fileService.uploadFile(file);
        return ResponseDTO.OK(filePath);
    }
}
