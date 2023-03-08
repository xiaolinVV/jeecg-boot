package org.jeecg.modules.marketing.api;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 会员礼品卡
 */
@Slf4j
@RestController
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
     *
     * 根据会员id查询礼品卡（转入余额只用）
     *
     * @param memberId
     * @param status
     * @return
     */
    @RequestMapping("getByMemberIdBalance")
    @ResponseBody
    public Result<?> getByMemberIdBalance(@RequestAttribute(required = false,name = "memberId")String memberId,
                                   String status,String storeManageId){
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("memberId",memberId);
        paramMap.put("status",status);
        paramMap.put("storeManageId",storeManageId);
        return Result.ok(iMarketingStoreGiftCardMemberListService.getByMemberId(paramMap));
    }


    /**
     * 余额转入
     *
     * @param srcId
     * @param destId
     * @param balance
     * @return
     */
    @PostMapping("balanceToGiftCard")
    public Result<?> balanceToGiftCard(String srcId, String destId, BigDecimal balance){
        //参数校验
        if(StringUtils.isBlank(srcId)){
            return Result.error("原卡信息有问题");
        }
        if(StringUtils.isBlank(destId)){
            return Result.error("请选择转入的礼品卡");
        }
        if(balance.doubleValue()<=0){
            return Result.error("转入余额不能小于0");
        }
        MarketingStoreGiftCardMemberList srcMarketingStoreGiftCardMemberList=iMarketingStoreGiftCardMemberListService.getById(srcId);
        if(srcMarketingStoreGiftCardMemberList==null){
            return Result.error("系统没有找到原卡");
        }
        MarketingStoreGiftCardMemberList destMarketingStoreGiftCardMemberList=iMarketingStoreGiftCardMemberListService.getById(destId);
        if(srcMarketingStoreGiftCardMemberList==null){
            return Result.error("系统没有找到原卡");
        }
        if(destMarketingStoreGiftCardMemberList==null){
            return Result.error("系统没有找到目标卡");
        }
        if(destMarketingStoreGiftCardMemberList.getDenomination().doubleValue()<balance.doubleValue()){
            return Result.error("转入卡的余额不足");
        }
        iMarketingStoreGiftCardMemberListService.subtractBlance(destId,balance,srcId,"3");
        iMarketingStoreGiftCardMemberListService.addBlance(srcId,balance,destId,"4");
        return Result.ok("转入完成");
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
        log.info("根据id查询会员礼拼卡信息:{}",marketingStoreGiftCardMemberListId);
        Map<String,Object> resultMap=Maps.newHashMap();
        //参数校验
        if(StringUtils.isBlank(marketingStoreGiftCardMemberListId)){
            return Result.error("会员礼品卡id不能为空");
        }
        //获取礼品卡信息
        MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=iMarketingStoreGiftCardMemberListService.getById(marketingStoreGiftCardMemberListId);
        if(marketingStoreGiftCardMemberList==null){
            return Result.error("会员礼品卡不存在");
        }
        MarketingStoreGiftCardList marketingStoreGiftCardList=iMarketingStoreGiftCardListService.getById(marketingStoreGiftCardMemberList.getMarketingStoreGiftCardListId());
        resultMap.put("id",marketingStoreGiftCardMemberList.getId());
        resultMap.put("carName",marketingStoreGiftCardMemberList.getCarName());
        resultMap.put("serialNumber",marketingStoreGiftCardMemberList.getSerialNumber());
        resultMap.put("denomination",marketingStoreGiftCardMemberList.getDenomination());
        resultMap.put("startTime", DateUtils.formatDate(marketingStoreGiftCardMemberList.getStartTime()));
        resultMap.put("endTime",DateUtils.formatDate(marketingStoreGiftCardMemberList.getEndTime()));
        resultMap.put("carExplain", StrUtil.emptyIfNull(marketingStoreGiftCardList.getCarExplain()));
        resultMap.put("status",marketingStoreGiftCardMemberList.getStatus());
        resultMap.put("ifFet",marketingStoreGiftCardMemberList.getIfGet());
        resultMap.put("storeManageId",marketingStoreGiftCardMemberList.getStoreManageId());
        //获取店铺信息

        resultMap.put("storeMap",frontStoreManageController.index(marketingStoreGiftCardMemberList.getStoreManageId(),longitude,latitude,"","").getResult());

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
        log.info("领取礼品卡，会员礼品卡id：{},赠送人id:{},当前会员id:{}",marketingStoreGiftCardMemberListId,tMemberId,memberId);
        //参数校验
        if(StringUtils.isBlank(marketingStoreGiftCardMemberListId)){
            return Result.error("会员礼品卡id不能为空");
        }
        if(StringUtils.isBlank(tMemberId)){
            return Result.error("赠送人不能为空");
        }
        MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=iMarketingStoreGiftCardMemberListService.getById(marketingStoreGiftCardMemberListId);
        if(marketingStoreGiftCardMemberList==null){
            return Result.error("礼品卡不存在");
        }
        //判断会员卡是否已被领取,未被领取则更新为已被领取
        if(marketingStoreGiftCardMemberList.getIfGet().equals("1")){
            return Result.error("礼品卡已被领取");
        }
        marketingStoreGiftCardMemberList.setIfGet("1");
        iMarketingStoreGiftCardMemberListService.saveOrUpdate(marketingStoreGiftCardMemberList);

        //未当前领取人新增一条礼品卡记录
        marketingStoreGiftCardMemberList.setStatus("1");
        marketingStoreGiftCardMemberList.setId(null);
        marketingStoreGiftCardMemberList.setMemberListId(memberId);
        marketingStoreGiftCardMemberList.setWaysObtain("1");
        marketingStoreGiftCardMemberList.setTMemberId(tMemberId);
        marketingStoreGiftCardMemberList.setIfGet("0");
        marketingStoreGiftCardMemberList.setGetTime(new Date());
        marketingStoreGiftCardMemberList.setCreateTime(new Date());

        iMarketingStoreGiftCardMemberListService.save(marketingStoreGiftCardMemberList);
        return Result.ok();
    }

}
