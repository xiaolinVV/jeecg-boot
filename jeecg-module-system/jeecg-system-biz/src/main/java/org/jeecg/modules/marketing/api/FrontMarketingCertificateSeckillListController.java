package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 限时抢券
 * @Author: jeecg-boot
 * @Date: 2021-03-29
 * @Version: V1.0
 */
@Slf4j
@RequestMapping("front/marketingCertificateSeckillList")
@Controller
public class FrontMarketingCertificateSeckillListController {
    @Autowired
    private IMarketingCertificateSeckillListService iMarketingCertificateSeckillListService;
    @Autowired
    private IMarketingCertificateSeckillBaseSettingService iMarketingCertificateSeckillBaseSettingService;
    @Autowired
    private IMarketingCertificateSeckillRecordService iMarketingCertificateSeckillRecordService;
    @Autowired
    private IMarketingCertificateSeckillActivityListService iMarketingCertificateSeckillActivityListService;
    @Autowired
    private IMarketingCertificateService iMarketingCertificateService;
    @Autowired
    private IMarketingCertificateGoodService iMarketingCertificateGoodService;
    @Autowired
    private IMarketingCertificateStoreService iMarketingCertificateStoreService;
    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private IMarketingCertificateRecordService iMarketingCertificateRecordService;

    /**
     * 限时抢券列表
     * @param marketingCertificateSeckillActivityListId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("pageListByMarketingCertificateSeckillActivityListId")
    @ResponseBody
    public Result<?> pageListByMarketingCertificateSeckillActivityListId(String marketingCertificateSeckillActivityListId,
                                                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        if (StringUtils.isBlank(marketingCertificateSeckillActivityListId)) {
            return Result.error("活动id为空");
        }
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
        return Result.ok(iMarketingCertificateSeckillListService.pageListByMarketingCertificateSeckillActivityListId(page, marketingCertificateSeckillActivityListId));
    }

    /**
     * 限时抢券头部信息
     * @param marketingCertificateSeckillActivityListId
     * @return
     */
    @RequestMapping("headData")
    @ResponseBody
    public Result<?> headData(String marketingCertificateSeckillActivityListId) {
        if (StringUtils.isBlank(marketingCertificateSeckillActivityListId)) {
            return Result.error("活动id为空");
        }
        MarketingCertificateSeckillBaseSetting marketingCertificateSeckillBaseSetting = iMarketingCertificateSeckillBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateSeckillBaseSetting>()
                .eq(MarketingCertificateSeckillBaseSetting::getDelFlag, "0"));
        HashMap<String, Object> map = new HashMap<>();
        MarketingCertificateSeckillActivityList marketingCertificateSeckillActivityList = iMarketingCertificateSeckillActivityListService.getById(marketingCertificateSeckillActivityListId);
        Map<String, Object> info = iMarketingCertificateSeckillActivityListService.getInfo(marketingCertificateSeckillActivityList.getId());
        map.put("time",info.get("surplusTime"));
        map.put("status", marketingCertificateSeckillActivityList.getStatus());
        map.put("ruleDescription", marketingCertificateSeckillBaseSetting.getRuleDescription());
        //活动别名
        map.put("anotherName",marketingCertificateSeckillBaseSetting.getAnotherName());
        //活动标签
        map.put("label",marketingCertificateSeckillBaseSetting.getLabel());
        //分享图
        map.put("coverPlan",marketingCertificateSeckillBaseSetting.getCoverPlan());
        //海报图
        map.put("posters",marketingCertificateSeckillBaseSetting.getPosters());
        map.put("marketingCertificateSeckillRecordList", iMarketingCertificateSeckillRecordService.dataList(marketingCertificateSeckillActivityListId));
        return Result.ok(map);
    }

    /**
     * 限时抢券详情
     * @param id
     * @return
     */
    @RequestMapping("findMarketingCertificateSeckillListById")
    @ResponseBody
    public Result<?> findMarketingCertificateSeckillListById(String id) {
        if (StringUtils.isBlank(id)) {
            return Result.error("限时抢券id为空");
        }
        HashMap<String, Object> map = new HashMap<>();
        //获取限时抢券
        MarketingCertificateSeckillList marketingCertificateSeckillList = iMarketingCertificateSeckillListService.getById(id);

        MarketingCertificateSeckillBaseSetting marketingCertificateSeckillBaseSetting = iMarketingCertificateSeckillBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateSeckillBaseSetting>()
                .eq(MarketingCertificateSeckillBaseSetting::getDelFlag, "0")
        );
        //兑换券id
        map.put("marketingCertificateId",marketingCertificateSeckillList.getMarketingCertificateId());
        //活动别名
        map.put("anotherName",marketingCertificateSeckillBaseSetting.getAnotherName());
        //活动标签
        map.put("label",marketingCertificateSeckillBaseSetting.getLabel());
        //限时抢券id
        map.put("id",marketingCertificateSeckillList.getId());
        //活动价
        map.put("activityPrice", marketingCertificateSeckillList.getActivityPrice());
        //获取限时活动
        MarketingCertificateSeckillActivityList marketingCertificateSeckillActivityList = iMarketingCertificateSeckillActivityListService.getById(marketingCertificateSeckillList.getMarketingCertificateSeckillActivityListId());
        //活动状态
        map.put("status", marketingCertificateSeckillActivityList.getStatus());
        //启用停用状态；0：启用；1：停用
        if (marketingCertificateSeckillActivityList.getDelFlag().equals("0")){
            if (marketingCertificateSeckillActivityList.getOnOffState().equals("1")){
                if (marketingCertificateSeckillList.getDelFlag().equals("0")){
                    if (marketingCertificateSeckillList.getStatus().equals("1")){
                        map.put("onOffState","0");
                    }else {
                        map.put("onOffState","1");
                    }
                }else {
                    map.put("onOffState","1");
                }
            }else {
                map.put("onOffState","1");
            }
        }else {
            map.put("onOffState","1");
        }
        //结束时间戳
        Map<String, Object> info = iMarketingCertificateSeckillActivityListService.getInfo(marketingCertificateSeckillActivityList.getId());
        map.put("time",info.get("surplusTime"));
        //获取兑换券
        MarketingCertificate marketingCertificate = iMarketingCertificateService.getById(marketingCertificateSeckillList.getMarketingCertificateId());

        BigDecimal subtract = marketingCertificate.getTotal().subtract(marketingCertificate.getReleasedQuantity());
        long count = iMarketingCertificateRecordService.count(new LambdaQueryWrapper<MarketingCertificateRecord>()
                .eq(MarketingCertificateRecord::getDelFlag, "0")
                .eq(MarketingCertificateRecord::getMarketingCertificateId, marketingCertificate.getId())
        );
        map.put("residue",subtract.divide(new BigDecimal(count).add(subtract), 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN));
        //券主图
        map.put("mainPicture", marketingCertificate.getMainPicture());
        //市场价
        map.put("marketPrice", marketingCertificate.getMarketPrice());
        //销售价
        map.put("price", marketingCertificate.getPrice());
        //券名称
        map.put("name", marketingCertificate.getName());
        //券使用时间
        if (marketingCertificate.getVouchersWay().equals("0")) {
            map.put("usrTime", marketingCertificate.getStartTime() + "~" + marketingCertificate.getEndTime());
        } else if (marketingCertificate.getVouchersWay().equals("1")) {
            map.put("usrTime", "领券当日起" + marketingCertificate.getDisData() + marketingCertificate.getMonad() + "内有效");
        } else if (marketingCertificate.getVouchersWay().equals("2")) {
            map.put("usrTime", "领券次日起" + marketingCertificate.getDisData() + marketingCertificate.getMonad() + "内有效");
        }
        //兑换方式
        if (marketingCertificate.getCertificateType().equals("0")) {
            map.put("certificate", "可兑换以下商品");
        } else if (marketingCertificate.getCertificateType().equals("1")) {
            map.put("certificate", "以下商品任选其一");
        }
        //兑换商品
        List<Map<String, Object>> marketingCertificateSeckillListGoodByCertificateId = iMarketingCertificateGoodService.findMarketingCertificateSeckillListGoodByCertificateId(marketingCertificate.getId());
        if (marketingCertificateSeckillListGoodByCertificateId.size() < 1) {
            map.put("certificate", "");
        }
        map.put("goodList",marketingCertificateSeckillListGoodByCertificateId);
        //使用店铺
        if (marketingCertificate.getRewardStore().equals("0")){
            List<Map<String, Object>> maps = iStoreManageService.listMaps(new QueryWrapper<StoreManage>()
                    .select("id as id,logo_addr as logoAddr")
                    .eq("del_flag", "0")
                    .eq("status", "1")
                    .in("attestation_status", "1", "2")
                    .orderByDesc("create_time")
                    .last("limit 10")
            );
            map.put("storeList",maps);
        }else {
            map.put("storeList",iMarketingCertificateStoreService.findMarketingCertificateSeckillListStoreByCertificateId(marketingCertificate.getId()));
        }
        //使用人
        map.put("userRestrict",marketingCertificate.getUserRestrict());
        //券说明
        map.put("discountExplain",marketingCertificate.getDiscountExplain());
        //限购数量；0：代表不限购
        map.put("purchaseNumber",marketingCertificateSeckillList.getPurchaseNumber());

        return Result.ok(map);
    }
}
