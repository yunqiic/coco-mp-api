package com.luomor.coco.mp.modules.wx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.luomor.coco.mp.config.TaskExcutor;
import com.luomor.coco.mp.modules.wx.entity.TemplateMsgLog;
import com.luomor.coco.mp.modules.wx.entity.WxUser;
import com.luomor.coco.mp.modules.wx.form.TemplateMsgBatchForm;
import com.luomor.coco.mp.modules.wx.service.MsgTemplateService;
import com.luomor.coco.mp.modules.wx.service.TemplateMsgLogService;
import com.luomor.coco.mp.modules.wx.service.TemplateMsgService;
import com.luomor.coco.mp.modules.wx.service.WxUserService;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter
 * @date 2017-9-27
 */
@Service
@RequiredArgsConstructor
public class TemplateMsgServiceImpl implements TemplateMsgService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TemplateMsgLogService templateMsgLogService;
    private final WxMpService wxService;
    @Autowired
    MsgTemplateService msgTemplateService;
    @Autowired
	WxUserService wxUserService;

    /**
     * 发送微信模版消息,使用固定线程的线程池
     */
    @Override
    @Async
    public void sendTemplateMsg(WxMpTemplateMessage msg,String appid) {
        TaskExcutor.submit(() -> {
            String result;
            try {
                wxService.switchover(appid);
                result = wxService.getTemplateMsgService().sendTemplateMsg(msg);
            } catch (WxErrorException e) {
                result = e.getMessage();
            }

            //保存发送日志
            TemplateMsgLog log = new TemplateMsgLog(msg,appid, result);
            templateMsgLogService.addLog(log);
        });
    }

    @Override
	@Async
    public void sendMsgBatch(TemplateMsgBatchForm form, String appid) {
		logger.info("批量发送模板消息任务开始,参数：{}",form.toString());
        wxService.switchover(appid);
		WxMpTemplateMessage.WxMpTemplateMessageBuilder builder = WxMpTemplateMessage.builder()
				.templateId(form.getTemplateId())
				.url(form.getUrl())
				.miniProgram(form.getMiniprogram())
				.data(form.getData());
		Map<String, Object> filterParams = form.getWxUserFilterParams();
		if(filterParams==null) {
            filterParams=new HashMap<>(8);
        }
		long currentPage=1L,totalPages=Long.MAX_VALUE;
		filterParams.put("appid",appid);
        filterParams.put("limit","500");
		while (currentPage<=totalPages){
			filterParams.put("page",String.valueOf(currentPage));
            //按条件查询用户
			IPage<WxUser> wxUsers = wxUserService.queryPage(filterParams);
			logger.info("批量发送模板消息任务,使用查询条件，处理第{}页，总用户数：{}",currentPage,wxUsers.getTotal());
			wxUsers.getRecords().forEach(user->{
				WxMpTemplateMessage msg = builder.toUser(user.getOpenid()).build();
				this.sendTemplateMsg(msg,appid);
			});
			currentPage=wxUsers.getCurrent()+1L;
			totalPages=wxUsers.getPages();
		}
		logger.info("批量发送模板消息任务结束");
    }

}
