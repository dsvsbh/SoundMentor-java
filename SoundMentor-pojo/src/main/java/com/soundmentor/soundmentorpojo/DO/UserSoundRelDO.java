package com.soundmentor.soundmentorpojo.DO;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 用于存储用户声音相关记录的表
 * </p>
 *
 * @author Make
 * @since 2025-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_sound_rel")
public class UserSoundRelDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增长的唯一标识符
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联用户的唯一标识，不能为空
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 用户声音文件的存储地址
     */
    @TableField("sound_url")
    private String soundUrl;

    /**
     * 标识声音相关训练是否完成，默认值为假（未完成）
     */
    @TableField("status")
    private Integer status;

    /**
     * 记录这行数据的创建时间，默认是当前系统时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
