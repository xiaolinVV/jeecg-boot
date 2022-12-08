package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingGroupIntegralManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("front/marketingGroupIntegralManage")
public class FrontMarketingGroupIntegralManageController {

    @Autowired
    private IMarketingGroupIntegralManageService iMarketingGroupIntegralManageService;


    /**
     * 获取拼购列表
     *
     * @return
     */
    @RequestMapping("getMarketingGroupIntegralManage")
    @ResponseBody
    public Result<?> getMarketingGroupIntegralManage( @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        return Result.ok(iMarketingGroupIntegralManageService.getMarketingGroupIntegralManage(new Page<>(pageNo,pageSize)));
    }
}
