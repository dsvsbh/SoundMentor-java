package com.soundmentor.soundmentorpojo.service.serviceImpl;

import com.soundmentor.soundmentorpojo.DO.OrganizationFileDO;
import com.soundmentor.soundmentorpojo.mapper.OrganizationFileMapper;
import com.soundmentor.soundmentorpojo.service.IOrganizationFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 组织文件关系表，用于存储组织与文件之间的关系 服务实现类
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-12
 */
@Service
public class OrganizationFileServiceImpl extends ServiceImpl<OrganizationFileMapper, OrganizationFileDO> implements IOrganizationFileService {

}
