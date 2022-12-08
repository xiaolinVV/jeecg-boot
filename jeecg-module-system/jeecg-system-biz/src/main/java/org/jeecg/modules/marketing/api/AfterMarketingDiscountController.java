package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.MarketingChannel;
import org.jeecg.modules.marketing.entity.MarketingDiscount;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.marketing.service.IMarketingChannelService;
import org.jeecg.modules.marketing.service.IMarketingDiscountCouponService;
import org.jeecg.modules.marketing.service.IMarketingDiscountService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

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

    @Autowired
    private IMarketingChannelService iMarketingChannelService;



    /**
     * 领取优惠券
     * @return
     */
    @RequestMapping("drawMarketingDiscountByIdAndMemberId")
    @ResponseBody
    public Result<String> drawMarketingDiscountByIdAndMemberId(String id,
                                                               String channelName,
                                                               HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();
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

        MarketingDiscountCoupon marketingDiscountCoupon=new MarketingDiscountCoupon();
        marketingDiscountCoupon.setDelFlag("0");
        marketingDiscountCoupon.setPrice(marketingDiscount.getSubtract());
        marketingDiscountCoupon.setName(marketingDiscount.getName());
        marketingDiscountCoupon.setIsThreshold(marketingDiscount.getIsThreshold());
        marketingDiscountCoupon.setMemberListId(memberId);
        marketingDiscountCoupon.setSysUserId(marketingDiscount.getSysUserId());
        marketingDiscountCoupon.setQqzixuangu(OrderNoUtils.getOrderNo());
        marketingDiscountCoupon.setMarketingDiscountId(marketingDiscount.getId());
        marketingDiscountCoupon.setIsPlatform(marketingDiscount.getIsPlatform());
        marketingDiscountCoupon.setCompletely(marketingDiscount.getCompletely());
        marketingDiscountCoupon.setIsGive(marketingDiscount.getIsGive());
        marketingDiscountCoupon.setIsWarn(marketingDiscount.getIsWarn());
        marketingDiscountCoupon.setWarnDays(marketingDiscount.getWarnDays());
        marketingDiscountCoupon.setUserRestrict(marketingDiscount.getUserRestrict());
        marketingDiscountCoupon.setDiscountExplain(marketingDiscount.getDiscountExplain());
        marketingDiscountCoupon.setCoverPlan(marketingDiscount.getCoverPlan());
        marketingDiscountCoupon.setPosters(marketingDiscount.getPosters());
        marketingDiscountCoupon.setMainPicture(marketingDiscount.getMainPicture());
        marketingDiscountCoupon.setIsUniqueness(marketingDiscount.getIsUniqueness());
        marketingDiscountCoupon.setIsDistribution(marketingDiscount.getIsDistribution());
        marketingDiscountCoupon.setGetRestrict(marketingDiscount.getGetRestrict());
         //平台渠道判断
        QueryWrapper<MarketingChannel> marketingChannelQueryWrapper=new QueryWrapper<>();
        marketingChannelQueryWrapper.eq("english_name",channelName);
        MarketingChannel marketingChannel=iMarketingChannelService.getOne(marketingChannelQueryWrapper);
        if(marketingChannel!=null) {
            marketingDiscountCoupon.setMarketingChannelId(marketingChannel.getId());
            marketingDiscountCoupon.setTheChannel(marketingChannel.getName());
        }else {
            result.setResult("9");
            result.error500("领券渠道不存在");
            return result;
        }


        //标准用券方式
        if(marketingDiscount.getVouchersWay().equals("0")){
            //优惠券的时间生成
            marketingDiscountCoupon.setStartTime(marketingDiscount.getStartTime());
            marketingDiscountCoupon.setEndTime(marketingDiscount.getEndTime());
        }

        //领券当日起
        if(marketingDiscount.getVouchersWay().equals("1")){
            //优惠券的时间生成
            Calendar calendar=Calendar.getInstance();
            marketingDiscountCoupon.setStartTime(calendar.getTime());

            if(marketingDiscount.getMonad().equals("天")){
                calendar.add(Calendar.DATE,marketingDiscount.getDisData().intValue());
            }
            if(marketingDiscount.getMonad().equals("周")){
                calendar.add(Calendar.WEEK_OF_MONTH,marketingDiscount.getDisData().intValue());
            }
            if(marketingDiscount.getMonad().equals("月")){
                calendar.add(Calendar.MONTH,marketingDiscount.getDisData().intValue());
            }

            marketingDiscountCoupon.setEndTime(calendar.getTime());
        }
        //领券次日起
        if(marketingDiscount.getVouchersWay().equals("2")){
            //优惠券的时间生成
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DATE,1);
            marketingDiscountCoupon.setStartTime(calendar.getTime());

            if(marketingDiscount.getMonad().equals("天")){
                calendar.add(Calendar.DATE,marketingDiscount.getDisData().intValue());
            }
            if(marketingDiscount.getMonad().equals("周")){
                calendar.add(Calendar.WEEK_OF_MONTH,marketingDiscount.getDisData().intValue());
            }
            if(marketingDiscount.getMonad().equals("月")){
                calendar.add(Calendar.MONTH,marketingDiscount.getDisData().intValue());
            }

            marketingDiscountCoupon.setEndTime(calendar.getTime());
        }

        if(new Date().getTime()>=marketingDiscountCoupon.getStartTime().getTime()&&new Date().getTime()<=marketingDiscountCoupon.getEndTime().getTime()) {
            //设置生效
            marketingDiscountCoupon.setStatus("1");
        }else{
            marketingDiscountCoupon.setStatus("0");
        }

        iMarketingDiscountCouponService.save(marketingDiscountCoupon);
        marketingDiscount.setReleasedQuantity(marketingDiscount.getReleasedQuantity().add(new BigDecimal(1)));
        iMarketingDiscountService.saveOrUpdate(marketingDiscount);
        //会员分享次数扣除
        memberList.setShareTimes(memberList.getShareTimes().subtract(new BigDecimal(1)));
        iMemberListService.saveOrUpdate(memberList);
        result.setResult("0");
        result.success("优惠券领取成功");
        return  result;
    }
}
