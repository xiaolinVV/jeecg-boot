package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftList;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftRecord;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftListService;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftRecordService;
import org.jeecg.modules.member.entity.MemberBusinessDesignation;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberBusinessDesignationService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("after/marketingBusinessGiftList")
@Controller
@Slf4j
public class AfterMarketingBusinessGiftListController {
    @Autowired
    private IMarketingBusinessGiftListService iMarketingBusinessGiftListService;
    @Autowired
    private IMemberBusinessDesignationService iMemberBusinessDesignationService;
    @Autowired
    private IMemberShippingAddressService iMemberShippingAddressService;
    @Autowired
    private IMarketingBusinessGiftRecordService iMarketingBusinessGiftRecordService;


    @Autowired
    private IMemberListService iMemberListService;
    /**
     * 创业礼包列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingBusinessGiftList")
    @ResponseBody
    public Result<?> findMarketingBusinessGiftList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        return Result.ok(iMarketingBusinessGiftListService.findMarketingBusinessGiftList(new Page<Map<String, Object>>(pageNo, pageSize)));
    }

    /**
     * 创业礼包详情
     * @param memberId
     * @param id
     * @return
     */
    @RequestMapping("findMarketingBusinessGiftdetails")
    @ResponseBody
    public Result<?> findMarketingBusinessGiftdetails(@RequestAttribute(required = false, name = "memberId") String memberId,
                                                      String id){
        if (StringUtils.isBlank(id)) {
            return Result.error("前端id未传递!");
        }
        MarketingBusinessGiftList marketingBusinessGiftList = iMarketingBusinessGiftListService.getById(id);
        if (marketingBusinessGiftList!=null){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",marketingBusinessGiftList.getId());
            map.put("giftName",marketingBusinessGiftList.getGiftName());
            map.put("salesPrice",marketingBusinessGiftList.getSalesPrice());
            map.put("mainPicture",marketingBusinessGiftList.getMainPicture());
            map.put("detailsFigure",marketingBusinessGiftList.getDetailsFigure());
            map.put("coverPlan",marketingBusinessGiftList.getCoverPlan());
            map.put("posters",marketingBusinessGiftList.getPosters());
            map.put("repertory",marketingBusinessGiftList.getRepertory());
            map.put("isBuyGift",iMemberBusinessDesignationService.count(new LambdaQueryWrapper<MemberBusinessDesignation>().eq(MemberBusinessDesignation::getMemberListId,memberId))>0?"1":"0");
            return Result.ok(map);
        }else {
            return Result.error("id传递有误,未找到对应实体");
        }
    }
    /**
     *
     * 确认订单
     *
     * @param promotionCode
     * @return
     */
    @RequestMapping("confirmOrder")
    @ResponseBody
    public Result<?> confirmOrder(@RequestParam(name = "promotionCode" ,required = false,defaultValue = "") String promotionCode,
                                  String marketingBusinessGiftListId,
                                  @RequestParam(name = "memberShippingAddressId" ,required = false,defaultValue = "") String memberShippingAddressId,
                                  @RequestAttribute(value = "memberId",required = false) String memberId) {
        //参数校验
        if(StringUtils.isBlank(marketingBusinessGiftListId)){
            return Result.error("创业礼包id不能为空");
        }

        //判断推广码是否买过创业礼包
        if (StringUtils.isNotBlank(promotionCode)) {
            promotionCode = StringUtils.trim(promotionCode);
            MemberList memberList = iMemberListService.getOne(new LambdaQueryWrapper<MemberList>().eq(MemberList::getPromotionCode, promotionCode));
            if (memberList == null) {
                return Result.error("推广码不存在会员");
            }
            long memberBusinessDesignationCount=iMemberBusinessDesignationService.count(new LambdaQueryWrapper<MemberBusinessDesignation>().eq(MemberBusinessDesignation::getMemberListId,memberList.getId()));
            if(memberBusinessDesignationCount==0){
                return Result.error("推广的会员必须购买过创业礼包");
            }
            if(memberList.getId().equals(memberId)){
                return Result.error("不可使用自己的推广码");
            }
        }
        Map<String,Object> resultMap= Maps.newHashMap();

        MemberShippingAddress memberShippingAddress=null;

        if(StringUtils.isBlank(memberShippingAddressId)){
            memberShippingAddress= iMemberShippingAddressService.getOne(new LambdaQueryWrapper<MemberShippingAddress>()
                    .eq(MemberShippingAddress::getMemberListId,memberId)
                    .eq(MemberShippingAddress::getIsDefault,"1")
                    .last("limit 1"));
        }else{
            memberShippingAddress=iMemberShippingAddressService.getById(memberShippingAddressId);
        }
        //收货地址数据
        Map<String, Object> memberShippingAddressMaps = Maps.newHashMap();
        if(memberShippingAddress!=null) {
            memberShippingAddressMaps.put("isDefault",memberShippingAddress.getIsDefault());
            memberShippingAddressMaps.put("phone",memberShippingAddress.getPhone());
            memberShippingAddressMaps.put("houseNumber",memberShippingAddress.getHouseNumber());
            memberShippingAddressMaps.put("areaAddress",memberShippingAddress.getAreaAddress());
            memberShippingAddressMaps.put("id",memberShippingAddress.getId());
            memberShippingAddressMaps.put("linkman",memberShippingAddress.getLinkman());
            memberShippingAddressMaps.put("sysAreaId",memberShippingAddress.getSysAreaId());
            memberShippingAddressMaps.put("areaExplan",memberShippingAddress.getAreaExplan());
            resultMap.put("memberShippingAddressId",memberShippingAddress.getId());
        }
        MarketingBusinessGiftList marketingBusinessGiftList=iMarketingBusinessGiftListService.getById(marketingBusinessGiftListId);
        resultMap.put("memberShippingAddress",memberShippingAddressMaps);
        resultMap.put("giftName",marketingBusinessGiftList.getGiftName());
        resultMap.put("mainPicture",marketingBusinessGiftList.getMainPicture());
        resultMap.put("price",marketingBusinessGiftList.getSalesPrice());
        resultMap.put("freight",0);
        resultMap.put("promotionCode",promotionCode);
        resultMap.put("totalPrice",marketingBusinessGiftList.getSalesPrice());
        resultMap.put("marketingBusinessGiftListId",marketingBusinessGiftListId);
        resultMap.put("memberListId",memberId);
        return Result.ok(resultMap);
    }

    /**
     * 我的创业礼包列表
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getIsMarketingBusinessGiftList")
    @ResponseBody
    public Result<?> getIsMarketingBusinessGiftList(@RequestAttribute(required = false, name = "memberId") String memberId,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        return Result.ok(iMarketingBusinessGiftListService.getIsMarketingBusinessGiftList(new Page<Map<String, Object>>(pageNo, pageSize),memberId));
    }

    /**
     * 我的创业礼包详情
     * @param id
     * @return
     */
    @RequestMapping("getIsMarketingBusinessGiftdetails")
    @ResponseBody
    public Result<?> getIsMarketingBusinessGiftdetails(String id){
        if (StringUtils.isBlank(id)) {
            return Result.error("前端id未传递!");
        }
        MarketingBusinessGiftRecord marketingBusinessGiftRecord = iMarketingBusinessGiftRecordService.getById(id);
        if (marketingBusinessGiftRecord!=null){
            MarketingBusinessGiftList marketingBusinessGiftList = iMarketingBusinessGiftListService.getById(marketingBusinessGiftRecord.getMarketingBusinessGiftListId());
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",marketingBusinessGiftList.getId());
            map.put("giftName",marketingBusinessGiftList.getGiftName());
            map.put("salesPrice",marketingBusinessGiftList.getSalesPrice());
            map.put("mainPicture",marketingBusinessGiftList.getMainPicture());
            map.put("detailsFigure",marketingBusinessGiftList.getDetailsFigure());
            map.put("coverPlan",marketingBusinessGiftList.getCoverPlan());
            map.put("posters",marketingBusinessGiftList.getPosters());
            map.put("repertory",marketingBusinessGiftList.getRepertory());
            return Result.ok(map);
        }else {
            return Result.error("id传递有误,未找到对应实体");
        }
    }
}
