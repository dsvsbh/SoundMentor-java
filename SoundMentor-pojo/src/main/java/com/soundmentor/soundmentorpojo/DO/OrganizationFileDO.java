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
 * 组织文件关系表，用于存储组织与文件之间的关系
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("organization_file")
public class OrganizationFileDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 组织ID
     */
    private Integer organizationId;

    /**
     * 文件ID，关联文件表
     */
    private Integer fileId;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;


}
