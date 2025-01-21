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
 * 用于存储用户收藏信息的表
 * </p>
 *
 * @author Make
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_favorite")
public class UserFavoriteDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增长的唯一标识符
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户的唯一标识符，不能为空
     */
    private Integer userId;
    /**
     * 收藏对象的唯一标识符，不能为空
     */
    private Integer favoriteId;

    /**
     * 记录该收藏记录的创建时间，默认是当前系统时间
     */
    private LocalDateTime createTime;


}
