package com.soundmentor.soundmentorweb.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soundmentor.soundmentorpojo.DO.UserSoundRelDO;
import com.soundmentor.soundmentorpojo.DTO.file.UserFileResDTO;
import com.soundmentor.soundmentorpojo.DTO.userSound.req.UserSoundLibQueryParam;
import com.soundmentor.soundmentorpojo.DTO.userSound.req.UserTrainSoundQueryParam;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserSoundLibDTO;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserTrainSoundDTO;
import com.soundmentor.soundmentorweb.mapper.UserSoundRelMapper;
import com.soundmentor.soundmentorweb.service.IUserSoundRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用于存储用户声音相关记录的表 服务实现类
 * </p>
 *
 * @author Make
 * @since 2025-01-11
 */
@Service
public class UserSoundRelServiceImpl extends ServiceImpl<UserSoundRelMapper, UserSoundRelDO> implements IUserSoundRelService {

    /**
     * 根据用户id获取用户声音列表
     * @PARAM: @param userId
     * @RETURN: @return
     *
     * @return*/
    @Override
    public IPage<UserTrainSoundDTO> pageTrainSound(UserTrainSoundQueryParam param) {
        Page<UserFileResDTO> page = new Page<>(param.getCurrent(), param.getSize());
        return baseMapper.pageTrainSound(page,param);
    }

    /**
     * 添加用户声音
     * @PARAM: @param addDO
     * @RETURN: @return
     **/
    @Override
    public Boolean addSound(UserSoundRelDO addDO) {
        return this.save(addDO);
    }

    /**
     * 根据id获取用户声音
     * @PARAM: @param ids
     * @RETURN: @return
     **/
    @Override
    public List<UserSoundRelDO> getSoundByIds(List<Integer> ids) {
        return this.listByIds(ids);
    }

    /**
     * 删除用户声音
     * @PARAM: @param ids
     * @RETURN: @return
     **/
    @Override
    public Boolean delSoundList(List<Integer> ids) {
        return this.removeByIds(ids);
    }

    /**
     * 更新用户声音
     * @PARAM: @param updateDO
     * @RETURN: @return
     **/
    @Override
    public Boolean updateSound(UserSoundRelDO updateDO) {
        return this.updateById(updateDO);
    }

    /**
     * 根据路径获取用户声音
     * @param soundUrl
     * @PARAM: @param id
     * @RETURN: @return <p>
     **/
    @Override
    public UserSoundRelDO getByPath(Integer id, String soundUrl) {
        return this.getOne(Wrappers.<UserSoundRelDO> lambdaQuery()
                .eq(UserSoundRelDO::getSoundUrl, soundUrl)
                .eq(UserSoundRelDO::getUserId, id));
    }


    /**
     * 分页获取用户声音
     * @PARAM: @param param
     * @RETURN: @return
     **/
    @Override
    public IPage<UserSoundLibDTO> pageSoundLib(UserSoundLibQueryParam param) {
        Page<UserFileResDTO> page = new Page<>(param.getCurrent(), param.getSize());
        return baseMapper.pageSoundLib(page,param);
    }

    /**
     * 根据用户id获取用户声音
     * @PARAM: @param id
     * @RETURN: @return
     **/
    @Override
    public List<UserSoundRelDO> getSoundByUserId(Integer id) {
        return this.list(Wrappers.<UserSoundRelDO>lambdaQuery().eq(UserSoundRelDO::getUserId,id));
    }
}
