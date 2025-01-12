package com.soundmentor.soundmentorpojo.DTO.userSound.res;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserSoundRelDTO implements Serializable {

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
     * 标识声音相关训练是否完成，默认值为0（未完成）
     */
    private Integer status;

    /**
     * 记录这行数据的创建时间，默认是当前系统时间
     */
    private LocalDateTime createTime;


}
