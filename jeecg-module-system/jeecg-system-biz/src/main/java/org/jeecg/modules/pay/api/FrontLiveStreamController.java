package org.jeecg.modules.pay.api;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingLiveStreaming;
import org.jeecg.modules.marketing.service.IMarketingLiveStreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Log
@RequestMapping("front/liveStream")
public class FrontLiveStreamController {

    @Autowired
    private IMarketingLiveStreamingService iMarketingLiveStreamingService;

    /**
     * 直播推流开始
     *
     * @param body
     * @return
     */
    @RequestMapping("pushStart")
    @ResponseBody
    public Result<?> pushStart(@RequestBody String body){
        log.info("推流开始回调："+ body);
        String streamId= JSON.parseObject(body).getString("stream_id");
        MarketingLiveStreaming marketingLiveStreaming=iMarketingLiveStreamingService.getOne(new LambdaQueryWrapper<MarketingLiveStreaming>()
                .eq(MarketingLiveStreaming::getStreamNumber,streamId)
                .eq(MarketingLiveStreaming::getStatus,"0"));
        if(marketingLiveStreaming!=null){
            log.info("启动直播，直播流id："+streamId);
            marketingLiveStreaming.setStatus("1");
            iMarketingLiveStreamingService.saveOrUpdate(marketingLiveStreaming);
        }
        return Result.ok();
    }

    /**
     * 直播推流结束
     *
     * @param body
     * @return
     */
    @RequestMapping("pushEnd")
    @ResponseBody
    public Result<?> pushEnd(@RequestBody String body){
        log.info("推流结束回调："+ body);
        String streamId= JSON.parseObject(body).getString("stream_id");
        MarketingLiveStreaming marketingLiveStreaming=iMarketingLiveStreamingService.getOne(new LambdaQueryWrapper<MarketingLiveStreaming>()
                .eq(MarketingLiveStreaming::getStreamNumber,streamId)
                .eq(MarketingLiveStreaming::getStatus,"1"));
        if(marketingLiveStreaming!=null){
            log.info("关闭直播，直播流id："+streamId);
            marketingLiveStreaming.setStatus("0");
            iMarketingLiveStreamingService.saveOrUpdate(marketingLiveStreaming);
        }
        return Result.ok();
    }
}
