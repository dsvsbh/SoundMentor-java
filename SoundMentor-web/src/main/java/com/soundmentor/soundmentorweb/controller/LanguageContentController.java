package com.soundmentor.soundmentorweb.controller;


import com.soundmentor.soundmentorbase.enums.ContentTypeEnum;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorpojo.DO.LanguageContent;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.content.GetContentReq;
import com.soundmentor.soundmentorweb.service.ILanguageContentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * 语言学习辅助内容相关接口
 * @author liuzhicheng
 * @since 2025-04-12
 */
@RestController
@RequestMapping("/language-content")
public class LanguageContentController {
    private static final Logger logger = LoggerFactory.getLogger(LanguageContentController.class);
    
    @Resource
    private ILanguageContentService languageContentService;

    /**
     * 根据条件获取随机内容
     * @param req
     * @return
     */
    @GetMapping("/random")
    public ResponseDTO<LanguageContent> getRandomLanguageContent(GetContentReq req) {
        return ResponseDTO.OK(languageContentService.getRandomLanguageContent(req));
    }

    /**
     * 用户添加内容
     * @param languageContent
     * @return
     */
    @PostMapping("/add")
    public ResponseDTO<LanguageContent> addLanguageContent(@RequestBody LanguageContent languageContent) {
        logger.info("接收到添加语言内容请求: {}", languageContent);
        
        if(StringUtils.isEmpty(languageContent.getContent()))
        {
            logger.warn("内容为空");
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "内容不能为空");
        }
        
        languageContentService.save(languageContent);
        logger.info("成功添加语言内容: {}", languageContent);
        
        return ResponseDTO.OK(languageContent);
    }
}
