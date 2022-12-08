package org.jeecg.modules.marketing.api;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingAdvertisingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 广告接口API
 */
@Controller
@RequestMapping("front/MarketingAdvertising")
public class FrontMarketingAdvertisingController {

    @Autowired
    private IMarketingAdvertisingService iMarketingAdvertisingService;

    /**
     * 查询广告信息
     * @param pattern  0:首页；1：分类；2：逛好店   其他的参考字典
     * @return
     */
    @RequestMapping("findarketingAdvertisingList")
    @ResponseBody
    public Result<List<Map<String,Object>>> findarketingAdvertisingList(String pattern,
                                                                        @RequestHeader(defaultValue = "") String sysUserId){

        Result<List<Map<String,Object>>> result=new Result<>();

        //参数校验
        if(StringUtils.isBlank(pattern)){
            result.error500("pattern不能为空！！！");
            return result;
        }

        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("pattern",pattern);
        paramObjectMap.put("sysUserId",sysUserId);

        result.setResult(iMarketingAdvertisingService.findMarketingAdvertisingByAdLocation(paramObjectMap));

        result.success("查询广告信息成功");
        return result;
    }

}
