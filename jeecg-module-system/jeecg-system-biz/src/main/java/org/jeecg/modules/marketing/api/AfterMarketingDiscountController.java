package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingDiscount;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.marketing.service.IMarketingDiscountCouponService;
import org.jeecg.modules.marketing.service.IMarketingDiscountService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 优惠券登录后api接口
 */
@RequestMapping("after/marketingDiscount")
@Controller
@Slf4j
public class AfterMarketingDiscountController {

    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingDiscountCouponService iMarketingDiscountCouponService;

    /**
     * 领取优惠券
     * @return
     */
    @RequestMapping("drawMarketingDiscountByIdAndMemberId")
    @ResponseBody
    public Result<String> drawMarketingDiscountByIdAndMemberId(String id,
                                                               @RequestAttribute("memberId") String memberId){
        Result<String> result=new Result<>();


        //参数校验
        if(StringUtils.isBlank(id)){
            result.error500("优惠券id不能为空！！！");
            result.setResult("1");
            return result;
        }

        MarketingDiscount marketingDiscount=iMarketingDiscountService.getById(id);

        MemberList memberList=iMemberListService.getById(memberId);

        //限制性条件判断
        //数量限制
        if(marketingDiscount.getTotal().subtract(marketingDiscount.getReleasedQuantity()).longValue()<=0){
            result.error500("优惠券的数量不足，领取失败！！！");
            result.setResult("2");
            return result;
        }

        //会员领取限制
        if(StringUtils.indexOf(marketingDiscount.getGetRestrict(),memberList.getMemberType())==-1){
            result.setResult("3");
            result.error500("本券仅{vip会员}可领取，您当前身份为{普通会员}，请先升级为{vip会员}");
            return result;
        }

        if(memberList.getShareTimes().intValue()<=0){
            result.setResult("4");
            result.error500("会员领券次数不足，请先分享优惠券获取次数");
            return result;
        }

        //再次领取限制
        if(marketingDiscount.getIsGetThe().equals("0")){
            QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrapper = new QueryWrapper<>();
            marketingDiscountCouponQueryWrapper.eq("marketing_discount_id", marketingDiscount.getId());
            marketingDiscountCouponQueryWrapper.eq("member_list_id", memberId);
            long count = iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrapper);
            if (count > 0) {
                result.setResult("5");
                result.error500("本券不支持再次领取");
                return result;
            }else{
                QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrappersender = new QueryWrapper<>();
                marketingDiscountCouponQueryWrappersender.eq("marketing_discount_id", marketingDiscount.getId());
                marketingDiscountCouponQueryWrappersender.eq("give_member_list_id", memberId);
                long countSender = iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrappersender);
                if (countSender > 0) {
                    result.setResult("5");
                    result.error500("本券不支持再次领取");
                    return result;
                }
            }

        }else {
            //支持再次领取
            QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrapper = new QueryWrapper<>();
            marketingDiscountCouponQueryWrapper.eq("marketing_discount_id", marketingDiscount.getId());
            marketingDiscountCouponQueryWrapper.eq("member_list_id", memberId);
            long count = iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrapper);
            if (count > 0) {
                if (StringUtils.indexOf(marketingDiscount.getAgainGet(), "2") == -1) {
                    //已过期不可再领
                    QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrappersender = new QueryWrapper<>();
                    marketingDiscountCouponQueryWrappersender.eq("marketing_discount_id", marketingDiscount.getId());
                    marketingDiscountCouponQueryWrappersender.eq("give_member_list_id", memberId);
                    marketingDiscountCouponQueryWrappersender.eq("status", "3");
                    long countSender = iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrappersender);
                    if (countSender > 0) {
                        result.setResult("6");
                        result.error500("本券已过期后不可再次领取！！！");
                        return result;
                    }
                }


                if (StringUtils.indexOf(marketingDiscount.getAgainGet(), "1") == -1) {
                    //已使用不可领取
                    QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrappersender = new QueryWrapper<>();
                    marketingDiscountCouponQueryWrappersender.eq("marketing_discount_id", marketingDiscount.getId());
                    marketingDiscountCouponQueryWrappersender.eq("give_member_list_id", memberId);
                    marketingDiscountCouponQueryWrappersender.eq("status", "2");
                    long countSender = iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrappersender);
                    if (countSender > 0) {
                        result.setResult("6");
                        result.error500("本券已使用后不可再次领取！！！");
                        return result;
                    }
                }

                //已领取未使用不可在领取

                QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrappersender = new QueryWrapper<>();
                marketingDiscountCouponQueryWrappersender.eq("marketing_discount_id", marketingDiscount.getId());
                marketingDiscountCouponQueryWrappersender.eq("member_list_id", memberId);
                marketingDiscountCouponQueryWrappersender.in("status", "0", "1");
                long countSender = iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrappersender);
                if (countSender > 0) {
                    result.setResult("7");
                    result.error500("本券已领取未使用不可再次领取！！！");
                    return result;
                }
            } else {
                //再次赠送条件
                if (StringUtils.indexOf(marketingDiscount.getAgainGet(), "0") == -1) {
                    //送出不可领取
                    QueryWrapper<MarketingDiscountCoupon> marketingDiscountCouponQueryWrappersender = new QueryWrapper<>();
                    marketingDiscountCouponQueryWrappersender.eq("marketing_discount_id", marketingDiscount.getId());
                    marketingDiscountCouponQueryWrappersender.eq("give_member_list_id", memberId);
                    long countSender = iMarketingDiscountCouponService.count(marketingDiscountCouponQueryWrappersender);
                    if (countSender > 0) {
                        result.setResult("8");
                        result.error500("本券已赠送后不可再次领取！！！");
                        return result;
                    }
                }
            }
        }
        //券的发放
        iMarketingDiscountService.generate(marketingDiscount.getId(),new BigDecimal(1),memberId,false);
        result.setResult("0");
        result.success("优惠券领取成功");
        return  result;
    }
}
