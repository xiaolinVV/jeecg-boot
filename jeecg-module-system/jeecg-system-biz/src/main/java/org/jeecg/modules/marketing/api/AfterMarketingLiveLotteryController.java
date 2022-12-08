package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingLiveLotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping("after/marketingLiveLottery")
@Controller
public class AfterMarketingLiveLotteryController {
    @Autowired
    private IMarketingLiveLotteryService iMarketingLiveLotteryService;

    /**
     * 本场直播的奖品
     * @param marketingLiveStreamingId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getMarketingLiveLotteryListByStreamingId")
    @ResponseBody
    public Result<?>getMarketingLiveLotteryListByStreamingId(String marketingLiveStreamingId,
                                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        if (StringUtils.isBlank(marketingLiveStreamingId)){
            return Result.error("前端直播间id未传递!");
        }
        return Result.ok(iMarketingLiveLotteryService.getMarketingLiveLotteryListByStreamingId(new Page<Map<String,Object>>(pageNo,pageSize),marketingLiveStreamingId));
    }

}
