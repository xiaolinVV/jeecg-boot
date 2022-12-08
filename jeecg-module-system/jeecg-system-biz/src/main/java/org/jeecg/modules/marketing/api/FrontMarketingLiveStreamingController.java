package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.java.Log;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingLiveStreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Log
@Controller
@RequestMapping("front/marketingLiveStreaming")
public class FrontMarketingLiveStreamingController {

    @Autowired
    private IMarketingLiveStreamingService iMarketingLiveStreamingService;

    /**
     * 获取直播列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getMarketingLiveStreamingList")
    @ResponseBody
    public Result<?> getMarketingLiveStreamingList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.ok(iMarketingLiveStreamingService.getMarketingLiveStreamingList(new Page<>(pageNo,pageSize)));
    }


}
