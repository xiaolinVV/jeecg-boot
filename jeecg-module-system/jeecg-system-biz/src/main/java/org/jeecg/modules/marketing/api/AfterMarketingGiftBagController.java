package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingGiftBag;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecord;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftBaseSettingService;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordService;
import org.jeecg.modules.marketing.service.IMarketingGiftBagService;
import org.jeecg.modules.member.entity.*;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.order.utils.TotalPayUtils;
import org.jeecg.modules.store.entity.StoreFranchiser;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreFranchiserService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 礼包需要的API接口
 */
@RequestMapping("after/marketingGiftBag")
@Controller
@Slf4j
public class AfterMarketingGiftBagController {

    @Autowired
    private IMarketingGiftBagService iMarketingGiftBagService;

    @Autowired
    private IMarketingGiftBagRecordService iMarketingGiftBagRecordService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMemberGradeService iMemberGradeService;

    @Autowired
    private IMemberDesignationService iMemberDesignationService;

    @Autowired
    private IMemberDesignationGroupService iMemberDesignationGroupService;

    @Autowired
    private IMemberDesignationMemberListService iMemberDesignationMemberListService;

    @Autowired
    private TotalPayUtils totalPayUtils;

    @Autowired
    private IMarketingBusinessGiftBaseSettingService iMarketingBusinessGiftBaseSettingService;


    @Autowired
    private IStoreFranchiserService iStoreFranchiserService;


    @Autowired
    private IStoreManageService iStoreManageService;

    /**
     * 提交礼包的待支付订单
     *
     * @param id
     * @return
     */
    @RequestMapping("submitMarketingGiftBag")
    @ResponseBody
    public Result<?> submitMarketingGiftBag(String id,
                                                             @RequestParam(name = "tPhone",required = false,defaultValue = "") String tPhone,
                                                             @RequestParam(name = "marketingGiftBagRecordId",required = false,defaultValue = "") String marketingGiftBagRecordId,
                                                             @RequestHeader(defaultValue = "") String sysUserId,
                                                             @RequestHeader(defaultValue = "") String longitude,
                                                             @RequestHeader(defaultValue = "") String latitude,
                                                             @RequestHeader(defaultValue = "") String tMemberId,
                                                             @RequestAttribute("memberId") String memberId,
                                                             @RequestHeader("softModel") String softModel,
                                                             HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
        //参数校验
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }
        MarketingGiftBag marketingGiftBag=iMarketingGiftBagService.getById(id);
        if (marketingGiftBag.getRepertory().doubleValue()<=0){
            return result.error500("库存不足");
        }
        if (marketingGiftBag.getStatus().equals("0")){
            return result.error500("该礼包已停用");
        }

        if(StringUtils.isBlank(marketingGiftBagRecordId)) {

            //判断推广人信息是否存在
            if(StringUtils.isNotBlank(tPhone)){
                MemberList memberList=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>()
                        .eq(MemberList::getPhone,tPhone)
                        .orderByDesc(MemberList::getCreateTime)
                        .last("limit 1"));
                if(memberList==null){
                    return result.error500("找不到推广人，请查询推广人手机号码是否正确");
                }
                tMemberId=memberList.getId();
                if(tMemberId.equals(memberId)){
                    return result.error500("推荐人不可以是自己");
                }
            }

            //查询会员是否有待付款的礼包
            long count=iMarketingGiftBagRecordService.count(new LambdaQueryWrapper<MarketingGiftBagRecord>()
                    .eq(MarketingGiftBagRecord::getPayStatus,"1")
                    .eq(MarketingGiftBagRecord::getMemberListId,memberId)
                    .eq(MarketingGiftBagRecord::getMarketingGiftBagId,id)
                    .gt(MarketingGiftBagRecord::getResiduePayTimes,0));
            if(count!=0){
                return result.error500("该礼包存在一个未支付完成的礼包，请进入个人中心-我的礼包处理");
            }

            LambdaQueryWrapper<MarketingGiftBagRecord> marketingGiftBagRecordLambdaQueryWrapper = new LambdaQueryWrapper<MarketingGiftBagRecord>()
                    .eq(MarketingGiftBagRecord::getMemberListId, memberId)
                    .eq(MarketingGiftBagRecord::getPayStatus, "1");


            //购买次数限制
            marketingGiftBagRecordLambdaQueryWrapper.eq(MarketingGiftBagRecord::getMarketingGiftBagId, id);
            if (marketingGiftBag.getLimitTimes().intValue() <= iMarketingGiftBagRecordService.count(marketingGiftBagRecordLambdaQueryWrapper)) {
                result.error500("您的购买件数已达上限。");
                return result;
            }

            //判断是否为称号礼包: 称号创始人不能购买自己称号的礼包
            if (StringUtils.isNotBlank(marketingGiftBag.getMemberDesignationId())) {
                MemberDesignation memberDesignation = iMemberDesignationService.getById(marketingGiftBag.getMemberDesignationId());
                MemberDesignationGroup memberDesignationGroup = iMemberDesignationGroupService.getById(memberDesignation.getMemberDesignationGroupId());
                if (memberDesignationGroup.getMemberId().equals(memberId)) {
                    return result.error500("您是该团队的创始人，不允许购买自己的团队礼包。");
                }
            }


            //判断是否有经销商奖励
            if(marketingGiftBag.getDealerAwards().doubleValue()>0){
                if(StringUtils.isBlank(sysUserId)){
                   return Result.error("请选进入商户礼包进行购买");
                }
                if(StringUtils.isNotBlank(tMemberId)){
                    StoreManage storeManage=iStoreManageService.getStoreManageBySysUserId(sysUserId);
                    //获取本会员的经销商
                    StoreFranchiser storeFranchiser=iStoreFranchiserService.findStoreFranchiser(memberId,storeManage.getId());
                    if(storeFranchiser!=null){
                        StoreFranchiser storeFranchiser1=iStoreFranchiserService.findStoreFranchiser(tMemberId,storeManage.getId());
                        if(storeFranchiser1!=null){
                            if(!storeFranchiser1.getId().equals(storeFranchiser.getId())){
                                return Result.error("您已经加入了一个经销商团队不可再加入其它的团队");
                            }
                        }
                    }
                }
            }



        }





        result.setResult(totalPayUtils.payGiftBag(memberId,request,id,sysUserId,longitude,latitude,tMemberId,softModel,marketingGiftBagRecordId));
        result.success("生成待支付订单成功");
        return result;
    }

    /**
     * 获取礼包列表
     *
     * @param sysUserId
     * @param request
     * @return
     */
    @RequestMapping("getMarketingGiftBagList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> getMarketingGiftBagList(String isBuy,
                                                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                     String sysUserId,
                                                                     HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();

        Result<IPage<Map<String,Object>>> result=new Result<>();

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("memberId",memberId);
        paramObjectMap.put("sysUserId",sysUserId);
        paramObjectMap.put("isBuy",isBuy);

        result.setResult(iMarketingGiftBagService.getMarketingGiftBagList(page,paramObjectMap));

        result.success("查询礼包列表成功");
        return result;
    }


    /**
     * 根据礼包id获取详情
     * @param id
     * @return
     */
    @RequestMapping("findMarketingGiftBagInfo")
    @ResponseBody
    public Result<Map<String,Object>> findMarketingGiftBagInfo(String id,
                                                               @RequestAttribute(required = false,name = "memberId")String memberId){
        Result<Map<String,Object>> result=new Result<>();
        //参数校验
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }
        log.info(id);
        HashMap<String, Object> map = new HashMap<>();
        MarketingGiftBag marketingGiftBag = iMarketingGiftBagService.getById(id);
        if(marketingGiftBag==null){
            result.error500("礼包id不能为空！！！");
            return result;
        }
        map.put("id",marketingGiftBag.getId());
        map.put("coverPlan",marketingGiftBag.getCoverPlan());
        map.put("posters",marketingGiftBag.getPosters());
        map.put("giftDeals",marketingGiftBag.getGiftDeals());
        map.put("price",marketingGiftBag.getPrice());
        map.put("payTimes",marketingGiftBag.getPayTimes());
        MemberList memberList = iMemberListService.getById(memberId);
        //判断是否等级限制
        if (marketingGiftBag.getBuyLimit().contains("1")&&StringUtils.isNotBlank(marketingGiftBag.getBuyVipMemberGradeId())){
            if (marketingGiftBag.getBuyVipMemberGradeId().contains(memberList.getMemberGradeId())){
                map.put("isViewBuyVipMemberGradeName","0");
            }else {
                map.put("isViewBuyVipMemberGradeName","1");
                if (marketingGiftBag.getBuyVipMemberGradeId().contains(",")){
                    List<String> memberGradeIds = Arrays.asList(StringUtils.split(marketingGiftBag.getBuyVipMemberGradeId(), ","));
                    ArrayList<String> memberGradeNameList = new ArrayList<>();

                    memberGradeIds.forEach(mgs->{
                        MemberGrade memberGrade = iMemberGradeService.getById(mgs);
                        if (oConvertUtils.isNotEmpty(memberGrade)){
                            memberGradeNameList.add(memberGrade.getGradeName());
                        }
                    });
                    map.put("buyVipMemberGradeName","请先升级为("+memberGradeNameList+")会员");
                }else {
                    MemberGrade memberGrade = iMemberGradeService.getById(marketingGiftBag.getBuyVipMemberGradeId());
                    if (oConvertUtils.isNotEmpty(memberGrade)){
                     map.put("buyVipMemberGradeName","请先升级为("+memberGrade.getGradeName()+")会员");
                    }
                }
            }
        }else {
            map.put("isViewBuyVipMemberGradeName","0");
        }

        //判断是否有前置礼包
        if (marketingGiftBag.getIsPreposition().equals("1")&&StringUtils.isNotBlank(marketingGiftBag.getPrepositionMarketingGiftBag())){
            if (iMarketingGiftBagRecordService.count(new LambdaQueryWrapper<MarketingGiftBagRecord>()
                    .eq(MarketingGiftBagRecord::getDelFlag,"0")
                    .eq(MarketingGiftBagRecord::getMemberListId,memberList.getId())
                    .eq(MarketingGiftBagRecord::getMarketingGiftBagId,marketingGiftBag.getPrepositionMarketingGiftBag())
                    .eq(MarketingGiftBagRecord::getPayStatus,"1")
            )>0){
                map.put("isViewprepositionName","0");
            }else {
                MarketingGiftBag giftBag = iMarketingGiftBagService.getById(marketingGiftBag.getPrepositionMarketingGiftBag());
                if (oConvertUtils.isNotEmpty(giftBag)){
                    map.put("isViewprepositionName","1");
                    map.put("prepositionName",giftBag.getGiftName());
                    map.put("prepositionMarketingGiftBag",giftBag.getId());
                }else {
                    map.put("isViewprepositionName","0");
                }
            }

        }else {
            map.put("isViewprepositionName","0");
        }
        result.setResult(map);
        result.success("获取礼包详情成功");
        return result;
    }
    @RequestMapping("isViewShare")
    @ResponseBody
    public Result<?> isViewShare(String marketingGiftBagId,
                                 HttpServletRequest request){
        String memberId = request.getAttribute("memberId").toString();
        if (StringUtils.isBlank(marketingGiftBagId)){
            return Result.error("礼包id未传递");
        }

        HashMap<String, Object> map = new HashMap<>();

        MarketingGiftBag marketingGiftBag = iMarketingGiftBagService.getById(marketingGiftBagId);
        if(marketingGiftBag==null){
            return Result.error("礼包id不存在");
        }
        if (StringUtils.isNotBlank(marketingGiftBag.getMemberDesignationId())){

            MemberDesignation memberDesignation = iMemberDesignationService.getById(marketingGiftBag.getMemberDesignationId());

            MemberDesignationGroup memberDesignationGroup = iMemberDesignationGroupService.getById(memberDesignation.getMemberDesignationGroupId());

            LambdaQueryWrapper<MemberDesignationMemberList> memberDesignationMemberListLambdaQueryWrapper = new LambdaQueryWrapper<MemberDesignationMemberList>()
                    .eq(MemberDesignationMemberList::getDelFlag, "0")
                    .eq(MemberDesignationMemberList::getMemberListId, memberId)
                    .eq(MemberDesignationMemberList::getMemberDesignationGroupId, memberDesignationGroup.getId());
            if (iMemberDesignationMemberListService.count(memberDesignationMemberListLambdaQueryWrapper)>0){
                map.put("status","1");
            }else {
                map.put("status","0");
                map.put("value","您尚未加入该团队，未购买过该团队的礼包，无该团队的礼包分享权限。");
            }
        }else {
            map.put("status","1");

        }
        return Result.ok(map);
    }

    /**
     * 是否展示创业礼包
     * @param softModel
     * @return
     */
    @RequestMapping("isViewGiftHeader")
    @ResponseBody
    public Result<?> isViewGiftHeader(@RequestHeader("softModel") String softModel){
        MarketingBusinessGiftBaseSetting marketingBusinessGiftBaseSetting = iMarketingBusinessGiftBaseSettingService.getOne(new LambdaQueryWrapper<MarketingBusinessGiftBaseSetting>()
                .eq(MarketingBusinessGiftBaseSetting::getDelFlag, "0")
                .orderByDesc(MarketingBusinessGiftBaseSetting::getCreateTime)
                .last("limit 1")
        );
        HashMap<String, Object> resultMap = new HashMap<>();
        //判断任务入口显隐
        if (marketingBusinessGiftBaseSetting !=null){
            if (marketingBusinessGiftBaseSetting.getStatus().equals("0")){
                resultMap.put("isViewGiftHeader","0");
            }else {
                if (marketingBusinessGiftBaseSetting.getPointsDisplay().equals("0")){
                    resultMap.put("isViewGiftHeader","1");
                    resultMap.put("businessGiftName",marketingBusinessGiftBaseSetting.getAnotherName());
                }else if (marketingBusinessGiftBaseSetting.getPointsDisplay().equals("1")){
                    if (softModel.equals("0")){
                        resultMap.put("isViewGiftHeader","1");
                        resultMap.put("businessGiftName",marketingBusinessGiftBaseSetting.getAnotherName());
                    }else {
                        resultMap.put("isViewGiftHeader","0");
                    }
                }else {
                    if (softModel.equals("1")||softModel.equals("2")){
                        resultMap.put("isViewGiftHeader","1");
                        resultMap.put("businessGiftName",marketingBusinessGiftBaseSetting.getAnotherName());
                    }else {
                        resultMap.put("isViewGiftHeader","0");
                    }
                }
            }
        }else {
            resultMap.put("isViewGiftHeader","0");
        }
        return Result.ok(resultMap);
    }


}
