package com.luomor.coco.mp.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

/**
 * 系统配置信息
 * @author Peter
 */
@Data
@TableName("sys_config")
public class SysConfigEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    @NotBlank(message = "参数名不能为空")
    private String paramKey;
    @NotBlank(message = "参数值不能为空")
    private String paramValue;
    private String remark;

}
