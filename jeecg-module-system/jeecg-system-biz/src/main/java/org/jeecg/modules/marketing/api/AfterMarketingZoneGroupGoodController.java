package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 专区团商品
 */
@RequestMapping("after/marketingZoneGroupGood")
@Controller
public class AfterMarketingZoneGroupGoodController {


    @Autowired
    private IMarketingZoneGroupGoodService iMarketingZoneGroupGoodService;

    /**
     * 根据专区id获取专区团商品
     *
     * @param marketingZoneGroupId
     * @return
     */
    @RequestMapping("getByMarketingZoneGroupId")
    @ResponseBody
    public Result<?> getByMarketingZoneGroupId(String marketingZoneGroupId,
                                               @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        //参数校验
        if(StringUtils.isBlank(marketingZoneGroupId)){
            return Result.error("专区id不能为空");
        }

        return Result.ok(iMarketingZoneGroupGoodService.getByMarketingZoneGroupId(new Page<>(pageNo,pageSize),marketingZoneGroupId));
    }

}
