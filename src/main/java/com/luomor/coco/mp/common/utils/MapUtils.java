package com.luomor.coco.mp.common.utils;

import java.util.HashMap;


/**
 * Map工具类
 * @author Peter
 */
public class MapUtils extends HashMap<String, Object> {


    private static final long serialVersionUID = 1L;

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
