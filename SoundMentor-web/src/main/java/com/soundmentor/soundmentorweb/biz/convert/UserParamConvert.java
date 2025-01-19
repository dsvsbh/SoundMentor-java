package com.soundmentor.soundmentorweb.biz.convert;

import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorpojo.DO.UserSoundRelDO;
import com.soundmentor.soundmentorpojo.DTO.user.req.AddUserParam;
import com.soundmentor.soundmentorpojo.DTO.user.req.UpdateUserInfoParam;
import com.soundmentor.soundmentorpojo.DTO.user.res.UserDTO;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserTrainSoundDTO;
import org.mapstruct.Mapper;
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
    UserTrainSoundDTO convert(UserSoundRelDO param);
}