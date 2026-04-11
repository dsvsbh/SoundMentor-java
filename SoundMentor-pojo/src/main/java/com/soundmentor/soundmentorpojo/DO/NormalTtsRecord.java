package com.soundmentor.soundmentorpojo.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @since 2025-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("normal_tts_record")
public class NormalTtsRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 音频文件名
     */
    private String fileName;

    /**
     * 音频文件url
     */
    private String fileUrl;

    /**
     * 语速
     */
    private Float speed;

    /**
     * 声音名
     */
    private String voiceName;

    /**
     * 文本内容
     */
    private String content;

    private LocalDateTime createTime;


}