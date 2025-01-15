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
 * 用于存储用户ppt相关记录的表
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_ppt_rel")
public class UserPptRelDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增长的唯一标识符
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联用户的唯一标识，不能为空
     */
    private Integer userId;

    /**
     * 用户ppt文件的存储地址
     */
    private String pptUrl;

    /**
     * ppt的页数
     */
    private Integer pageCount;

    /**
     * 记录这行数据的创建时间，默认是当前系统时间
     */
    private LocalDateTime createTime;


}
