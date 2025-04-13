package com.soundmentor.soundmentorweb.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soundmentor.soundmentorpojo.DO.NormalTtsRecord;
import com.soundmentor.soundmentorpojo.DTO.basic.PageResult;
import com.soundmentor.soundmentorweb.mapper.NormalTtsRecordMapper;
import com.soundmentor.soundmentorweb.service.INormalTtsRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-04-13
 */
@Service
public class NormalTtsRecordServiceImpl extends ServiceImpl<NormalTtsRecordMapper, NormalTtsRecord> implements INormalTtsRecordService {
    @Resource
    private UserInfoApi userInfoApi;
    @Override
    public PageResult<NormalTtsRecord> getRecords(Integer pageNum, Integer pageSize) {
        Page<NormalTtsRecord> page = lambdaQuery().eq(NormalTtsRecord::getUserId, userInfoApi.getUser().getId())
                .orderByDesc(NormalTtsRecord::getCreateTime)
                .page(new Page<>(pageNum, pageSize));
        PageResult<NormalTtsRecord> pageResult = new PageResult();
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setPages(page.getPages());
        pageResult.setRecords(page.getRecords());
        pageResult.setTotal(page.getTotal());
        return pageResult;
    }
}
