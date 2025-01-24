package com.soundmentor.soundmentorpojo.DTO.userSound.res;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户训练声音查询
 * @Author: Make
 * @DATE: 2025/01/19
 **/
@Data
public class UserTrainSoundDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增长的唯一标识符
     */
    private Integer id;

    /**
     * 关联用户的唯一标识，不能为空
     */
    private Integer userId;

    /**
     * 用户声音文件的存储地址
     */
    private String soundUrl;

    /**
     * 声音名称
     **/
    private String soundName;
    /**
     * 标识声音相关训练是否完成，默认值为0（未完成）
     */
    private Integer status;

    /**
     * 记录这行数据的创建时间，默认是当前系统时间
     */
    private LocalDateTime createTime;

    /**
     * 声音描述
     **/
    private String description;

}
