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
 * 任务表，存储所有任务的详细信息和状态
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("task")
public class TaskDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID，自动递增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务详细信息，JSON格式
     */
    private String taskDetail;

    /**
     * 任务类型，VARCHAR类型
     */
    private Integer type;

    /**
     * 任务状态，VARCHAR类型
     */
    private Integer status;

    /**
     * 创建时间，默认为当前时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间，更新时自动更新为当前时间
     */
    private LocalDateTime updateTime;


}
