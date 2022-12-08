package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingActivityListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 活动列表
 */
@RequestMapping("front/marketingActivityList")
@Controller
public class FrontMarketingActivityListController {

    @Autowired
    private IMarketingActivityListService iMarketingActivityListService;

    /**
     * 获取活动列表
     *
     * @return
     */
    @RequestMapping("getMarketingActivityList")
    @ResponseBody
    public Result<?> getMarketingActivityList(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        return Result.ok(iMarketingActivityListService.getMarketingActivityList(new Page<>(pageNo,pageSize)));
    }

}
