package com.soundmentor.soundmentorpojo.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("language_content")
public class LanguageContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String content;

    /**
     * 语言 1: 中文 2: 英文 ...
     */
    private Integer language;

    /**
     * 类型 1: 词语/单词 2: 诗词
     */
    private Integer type;
    /**
     * 翻译
     */
    private String translation;
    /**
     * 发音
     */
    private String pronunciation;
}
