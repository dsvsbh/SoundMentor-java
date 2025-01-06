package com.soundmentor.soundmentorweb.biz.convert;

import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorpojo.DTO.user.req.AddUserParam;
import com.soundmentor.soundmentorpojo.DTO.user.req.UpdateUserInfoParam;
import com.soundmentor.soundmentorpojo.DTO.user.req.UpdateUserPasswordParam;
import com.soundmentor.soundmentorpojo.DTO.user.res.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * 用户参数转换类
 * @Author: Make
 * @DATE: 2025/01/05
 **/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserParamConvert {
    UserDO convert(AddUserParam param);
    UserDTO convert(UserDO userDO);
    UserDO convert(UpdateUserInfoParam param);
}