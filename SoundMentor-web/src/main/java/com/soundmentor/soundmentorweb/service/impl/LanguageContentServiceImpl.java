package com.soundmentor.soundmentorweb.service.impl;

import com.soundmentor.soundmentorpojo.DO.LanguageContent;
import com.soundmentor.soundmentorpojo.DTO.content.ContentQueryParam;
import com.soundmentor.soundmentorpojo.DTO.content.GetContentReq;
import com.soundmentor.soundmentorweb.mapper.LanguageContentMapper;
import com.soundmentor.soundmentorweb.service.ILanguageContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-04-12
 */
@Service
public class LanguageContentServiceImpl extends ServiceImpl<LanguageContentMapper, LanguageContent> implements ILanguageContentService {
    @Resource
    private LanguageContentMapper languageContentMapper;
    @Override
    public LanguageContent getRandomLanguageContent(GetContentReq req) {
        ContentQueryParam param = new ContentQueryParam();
        param.setLanguage(req.getLanguage().getCode());
        param.setType(req.getType().getCode());
        List<Integer> ids=languageContentMapper.getContentIdsByLanguageAndType(param);
        int randomIndex = (int) (Math.random() * ids.size());
        Integer id = ids.get(randomIndex);
        return getById(id);
    }
}
