package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingRushGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 抢购商品
 */

@Controller
@RequestMapping("front/marketingRushGood")
public class FrontMarketingRushGoodController {

    @Autowired
    private IMarketingRushGoodService iMarketingRushGoodService;


    /**
     * 根据抢购分类获取抢购商品
     *
     * @param marketingRushTypeId
     * @return
     */
    @RequestMapping("getMarketingRushGoodByTypeId")
    @ResponseBody
    public Result<?> getMarketingRushGoodByTypeId(String marketingRushTypeId,
                                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        if(StringUtils.isBlank(marketingRushTypeId)){
            return Result.error("抢购分类id不能为空");
        }
        return Result.ok(iMarketingRushGoodService.getMarketingRushGoodByTypeId(new Page<>(pageNo,pageSize),marketingRushTypeId));
    }
}
