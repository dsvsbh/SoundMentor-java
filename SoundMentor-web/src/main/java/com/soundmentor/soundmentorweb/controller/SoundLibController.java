package com.soundmentor.soundmentorweb.controller;

import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.basic.IdListParam;
import com.soundmentor.soundmentorpojo.DTO.basic.IdParam;
import com.soundmentor.soundmentorpojo.DTO.basic.PageResult;
import com.soundmentor.soundmentorpojo.DTO.userSound.req.UserFavoriteQueryParam;
import com.soundmentor.soundmentorpojo.DTO.userSound.req.UserSoundLibQueryParam;
import com.soundmentor.soundmentorpojo.DTO.userSound.req.UserTrainSoundQueryParam;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserSoundLibDTO;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserTrainSoundDTO;
import com.soundmentor.soundmentorweb.biz.UserSoundBiz;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 声音样本库相关接口
 * @Author: Make
 * @DATE: 2025/01/10
 **/
@RestController
@RequestMapping("/soundlib")
public class SoundLibController {
    private static final String CAN_ADD_SOUND = "canAddSound";
    private static final String GET_SOUND = "getSound";
    private static final String DEL_SOUND_LIST = "delSoundList";
    private static final String GET_SOUND_LIST = "getSoundLIST";
    private static final String GET_SOUND_LIB = "getSoundLib";
    private static final String ADD_FAVORITE = "addFavorite";
    private static final String DEL_FAVORITE = "delFavorite";
    private static final String Page_FAVORITE = "pageFavorite";

    @Resource
    private UserSoundBiz userSoundBiz;
    /**
     * 是否能添加训练声音
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(CAN_ADD_SOUND)
    public ResponseDTO<Boolean> canAddSound(){
        return ResponseDTO.OK(userSoundBiz.canAddSound());
    }


    /**
     * 获取声音样本库训练声音
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(GET_SOUND)
    public ResponseDTO<UserTrainSoundDTO> getSound(@Valid @RequestBody IdParam param){
        return ResponseDTO.OK(userSoundBiz.getSound(param.getId()));
    }

    /**
     * 删除声音样本库训练声音
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(DEL_SOUND_LIST)
    public ResponseDTO<Boolean> delSoundList(@Valid @RequestBody IdListParam param){
        AssertUtil.notEmpty(param.getIdList(), "ID集合不能为空");
        return ResponseDTO.OK(userSoundBiz.delSoundList(param.getIdList()));
    }

    /**
     * 分页查询训练声音列表
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(GET_SOUND_LIST)
    public ResponseDTO<PageResult<UserTrainSoundDTO>> getSoundList(@Valid @RequestBody UserTrainSoundQueryParam param){
        return ResponseDTO.OK(userSoundBiz.pageTrainSound(param));
    }

    /**
     * 获取声音样本库
     * @PARAM: @param param
     * @RETURN: @return
     **/
    @PostMapping(GET_SOUND_LIB)
    public ResponseDTO<PageResult<UserSoundLibDTO>> pageSoundLib(@Valid @RequestBody UserSoundLibQueryParam param){
        return ResponseDTO.OK(userSoundBiz.pageSoundLib(param));
    }

    /**
     * 添加收藏
     * @PARAM: @param param
     * @RETURN: @return
     **/
    @PostMapping(ADD_FAVORITE)
    public ResponseDTO<Boolean> addFavorite(@Valid @RequestBody IdParam param){
        return ResponseDTO.OK(userSoundBiz.addFavorite(param.getId()));
    }

    /**
     * 取消收藏
     * @PARAM: @param param
     * @RETURN: @return
     **/
    @PostMapping(DEL_FAVORITE)
    public ResponseDTO<Boolean> delFavorite(@Valid @RequestBody IdParam param){
        return ResponseDTO.OK(userSoundBiz.delFavorite(param.getId()));
    }

    /**
     * 分页查询收藏列表
     * @PARAM: @param param
     * @RETURN: @return
     **/
    @PostMapping(Page_FAVORITE)
    public ResponseDTO<PageResult<UserSoundLibDTO>> pageFavorite(@Valid @RequestBody UserFavoriteQueryParam param){
        return ResponseDTO.OK(userSoundBiz.pageFavoriteSound(param));
    }

}
