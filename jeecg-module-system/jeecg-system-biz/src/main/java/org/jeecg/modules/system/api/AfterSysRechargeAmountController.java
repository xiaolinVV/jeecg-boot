package org.jeecg.modules.system.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.SysRechargeAmount;
import org.jeecg.modules.system.service.ISysRechargeAmountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 充值金额
 */

@Controller
@RequestMapping("after/sysRechargeAmount")
public class AfterSysRechargeAmountController {


    @Autowired
    private ISysRechargeAmountService iSysRechargeAmountService;


    /**
     * 获取充值金额
     *
     * @return
     */
    @RequestMapping("getSysRechargeAmount")
    @ResponseBody
    public Result<?> getSysRechargeAmount(){
        Map<String,Object> resultMap= Maps.newHashMap();
        long count=iSysRechargeAmountService.count(new LambdaQueryWrapper<SysRechargeAmount>().eq(SysRechargeAmount::getStatus,"1"));
        if(count==0){
            resultMap.put("isViewSysRechargeAmount","0");
        }else{
            resultMap.put("isViewSysRechargeAmount","1");
            resultMap.put("sysRechargeAmountList",iSysRechargeAmountService.list(new LambdaQueryWrapper<SysRechargeAmount>()
                    .eq(SysRechargeAmount::getStatus,"1").orderByAsc(SysRechargeAmount::getSort)));
        }
        return Result.ok(resultMap);
    }

}
