package com.luomor.coco.mp.modules.sys.service.impl;

import com.luomor.coco.mp.modules.sys.dao.SysUserDao;
import com.luomor.coco.mp.common.utils.Constant;
import com.luomor.coco.mp.modules.sys.dao.SysMenuDao;
import com.luomor.coco.mp.modules.sys.dao.SysUserTokenDao;
import com.luomor.coco.mp.modules.sys.entity.SysMenuEntity;
import com.luomor.coco.mp.modules.sys.entity.SysUserEntity;
import com.luomor.coco.mp.modules.sys.entity.SysUserTokenEntity;
import com.luomor.coco.mp.modules.sys.service.ShiroService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserTokenDao sysUserTokenDao;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysUserDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenDao.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(Long userId) {
        return sysUserDao.selectById(userId);
    }
}
