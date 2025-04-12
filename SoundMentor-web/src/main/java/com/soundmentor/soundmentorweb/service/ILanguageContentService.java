package com.soundmentor.soundmentorweb.service;

import com.soundmentor.soundmentorpojo.DO.LanguageContent;
import com.soundmentor.soundmentorpojo.DTO.content.GetContentReq;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-04-12
 */
public interface ILanguageContentService extends IService<LanguageContent> {

    LanguageContent getRandomLanguageContent(GetContentReq req);
}
