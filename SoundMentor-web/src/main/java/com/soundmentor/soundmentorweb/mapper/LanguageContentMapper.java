package com.soundmentor.soundmentorweb.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soundmentor.soundmentorbase.enums.ContentTypeEnum;
import com.soundmentor.soundmentorbase.enums.LanguageEnum;
import com.soundmentor.soundmentorpojo.DO.LanguageContent;
import com.soundmentor.soundmentorpojo.DTO.content.ContentQueryParam;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-04-12
 */
public interface LanguageContentMapper extends BaseMapper<LanguageContent> {

    List<Integer> getContentIdsByLanguageAndType(ContentQueryParam param);
}
