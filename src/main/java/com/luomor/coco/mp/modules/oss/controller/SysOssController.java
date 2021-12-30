package com.luomor.coco.mp.modules.oss.controller;

import com.alibaba.fastjson.JSON;
import com.luomor.coco.mp.common.exception.RRException;
import com.luomor.coco.mp.common.utils.ConfigConstant;
import com.luomor.coco.mp.common.utils.Constant;
import com.luomor.coco.mp.common.utils.PageUtils;
import com.luomor.coco.mp.common.utils.R;
import com.luomor.coco.mp.common.validator.ValidatorUtils;
import com.luomor.coco.mp.common.validator.group.AliyunGroup;
import com.luomor.coco.mp.common.validator.group.QcloudGroup;
import com.luomor.coco.mp.common.validator.group.QiniuGroup;
import com.luomor.coco.mp.modules.oss.cloud.CloudStorageConfig;
import com.luomor.coco.mp.modules.oss.cloud.OSSFactory;
import com.luomor.coco.mp.modules.oss.entity.SysOssEntity;
import com.luomor.coco.mp.modules.oss.service.SysOssService;
import com.luomor.coco.mp.modules.sys.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 文件上传
 * @author Peter
 */
@RestController
@RequestMapping("sys/oss")
@Api(tags = {"对象存储/文件上传"})
public class SysOssController {
    @Autowired
    private SysOssService sysOssService;
    @Autowired
    private SysConfigService sysConfigService;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;

    /**
     * 列表
     */
    @ApiOperation(value = "文件列表",notes = "对象存储管理的文件")
    @GetMapping("/list")
    @RequiresPermissions("sys:oss:all")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysOssService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 云存储配置信息
     */
    @GetMapping("/config")
    @RequiresPermissions("sys:oss:all")
    @ApiOperation(value = "云存储配置信息",notes = "首次使用前先管理后台新增配置")
    public R config() {
        CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

        return R.ok().put("config", config);
    }


    /**
     * 保存云存储配置信息
     */
    @PostMapping("/saveConfig")
    @RequiresPermissions("sys:oss:all")
    @ApiOperation(value = "保存云存储配置信息")
    public R saveConfig(@RequestBody CloudStorageConfig config) {
        //校验类型
        ValidatorUtils.validateEntity(config);

        if (config.getType() == Constant.CloudService.QINIU.getValue()) {
            //校验七牛数据
            ValidatorUtils.validateEntity(config, QiniuGroup.class);
        } else if (config.getType() == Constant.CloudService.ALIYUN.getValue()) {
            //校验阿里云数据
            ValidatorUtils.validateEntity(config, AliyunGroup.class);
        } else if (config.getType() == Constant.CloudService.QCLOUD.getValue()) {
            //校验腾讯云数据
            ValidatorUtils.validateEntity(config, QcloudGroup.class);
        }

        sysConfigService.updateValueByKey(KEY, JSON.toJSONString(config));

        return R.ok();
    }


    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @RequiresPermissions("sys:oss:all")
    @ApiOperation(value = "上传文件到OSS")
    public R upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }

        //上传文件
        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        String url = Objects.requireNonNull(OSSFactory.build()).uploadSuffix(file.getBytes(), suffix);

        //保存文件信息
        SysOssEntity ossEntity = new SysOssEntity();
        ossEntity.setUrl(url);
        ossEntity.setCreateDate(new Date());
        sysOssService.save(ossEntity);

        return R.ok().put("url", url);
    }


    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("sys:oss:all")
    @ApiOperation(value = "删除文件",notes = "只删除记录，云端文件不会删除")
    public R delete(@RequestBody Long[] ids) {
        sysOssService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
