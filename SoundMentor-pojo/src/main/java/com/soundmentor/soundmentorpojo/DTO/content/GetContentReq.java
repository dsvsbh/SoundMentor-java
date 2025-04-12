package com.soundmentor.soundmentorpojo.DTO.content;

import com.soundmentor.soundmentorbase.enums.ContentTypeEnum;
import com.soundmentor.soundmentorbase.enums.LanguageEnum;
import lombok.Data;

@Data
public class GetContentReq {
    private LanguageEnum language;
    private ContentTypeEnum type;
}
