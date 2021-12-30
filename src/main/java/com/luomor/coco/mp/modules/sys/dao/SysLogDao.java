package com.luomor.coco.mp.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luomor.coco.mp.modules.sys.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 * @author Peter
 */
@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity> {

}
