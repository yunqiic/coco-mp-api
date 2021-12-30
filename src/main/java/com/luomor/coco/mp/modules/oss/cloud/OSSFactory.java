package com.luomor.coco.mp.modules.oss.cloud;


import com.luomor.coco.mp.common.utils.Constant;
import com.luomor.coco.mp.modules.sys.service.SysConfigService;
import com.luomor.coco.mp.common.utils.ConfigConstant;
import com.luomor.coco.mp.common.utils.SpringContextUtils;

/**
 * 文件上传Factory
 * @author Peter
 */
public final class OSSFactory {
    private static SysConfigService sysConfigService;

    static {
        OSSFactory.sysConfigService = (SysConfigService) SpringContextUtils.getBean("sysConfigService");
    }

    public static AbstractCloudStorageService build() {
        //获取云存储配置信息
        CloudStorageConfig config = sysConfigService.getConfigObject(ConfigConstant.CLOUD_STORAGE_CONFIG_KEY, CloudStorageConfig.class);

        if (config.getType() == Constant.CloudService.QINIU.getValue()) {
            return new QiniuAbstractCloudStorageService(config);
        } else if (config.getType() == Constant.CloudService.ALIYUN.getValue()) {
            return new AliyunAbstractCloudStorageService(config);
        } else if (config.getType() == Constant.CloudService.QCLOUD.getValue()) {
            return new QcloudAbstractCloudStorageService(config);
        }

        return null;
    }

}
