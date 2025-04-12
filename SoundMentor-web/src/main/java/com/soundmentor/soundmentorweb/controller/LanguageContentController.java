package com.soundmentor.soundmentorweb.controller;


import com.soundmentor.soundmentorbase.enums.ContentTypeEnum;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorpojo.DO.LanguageContent;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.content.GetContentReq;
import com.soundmentor.soundmentorweb.service.ILanguageContentService;
import org.apache.commons.lang3.StringUtils;
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
        if(StringUtils.isEmpty(languageContent.getContent()))
        {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "内容不能为空");
        }
        if(languageContent.getType().equals(ContentTypeEnum.WORD.getCode()))
        {
            if(StringUtils.isEmpty(languageContent.getTranslation()))
            {
                throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "翻译不能为空");
            }
            if(StringUtils.isEmpty(languageContent.getPronunciation()))
            {
                throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(), "发音不能为空");
            }
        }
       languageContentService.save(languageContent);
       return ResponseDTO.OK(languageContent);
    }
}
