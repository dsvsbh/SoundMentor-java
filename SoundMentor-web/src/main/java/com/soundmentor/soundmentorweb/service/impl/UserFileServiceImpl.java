package com.soundmentor.soundmentorweb.service.impl;

import com.soundmentor.soundmentorpojo.DO.UserFileDO;

import com.soundmentor.soundmentorweb.mapper.UserFileMapper;
import com.soundmentor.soundmentorweb.service.IUserFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-12
 */
@Service
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFileDO> implements IUserFileService {

    @Override
    public void bindUserFile(UserFileDO userFileDO) {
        UserFileDO one = this.lambdaQuery()
                .eq(UserFileDO::getUserId, userFileDO.getUserId())
                .eq(UserFileDO::getFileId, userFileDO.getFileId())
                .one();
        if (one == null) {
            this.save(userFileDO);
        }
    }
}
