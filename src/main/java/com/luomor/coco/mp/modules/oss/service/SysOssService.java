package com.luomor.coco.mp.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luomor.coco.mp.common.utils.PageUtils;
import com.luomor.coco.mp.modules.oss.entity.SysOssEntity;

import java.util.Map;

/**
 * 文件上传
 * @author Peter
 */
public interface SysOssService extends IService<SysOssEntity> {
    /**
     * 分页查询用户数据
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    PageUtils queryPage(Map<String, Object> params);
}
