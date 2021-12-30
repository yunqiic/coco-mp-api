package com.luomor.coco.mp.modules.wx.manage;

import com.luomor.coco.mp.common.utils.PageUtils;
import com.luomor.coco.mp.common.utils.R;
import com.luomor.coco.mp.modules.wx.entity.WxMsg;
import com.luomor.coco.mp.modules.wx.form.WxMsgReplyForm;
import com.luomor.coco.mp.modules.wx.service.MsgReplyService;
import com.luomor.coco.mp.modules.wx.service.WxMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 微信消息
 *
 * @author Peter
 * @date 2020-05-14 17:28:34
 */
@RestController
@RequestMapping("/manage/wxMsg")
@Api(tags = {"公众号消息记录-管理后台"})
public class WxMsgManageController {
    @Autowired
    private WxMsgService wxMsgService;
    @Autowired
    private MsgReplyService msgReplyService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("wx:wxmsg:list")
    @ApiOperation(value = "列表")
    public R list(@CookieValue String appid,@RequestParam Map<String, Object> params){
        params.put("appid",appid);
        PageUtils page = wxMsgService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("wx:wxmsg:info")
    @ApiOperation(value = "详情")
    public R info(@CookieValue String appid,@PathVariable("id") Long id){
		WxMsg wxMsg = wxMsgService.getById(id);

        return R.ok().put("wxMsg", wxMsg);
    }

    /**
     * 回复
     */
    @PostMapping("/reply")
    @RequiresPermissions("wx:wxmsg:save")
    @ApiOperation(value = "回复")
    public R reply(@CookieValue String appid,@RequestBody WxMsgReplyForm form){

        msgReplyService.reply(form.getOpenid(),form.getReplyType(),form.getReplyContent());
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("wx:wxmsg:delete")
    @ApiOperation(value = "删除")
    public R delete(@CookieValue String appid,@RequestBody Long[] ids){
		wxMsgService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
