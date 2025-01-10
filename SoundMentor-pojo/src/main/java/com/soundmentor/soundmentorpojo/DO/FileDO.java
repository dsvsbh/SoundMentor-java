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
 * 文件表，用于存储文件的基本信息
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("file")
public class FileDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件原始名称
     */
    private String originName;

    /**
     * 文件存储路径
     */
    private String path;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private Integer fileType;

    /**
     * 文件创建者（用户ID）
     */
    private Integer creator;

    /**
     * 文件创建时间
     */
    private LocalDateTime createTime;

    /**
     * 文件md5值
     */
    private String md5;
}
