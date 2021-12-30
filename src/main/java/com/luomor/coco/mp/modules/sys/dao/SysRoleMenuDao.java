package com.luomor.coco.mp.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luomor.coco.mp.modules.sys.entity.SysRoleMenuEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色与菜单对应关系
 * @author Peter
 */
@Mapper
@CacheNamespace(flushInterval = 300000L)//缓存五分钟过期
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity> {

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}
