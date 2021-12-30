package com.luomor.coco.mp.modules.wx.form;

import com.luomor.coco.mp.common.exception.RRException;
import com.luomor.coco.mp.common.utils.Json;
import lombok.Data;

@Data
public class TemplateMsgForm {
    private String openid;
    private String msg;
    private String template;

    @Override
    public String toString() {
        return Json.toJsonString(this);
    }

    public boolean isValid() {
        if (openid == null || openid.isEmpty() || msg == null || msg.isEmpty() || template == null || template.isEmpty()) {
            throw new RRException("缺少必要参数");
        }
        return true;
    }
}
