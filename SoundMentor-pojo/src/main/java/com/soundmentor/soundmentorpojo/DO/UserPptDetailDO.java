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
 * @since 2025-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_ppt_detail")
public class UserPptDetailDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增长的唯一标识符
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联user_ppt_rel
     */
    private Integer userPptId;

    /**
     * ppt的页数
     */
    private Integer pptPage;

    /**
     * 该页ppt的摘要
     */
    private String summary;

    /**
     * 该页ppt的音频地址
     */
    private String soundUrl;

    /**
     * 该页ppt的最新任务id
     */
    private Integer lastTaskId;
    /**
     * 记录这行数据的创建时间，默认是当前系统时间
     */
    private LocalDateTime createTime;


}
