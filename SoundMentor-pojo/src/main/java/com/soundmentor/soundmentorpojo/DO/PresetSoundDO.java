package com.soundmentor.soundmentorpojo.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 预制声音样本表
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("preset_sound")
@AllArgsConstructor
@NoArgsConstructor
public class PresetSoundDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增长的唯一标识符
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 预制声音文件的存储地址
     */
    private String soundUrl;

    /**
     * 声音名称
     */
    private String soundName;

    /**
     * API 参数，默认为 preset
     */
    private String apiParam;

    /**
     * 声音描述
     */
    private String description;
}
