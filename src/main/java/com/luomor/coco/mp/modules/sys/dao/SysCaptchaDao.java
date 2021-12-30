package com.luomor.coco.mp.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luomor.coco.mp.modules.sys.entity.SysCaptchaEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 验证码
 * @author Peter
 */
@Mapper
public interface SysCaptchaDao extends BaseMapper<SysCaptchaEntity> {

}
