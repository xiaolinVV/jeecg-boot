package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingFourthIntegralRecord;
import org.jeecg.modules.marketing.service.IMarketingFourthIntegralRecordService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
@RequestMapping("after/marketingFourthIntegralRecord")
@Log
public class AfterMarketingFourthIntegralRecordController {


    @Autowired
    private IMarketingFourthIntegralRecordService iMarketingFourthIntegralRecordService;

    @Autowired
    private IMemberListService iMemberListService;



    /**
     * 获取第四积分记录列表
     *
     * @param goAndCome
     * @param pageNo
     * @param pageSize
     * @param memberId
     * @return
     */
    @RequestMapping("getMarketingFourthIntegralRecordByMemberId")
    @ResponseBody
    public Result<?> getMarketingFourthIntegralRecordByMemberId(@RequestParam(name = "goAndCome",defaultValue = "",required = false) String goAndCome,
                                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                  @RequestAttribute(value = "memberId",required = false) String memberId){
        return Result.ok(iMarketingFourthIntegralRecordService.page(new Page<>(pageNo,pageSize),new LambdaQueryWrapper<MarketingFourthIntegralRecord>()
                .eq(MarketingFourthIntegralRecord::getMemberListId,memberId)
                .eq(StringUtils.isNotBlank(goAndCome),MarketingFourthIntegralRecord::getGoAndCome,goAndCome)
                .orderByDesc(MarketingFourthIntegralRecord::getCreateTime)
                .orderByDesc(MarketingFourthIntegralRecord::getIntegralBalance)));
    }


    /**
     * 产品券充值
     *
     * @param memberId
     * @return
     */
    @RequestMapping("recharge")
    @ResponseBody
    @Transactional
    public Result<?> recharge(BigDecimal price, @RequestAttribute(value = "memberId",required = false) String memberId){
        //参数校验
        if(price==null){
            return Result.error("金额不能为空");
        }
        if(price.doubleValue()==0){
            return Result.error("金额不能为零");
        }
        MemberList memberList=iMemberListService.getById(memberId);
        if(memberList.getBalance().subtract(price).doubleValue()<0){
            return Result.error("会员金额不足");
        }
        //扣除会员金额
        if(!iMemberListService.subtractBlance(memberId,price,memberId,"30")){
            log.info("余额产品券充值回滚");
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("系统正在努力处理中，请稍作等待，继续尝试，谢谢！！！");
        }
        //增加第四积分
        if(!iMarketingFourthIntegralRecordService.addFourthIntegral(memberId,price,memberId,"1")){
            log.info("产品券充值第四积分回滚");
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("系统正在努力处理中，请稍作等待，继续尝试，谢谢！！！");
        }
        return Result.ok("充值成功！！！");
    }

}
