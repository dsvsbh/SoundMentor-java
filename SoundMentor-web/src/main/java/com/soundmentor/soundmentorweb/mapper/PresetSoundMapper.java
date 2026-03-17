package com.soundmentor.soundmentorweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soundmentor.soundmentorpojo.DO.PresetSoundDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 预制声音样本表 Mapper 接口
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-04
 */
@Mapper
public interface PresetSoundMapper extends BaseMapper<PresetSoundDO> {

}
