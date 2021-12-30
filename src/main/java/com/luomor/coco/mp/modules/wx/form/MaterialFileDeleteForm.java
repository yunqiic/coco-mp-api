package com.luomor.coco.mp.modules.wx.form;

import com.luomor.coco.mp.common.utils.Json;
import lombok.Data;

@Data
public class MaterialFileDeleteForm {
    String mediaId;

    @Override
    public String toString() {
        return Json.toJsonString(this);
    }
}
