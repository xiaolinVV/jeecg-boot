package org.jeecg.modules.system.wapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.SysBlanceCapitalDetail;
import org.jeecg.modules.system.service.ISysBlanceCapitalDetailService;
import org.jeecg.modules.system.service.ISysBlanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 平台资金控制层
 */
@RequestMapping("wapi/sysBlance")
@Controller
@Log
public class WapiSysBlanceController {

    @Autowired
    private ISysBlanceService iSysBlanceService;

    @Autowired ISysBlanceCapitalDetailService iSysBlanceCapitalDetailService;

    /**
     * 获取平台资金
     *
     * @return
     */
    @RequestMapping("getBlance")
    @ResponseBody
    public Result<?> getBlance(){
        return Result.ok(iSysBlanceService.getBalance());
    }

    /**
     * 平台金额减少
     *
     * @param balance 金额
     * @param tradeType 交易类型;0:订单利润；1：利润退款；2：积分回购
     * @param tradeNo  单号
     */
    @RequestMapping("subtract")
    @ResponseBody
    public Result<?> subtract(BigDecimal balance, String tradeType, String tradeNo){
        iSysBlanceService.subtract(balance,tradeType,tradeNo);
        return Result.ok("平台资金减少成功");
    }

    /**
     * 查询资金列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getSysBlanceCapitalDetailList")
    @ResponseBody
    public Result<?> getSysBlanceCapitalDetailList(String serialNumber,
                                                   String tradeType,
                                                   String goAndCome,
                                                   String tradeNo,
                                                   String payTimeStart,
                                                   String payTimeEnd,
                                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.ok(iSysBlanceCapitalDetailService.page(new Page<SysBlanceCapitalDetail>(pageNo,pageSize),
                new LambdaQueryWrapper<SysBlanceCapitalDetail>()
                        .like(StringUtils.isNotBlank(serialNumber),SysBlanceCapitalDetail::getSerialNumber,serialNumber)
                        .eq(StringUtils.isNotBlank(tradeType),SysBlanceCapitalDetail::getTradeType,tradeType)
                        .eq(StringUtils.isNotBlank(goAndCome),SysBlanceCapitalDetail::getGoAndCome,goAndCome)
                        .like(StringUtils.isNotBlank(tradeNo),SysBlanceCapitalDetail::getTradeNo,tradeNo)
                        .ge(StringUtils.isNotBlank(payTimeStart),SysBlanceCapitalDetail::getPayTime,payTimeStart)
                        .le(StringUtils.isNotBlank(payTimeEnd),SysBlanceCapitalDetail::getPayTime,payTimeEnd)
                        .orderByDesc(SysBlanceCapitalDetail::getPayTime)));
    }

}
