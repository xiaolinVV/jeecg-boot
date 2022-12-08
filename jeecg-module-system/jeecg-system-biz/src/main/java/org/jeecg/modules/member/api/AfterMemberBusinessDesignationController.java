package org.jeecg.modules.member.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingBusinessCapital;
import org.jeecg.modules.marketing.entity.MarketingBusinessDesignation;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingWelfarePaymentsSetting;
import org.jeecg.modules.marketing.service.IMarketingBusinessCapitalService;
import org.jeecg.modules.marketing.service.IMarketingBusinessDesignationService;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftBaseSettingService;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.member.entity.MemberBusinessDesignation;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberBusinessDesignationService;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("after/memberBusinessDesignation")
@Controller
@Slf4j
public class AfterMemberBusinessDesignationController {
    @Autowired
    private IMemberBusinessDesignationService iMemberBusinessDesignationService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IMarketingBusinessDesignationService iMarketingBusinessDesignationService;
    @Autowired
    private IMarketingBusinessCapitalService iMarketingBusinessCapitalService;
    @Autowired
    private IMarketingBusinessGiftBaseSettingService iMarketingBusinessGiftBaseSettingService;
    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    /**
     * 创业会员称号
     * @param memberId
     * @return
     */
    @RequestMapping("getMyMemberBusinessDesignation")
    @ResponseBody
    public Result<?> getMyMemberBusinessDesignation(@RequestAttribute(required = false, name = "memberId") String memberId) {
        HashMap<String, Object> map = new HashMap<>();
        MemberList memberList = iMemberListService.getById(memberId);

        map.put("headPortrait",memberList.getHeadPortrait());
        map.put("nickName", StringUtils.isBlank(memberList.getNickName())?memberList.getPhone():memberList.getNickName());

        MemberBusinessDesignation memberBusinessDesignation = iMemberBusinessDesignationService.getOne(new LambdaQueryWrapper<MemberBusinessDesignation>()
                .eq(MemberBusinessDesignation::getMemberListId, memberList.getId())
                .orderByDesc(MemberBusinessDesignation::getCreateTime)
                .last("limit 1")
        );
        //创业礼包设置
        MarketingBusinessGiftBaseSetting marketingBusinessGiftBaseSetting = iMarketingBusinessGiftBaseSettingService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftBaseSetting>()
                .eq(MarketingBusinessGiftBaseSetting::getDelFlag, "0")
                .orderByDesc(MarketingBusinessGiftBaseSetting::getCreateTime)
                .last("limit 1")
        );

        MarketingBusinessDesignation marketingBusinessDesignation = iMarketingBusinessDesignationService.getById(memberBusinessDesignation.getMarketingBusinessDesignationId());
        map.put("designationName",marketingBusinessDesignation.getDesignationName());
        map.put("ruleDescription",marketingBusinessDesignation.getRuleDescription());
        map.put("icon",marketingBusinessDesignation.getIcon());
        //当前人数
        long theNumber = iMemberBusinessDesignationService.count(new LambdaQueryWrapper<MemberBusinessDesignation>()
                .eq(MemberBusinessDesignation::getMarketingBusinessDesignationId, memberBusinessDesignation.getMarketingBusinessDesignationId()));
        map.put("theNumber",theNumber);
        //获取称号资金池
        MarketingBusinessCapital marketingBusinessCapital = iMarketingBusinessCapitalService.getOne(new LambdaQueryWrapper<MarketingBusinessCapital>()
                .eq(MarketingBusinessCapital::getMarketingBusinessDesignationId, marketingBusinessDesignation.getId())
                .orderByDesc(MarketingBusinessCapital::getCreateTime)
                .last("limit 1")
        );

        if (marketingBusinessGiftBaseSetting.getShareBonus().equals("0")){
            MarketingWelfarePaymentsSetting marketingWelfarePaymentsSetting = iMarketingWelfarePaymentsSettingService.getOne(new LambdaQueryWrapper<MarketingWelfarePaymentsSetting>()
                    .eq(MarketingWelfarePaymentsSetting::getDelFlag, "0")
                    .orderByDesc(MarketingWelfarePaymentsSetting::getCreateTime)
                    .last("limit 1")
            );

            map.put("bonusMoney",marketingBusinessCapital.getBalance()+marketingWelfarePaymentsSetting.getNickName());
            map.put("toDayBonusMoney",marketingBusinessCapital.getBalance().divide(new BigDecimal(theNumber),2, RoundingMode.DOWN)+marketingWelfarePaymentsSetting.getNickName());
            if (marketingBusinessCapital.getWhetherDividend().equals("0")){
                map.put("bonusQuota","无");
            }else {
                map.put("bonusQuota",memberBusinessDesignation.getAmountShare()+"/"+marketingBusinessCapital.getAmountShare()+marketingWelfarePaymentsSetting.getNickName());
            }
            List<Map<String, Object>> maps = iMarketingBusinessCapitalService.listMaps(new QueryWrapper<MarketingBusinessCapital>()
                    .select("capital_name as capitalName,balance")
                    .eq("del_flag", "0")
                    .eq("capital_type", "0")
                    .eq("is_view","1")
            );
            maps.forEach(ms->{
                ms.put("balance",ms.get("balance")+marketingWelfarePaymentsSetting.getNickName());
            });
            map.put("MarketingBusinessCapitalMap",maps);

        }else {
            //预留余额

        }
        map.put("repetitionMoney",marketingBusinessCapital.getWhetherComplex().equals("0")?"无":"￥"+marketingBusinessCapital.getInvestmentAmount());
        map.put("promotionCode",memberList.getPromotionCode());
        map.put("myTeam",memberBusinessDesignation.getTotalTeam()+"人");
        return Result.ok(map);
    }

    /**
     * 我的团队
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getMyGroup")
    @ResponseBody
    public Result<?> getMyGroup(@RequestAttribute(required = false, name = "memberId") String memberId,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        HashMap<String, Object> map = new HashMap<>();
        MemberList memberList = iMemberListService.getById(memberId);
        map.put("headPortrait",memberList.getHeadPortrait());
        map.put("nickName", StringUtils.isBlank(memberList.getNickName())?memberList.getPhone():memberList.getNickName());
        MemberBusinessDesignation memberBusinessDesignation = iMemberBusinessDesignationService.getOne(new LambdaQueryWrapper<MemberBusinessDesignation>()
                .eq(MemberBusinessDesignation::getMemberListId, memberList.getId())
                .orderByDesc(MemberBusinessDesignation::getCreateTime)
                .last("limit 1")
        );
        MarketingBusinessDesignation marketingBusinessDesignation = iMarketingBusinessDesignationService.getById(memberBusinessDesignation.getMarketingBusinessDesignationId());
        map.put("designationName",marketingBusinessDesignation.getDesignationName());
        map.put("ruleDescription",marketingBusinessDesignation.getRuleDescription());
        map.put("icon",marketingBusinessDesignation.getIcon());
        map.put("subordinateList",iMemberBusinessDesignationService.findMemberBusinessDesignationBySuperiorMemberId(new Page<Map<String, Object>>(pageNo, pageSize),memberId));
        return Result.ok(map);
    }

    /**
     * 根据id获取团队成员信息
     * @param memberId
     * @param id
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getGroupByMemberId")
    @ResponseBody
    public Result<?> getGroupByMemberId(@RequestAttribute(required = false, name = "memberId") String memberId,
                                        String id,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        if (StringUtils.isBlank(id)) {
            return Result.error("前端id未传递!");
        }
        if (id.equals(memberId)){
            return Result.error("前端id传递错误!");
        }
        MemberList memberList = iMemberListService.getById(id);
        if (memberList!=null){
            HashMap<String, Object> map = new HashMap<>();
            MemberBusinessDesignation memberBusinessDesignation = iMemberBusinessDesignationService.getOne(new LambdaQueryWrapper<MemberBusinessDesignation>()
                    .eq(MemberBusinessDesignation::getMemberListId, memberList.getId())
                    .orderByDesc(MemberBusinessDesignation::getCreateTime)
                    .last("limit 1")
            );
            map.put("isViewSuperior",memberBusinessDesignation.getTMemberId().equals(memberId)?"0":"1");
            map.put("headPortrait",memberList.getHeadPortrait());
            map.put("nickName", StringUtils.isBlank(memberList.getNickName())?memberList.getPhone():memberList.getNickName());
            MarketingBusinessDesignation marketingBusinessDesignation = iMarketingBusinessDesignationService.getById(memberBusinessDesignation.getMarketingBusinessDesignationId());
            map.put("designationName",marketingBusinessDesignation.getDesignationName());
            map.put("icon",marketingBusinessDesignation.getIcon());
            map.put("tMemberId",memberBusinessDesignation.getTMemberId());
            map.put("subordinateList",iMemberBusinessDesignationService.findMemberBusinessDesignationBySuperiorMemberId(new Page<Map<String, Object>>(pageNo, pageSize),memberList.getId()));
            return Result.ok(map);
        }else {
            return Result.error("id传递有误,未找到对应实体");
        }
    }
}
