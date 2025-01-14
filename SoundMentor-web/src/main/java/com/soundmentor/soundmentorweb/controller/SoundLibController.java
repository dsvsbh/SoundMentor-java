package com.soundmentor.soundmentorweb.controller;

import com.soundmentor.soundmentorbase.utils.AssertUtil;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.basic.IdListParam;
import com.soundmentor.soundmentorpojo.DTO.basic.IdParam;
import com.soundmentor.soundmentorpojo.DTO.basic.StringParam;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserSoundRelDTO;
import com.soundmentor.soundmentorweb.biz.UserSoundBiz;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 声音样本库相关接口
 * @Author: Make
 * @DATE: 2025/01/10
 **/
@RestController
@RequestMapping("/soundlib")
public class SoundLibController {
    private static final String CAN_ADD_SOUND = "canAddSound";
    private static final String ADD_SOUND = "addSound";
    private static final String GET_SOUND = "getSound";
    private static final String DEL_SOUND_LIST = "delSoundList";
    private static final String GET_SOUND_LIST = "getSoundLIST";

    @Resource
    private UserSoundBiz userSoundBiz;
    /**
     * 是否能添加声音
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(CAN_ADD_SOUND)
    public ResponseDTO<Boolean> canAddSound(){
        return ResponseDTO.OK(userSoundBiz.canAddSound());
    }

///    /**
///    * 往声音样本库中添加声音,训练声音
///     * @PARAM:
///     * @RETURN: @return
///     **/
///    @PostMapping(ADD_SOUND)
///    public ResponseDTO<Integer> addSound(@Valid @RequestBody StringParam param){
///        return ResponseDTO.OK(userSoundBiz.addSound(param.getData()));
///    }

    /**
     * 获取声音样本库声音
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(GET_SOUND)
    public ResponseDTO<UserSoundRelDTO> getSound(@Valid @RequestBody IdParam param){
        return ResponseDTO.OK(userSoundBiz.getSound(param.getId()));

    }

    /**
     * 删除声音样本库声音
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(DEL_SOUND_LIST)
    public ResponseDTO<Boolean> delSoundList(@Valid @RequestBody IdListParam param){
        AssertUtil.notEmpty(param.getIdList(), "ID集合不能为空");
        return ResponseDTO.OK(userSoundBiz.delSoundList(param.getIdList()));
    }

    /**
     * 获取声音样本库声音列表
     * @PARAM:
     * @RETURN: @return
     **/
    @PostMapping(GET_SOUND_LIST)
    public ResponseDTO<List<UserSoundRelDTO>> getSoundList(){
        return ResponseDTO.OK(userSoundBiz.getSoundList());
    }

}
