package com.luomor.coco.mp.modules.oss.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luomor.coco.mp.modules.oss.dao.SysOssDao;
import com.luomor.coco.mp.modules.oss.entity.SysOssEntity;
import com.luomor.coco.mp.modules.oss.service.SysOssService;
import com.luomor.coco.mp.common.utils.PageUtils;
import com.luomor.coco.mp.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysOssEntity> page = this.page(
            new Query<SysOssEntity>().getPage(params)
        );

        return new PageUtils(page);
    }

}
