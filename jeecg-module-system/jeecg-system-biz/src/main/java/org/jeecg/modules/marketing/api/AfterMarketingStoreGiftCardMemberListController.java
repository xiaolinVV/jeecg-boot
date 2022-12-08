package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardList;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardMemberList;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardListService;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardMemberListService;
import org.jeecg.modules.store.api.FrontStoreManageController;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 会员礼品卡
 */
@Controller
@RequestMapping("after/marketingStoreGiftCardMemberList")
public class AfterMarketingStoreGiftCardMemberListController {

    @Autowired
    private IMarketingStoreGiftCardMemberListService iMarketingStoreGiftCardMemberListService;

    @Autowired
    private FrontStoreManageController frontStoreManageController;

    @Autowired
    private IMarketingStoreGiftCardListService iMarketingStoreGiftCardListService;

    @Autowired
    private IStoreManageService iStoreManageService;


    /**
     * 领回礼品卡
     * @param id
     * @return
     */
    @RequestMapping("getGiftCardBack")
    @ResponseBody
    public Result<?> getGiftCardBack(String id){
        MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=iMarketingStoreGiftCardMemberListService.getById(id);
        if(marketingStoreGiftCardMemberList.getIfGet().equals("1")){
            return Result.error("礼品卡已送出");
        }
        marketingStoreGiftCardMemberList.setStatus("1");
        marketingStoreGiftCardMemberList.setWaysObtain("0");
        marketingStoreGiftCardMemberList.setTMemberId(null);
        if(iMarketingStoreGiftCardMemberListService.saveOrUpdate(marketingStoreGiftCardMemberList)){
            return Result.ok();
        }
       return Result.error("礼品卡领回失败");
    }


    /**
     *
     * 根据会员id查询礼品卡
     *
     * @param memberId
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getByMemberId")
    @ResponseBody
    public Result<?> getByMemberId(@RequestAttribute(required = false,name = "memberId")String memberId,
                                   String status,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("memberId",memberId);
        paramMap.put("status",status);
        return Result.ok(iMarketingStoreGiftCardMemberListService.getByMemberId(new Page<>(pageNo,pageSize),paramMap));
    }


    /**
     * 根据id查询会员礼拼卡信息
     *
     * @param marketingStoreGiftCardMemberListId
     * @return
     */
    @RequestMapping("getMarketingStoreGiftCardMemberListById")
    @ResponseBody
    public Result<?> getMarketingStoreGiftCardMemberListById(String marketingStoreGiftCardMemberListId,
                                                             @RequestHeader(defaultValue = "") String longitude,
                                                             @RequestHeader(defaultValue = "") String latitude){
        Map<String,Object> resultMap=Maps.newHashMap();
        //参数校验
        if(StringUtils.isBlank(marketingStoreGiftCardMemberListId)){
            return Result.error("会员礼品卡id不能为空");
        }
        //获取礼品卡信息
        MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=iMarketingStoreGiftCardMemberListService.getById(marketingStoreGiftCardMemberListId);
        MarketingStoreGiftCardList marketingStoreGiftCardList=iMarketingStoreGiftCardListService.getById(marketingStoreGiftCardMemberList.getMarketingStoreGiftCardListId());
        if(marketingStoreGiftCardMemberList==null){
            return Result.error("会员礼品卡不存在");
        }
        resultMap.put("id",marketingStoreGiftCardMemberList.getId());
        resultMap.put("carName",marketingStoreGiftCardMemberList.getCarName());
        resultMap.put("serialNumber",marketingStoreGiftCardMemberList.getSerialNumber());
        resultMap.put("denomination",marketingStoreGiftCardMemberList.getDenomination());
        resultMap.put("startTime", DateUtils.formatDate(marketingStoreGiftCardMemberList.getStartTime()));
        resultMap.put("endTime",DateUtils.formatDate(marketingStoreGiftCardMemberList.getEndTime()));
        resultMap.put("carExplain",marketingStoreGiftCardList.getCarExplain());
        resultMap.put("status",marketingStoreGiftCardMemberList.getStatus());
        resultMap.put("ifFet",marketingStoreGiftCardMemberList.getIfGet());

        //获取店铺信息

        resultMap.put("storeMap",frontStoreManageController.index(marketingStoreGiftCardMemberList.getStoreManageId(),longitude,latitude,"").getResult());

        //获取商品信息

        resultMap.put("goodMap",iMarketingStoreGiftCardListService.getSelectGoods(new Page<>(1,6),marketingStoreGiftCardList.getId()).getRecords());

        return Result.ok(resultMap);
    }


    /**
     * 获取礼品卡店铺第一分类
     *
     * @param marketingStoreGiftCardMemberListId
     * @return
     */
    @RequestMapping("getGoodTypeOne")
    @ResponseBody
    public Result<?> getGoodTypeOne(String marketingStoreGiftCardMemberListId){
        //参数校验
        if(StringUtils.isBlank(marketingStoreGiftCardMemberListId)){
            return Result.error("会员礼品卡id不能为空");
        }
        MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=iMarketingStoreGiftCardMemberListService.getById(marketingStoreGiftCardMemberListId);
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("marketingStoreGiftCardListId",marketingStoreGiftCardMemberList.getMarketingStoreGiftCardListId());
        paramMap.put("sysUserId",iStoreManageService.getById(marketingStoreGiftCardMemberList.getStoreManageId()).getSysUserId());
        return Result.ok(iMarketingStoreGiftCardMemberListService.getGoodTypeOne(paramMap));
    }

    /**
     * 获取礼品卡店铺第一分类
     *
     * @param marketingStoreGiftCardMemberListId
     * @return
     */
    @RequestMapping("getGoodTypeTwo")
    @ResponseBody
    public Result<?> getGoodTypeTwo(String marketingStoreGiftCardMemberListId,String goodTypeId){
        //参数校验
        if(StringUtils.isBlank(marketingStoreGiftCardMemberListId)){
            return Result.error("会员礼品卡id不能为空");
        }
        if(StringUtils.isBlank(goodTypeId)){
            return Result.error("商品分类id不能为空");
        }
        MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=iMarketingStoreGiftCardMemberListService.getById(marketingStoreGiftCardMemberListId);
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("marketingStoreGiftCardListId",marketingStoreGiftCardMemberList.getMarketingStoreGiftCardListId());
        paramMap.put("sysUserId",iStoreManageService.getById(marketingStoreGiftCardMemberList.getStoreManageId()).getSysUserId());
        paramMap.put("parentId",goodTypeId);
        return Result.ok(iMarketingStoreGiftCardMemberListService.getGoodTypeTwo(paramMap));
    }

    /**
     * 获取商品列表
     *
     * @return
     */
    @RequestMapping("getGoodList")
    @ResponseBody
    public Result<?> getGoodList(String marketingStoreGiftCardMemberListId,String goodTypeId,
                                 @RequestAttribute(required = false,name = "memberId")String memberId,
                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        //参数校验
        if(StringUtils.isBlank(marketingStoreGiftCardMemberListId)){
            return Result.error("会员礼品卡id不能为空");
        }
        if(StringUtils.isBlank(goodTypeId)){
            return Result.error("商品分类id不能为空");
        }
        MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=iMarketingStoreGiftCardMemberListService.getById(marketingStoreGiftCardMemberListId);
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("goodTypeId",goodTypeId);
        paramMap.put("marketingStoreGiftCardListId",marketingStoreGiftCardMemberList.getMarketingStoreGiftCardListId());
        paramMap.put("memberId",memberId);
        paramMap.put("marketingStoreGiftCardMemberListId",marketingStoreGiftCardMemberListId);
        return Result.ok(iMarketingStoreGiftCardMemberListService.getGoodList(new Page<>(pageNo,pageSize),paramMap));
    }


    /**
     * 会员礼品卡赠送
     *
     * @param marketingStoreGiftCardMemberListId
     * @param memberId
     * @return
     */
    @RequestMapping("giveAway")
    @ResponseBody
    public Result<?> giveAway(String marketingStoreGiftCardMemberListId,
                              @RequestAttribute(required = false,name = "memberId")String memberId){
        //参数校验
        if(StringUtils.isBlank(marketingStoreGiftCardMemberListId)){
            return Result.error("会员礼品卡id不能为空");
        }
        MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=iMarketingStoreGiftCardMemberListService.getById(marketingStoreGiftCardMemberListId);
        if(!memberId.equals(marketingStoreGiftCardMemberList.getMemberListId())){
            return Result.error("非本人礼品卡不可赠送");
        }
        marketingStoreGiftCardMemberList.setStatus("2");
        iMarketingStoreGiftCardMemberListService.saveOrUpdate(marketingStoreGiftCardMemberList);
        return Result.ok();
    }

    /**
     * 获得
     * @param marketingStoreGiftCardMemberListId
     * @param tMemberId
     * @param memberId
     * @return
     */
    @RequestMapping("acquisition")
    @ResponseBody
    public Result<?> acquisition(String marketingStoreGiftCardMemberListId,
                                 String tMemberId,
                                 @RequestAttribute(required = false,name = "memberId")String memberId){
        //参数校验
        if(StringUtils.isBlank(marketingStoreGiftCardMemberListId)){
            return Result.error("会员礼品卡id不能为空");
        }
        if(StringUtils.isBlank(tMemberId)){
            return Result.error("赠送人不能为空");
        }
        MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=iMarketingStoreGiftCardMemberListService.getById(marketingStoreGiftCardMemberListId);
        if(marketingStoreGiftCardMemberList.getIfGet().equals("1")){
            return Result.error("礼品卡已被领取");
        }
        marketingStoreGiftCardMemberList.setIfGet("1");
        iMarketingStoreGiftCardMemberListService.saveOrUpdate(marketingStoreGiftCardMemberList);
        if(marketingStoreGiftCardMemberList==null){
            return Result.error("礼品卡不存在");
        }
        //判断会员卡是否已被领取

        marketingStoreGiftCardMemberList.setStatus("1");
        marketingStoreGiftCardMemberList.setId(null);
        marketingStoreGiftCardMemberList.setMemberListId(memberId);
        marketingStoreGiftCardMemberList.setWaysObtain("1");
        marketingStoreGiftCardMemberList.setTMemberId(tMemberId);
        marketingStoreGiftCardMemberList.setIfGet("0");
        iMarketingStoreGiftCardMemberListService.saveOrUpdate(marketingStoreGiftCardMemberList);
        return Result.ok();
    }

}
