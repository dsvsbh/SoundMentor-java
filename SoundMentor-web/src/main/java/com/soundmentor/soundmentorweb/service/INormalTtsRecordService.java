package com.soundmentor.soundmentorweb.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.soundmentor.soundmentorpojo.DO.NormalTtsRecord;
import com.soundmentor.soundmentorpojo.DTO.basic.PageResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-04-13
 */
public interface INormalTtsRecordService extends IService<NormalTtsRecord> {

    PageResult<NormalTtsRecord> getRecords(Integer pageNum, Integer pageSize);
}
