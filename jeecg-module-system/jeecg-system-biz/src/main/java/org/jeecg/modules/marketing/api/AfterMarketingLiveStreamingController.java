package org.jeecg.modules.marketing.api;

import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingLiveStreaming;
import org.jeecg.modules.marketing.service.IMarketingLiveStreamingService;
import org.jeecg.utils.tengxun.LiveStreamingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;

@Log
@Controller
@RequestMapping("after/marketingLiveStreaming")
public class AfterMarketingLiveStreamingController {


    @Autowired
    private IMarketingLiveStreamingService iMarketingLiveStreamingService;

    @Autowired
    private LiveStreamingUtils liveStreamingUtils;

    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 根据直播id获取直播详情
     *
     * @param id
     * @return
     */
    @RequestMapping("getMarketingLiveStreamingListById")
    @ResponseBody
    public Result<?> getMarketingLiveStreamingListById(String id){
        if(StringUtils.isBlank(id)){
            return Result.error("直播id不能为空");
        }
        return Result.ok(iMarketingLiveStreamingService.getMarketingLiveStreamingListById(id));
    }

    /**
     * 直播点赞
     *
     * @param id
     * @return
     */
    @RequestMapping("thumb")
    @ResponseBody
    public Result<?> thumb(String id){
        if(StringUtils.isBlank(id)){
            return Result.error("直播id不能为空");
        }
        MarketingLiveStreaming marketingLiveStreaming=iMarketingLiveStreamingService.getById(id);
        marketingLiveStreaming.setThumb(marketingLiveStreaming.getThumb().add(new BigDecimal(1)));
        iMarketingLiveStreamingService.saveOrUpdate(marketingLiveStreaming);
        return Result.ok();
    }

    /**
     * 获取会员票据
     *
     * @param memberId
     * @return
     */
    @RequestMapping("getSign")
    @ResponseBody
    public Result<?> getSign(@RequestAttribute(value = "memberId",required = false) String memberId){
        return Result.ok(liveStreamingUtils.getSign(memberId));
    }

    /**
     * 轮询获取最新在线人数
     * @param id
     * @return
     */
    @RequestMapping("getOnlineNumber")
    @ResponseBody
    public Result<?> getOnlineNumber(String id){
        if (StringUtils.isBlank(id)){
            return Result.error("前端id未传递");
        }
        String s = redisTemplate.opsForValue().get(id);
            HashMap<String, Object> map = new HashMap<>();
            map.put("newOnlineNumber",StringUtils.isBlank(s)?"1":s);
            return Result.ok(map);
    }
}
