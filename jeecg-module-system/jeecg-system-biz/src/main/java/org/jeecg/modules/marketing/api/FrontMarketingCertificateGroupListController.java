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
 * 拼好券
 */
@Slf4j
@RequestMapping("front/marketingCertificateGroupList")
@Controller
public class FrontMarketingCertificateGroupListController {
    @Autowired
    private IMarketingCertificateGroupListService iMarketingCertificateGroupListService;
    @Autowired
    private IMarketingCertificateGroupBaseSettingService iMarketingCertificateGroupBaseSettingService;
    @Autowired
    private IMarketingCertificateGroupManageService iMarketingCertificateGroupManageService;
    @Autowired
    private IMarketingCertificateGroupRecordService iMarketingCertificateGroupRecordService;
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
     * 拼好券头部信息
     * @return
     */
    @RequestMapping("headData")
    @ResponseBody
    public Result<?> headData(){
        HashMap<String, Object> map = new HashMap<>();
        //获取拼好券设置
        MarketingCertificateGroupBaseSetting marketingCertificateGroupBaseSetting = iMarketingCertificateGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateGroupBaseSetting>()
                .eq(MarketingCertificateGroupBaseSetting::getDelFlag, "0")
        );
        if (marketingCertificateGroupBaseSetting != null){
            //规则说明
            map.put("ruleDescription",marketingCertificateGroupBaseSetting.getRuleDescription());
            map.put("memberList",iMarketingCertificateGroupRecordService.getMemberList());
            map.put("marketingCertificateGroupManageList",iMarketingCertificateGroupManageService.dataList());
            //活动别名
            map.put("anotherName",marketingCertificateGroupBaseSetting.getAnotherName());
            //活动标签
            map.put("label",marketingCertificateGroupBaseSetting.getLabel());
            //分享图
            map.put("coverPlan",marketingCertificateGroupBaseSetting.getCoverPlan());
            //海报图
            map.put("posters",marketingCertificateGroupBaseSetting.getPosters());
            //封面图
            map.put("surfacePlot",marketingCertificateGroupBaseSetting.getSurfacePlot());
            return Result.ok(map);
        }else {
            return Result.ok("活动未开启");
        }
    }

    /**
     * 拼好券列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("pageListByMarketingCertificateGroupList")
    @ResponseBody
    public Result<?> pageListByMarketingCertificateGroupList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
        return Result.ok(iMarketingCertificateGroupListService.pageListByMarketingCertificateGroupList(page));
    }

    /**
     * 拼好券详情
     * @param id
     * @return
     */
    @RequestMapping("findMarketingCertificateGroupListById")
    @ResponseBody
    public Result<?> findMarketingCertificateGroupListById(String id){
        if (StringUtils.isBlank(id)) {
            return Result.error("拼好券id为空");
        }
        HashMap<String, Object> map = new HashMap<>();
        MarketingCertificateGroupBaseSetting marketingCertificateGroupBaseSetting = iMarketingCertificateGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateGroupBaseSetting>()
                .eq(MarketingCertificateGroupBaseSetting::getDelFlag, "0"));
        if (marketingCertificateGroupBaseSetting!=null){
            //分享图
            map.put("coverPlan",marketingCertificateGroupBaseSetting.getCoverPlan());
            //活动别名
            map.put("anotherName",marketingCertificateGroupBaseSetting.getAnotherName());
            //活动标签
            map.put("label",marketingCertificateGroupBaseSetting.getLabel());
        }
        //获取拼好券
        MarketingCertificateGroupList marketingCertificateGroupList = iMarketingCertificateGroupListService.getById(id);
        //限时抢券id
        map.put("id",marketingCertificateGroupList.getId());
        //兑换券id
        map.put("marketingCertificateId",marketingCertificateGroupList.getMarketingCertificateId());
        //活动价
        map.put("activityPrice", marketingCertificateGroupList.getActivityPrice());
        if (marketingCertificateGroupList.getDelFlag().equals("0")){
            if (marketingCertificateGroupList.getStatus().equals("1")){
                map.put("onOffState","0");
            }else {
                map.put("onOffState","1");
            }
        }else {
            map.put("onOffState","1");
        }
        //获取兑换券
        MarketingCertificate marketingCertificate = iMarketingCertificateService.getById(marketingCertificateGroupList.getMarketingCertificateId());

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
        return Result.ok(map);
    }

    /**
     * 拼团券详情
     * @param id
     * @return
     */
    @RequestMapping("findMarketingCertificateGroupManageById")
    @ResponseBody
    public Result<?>findMarketingCertificateGroupManageById(String id){
        if (StringUtils.isBlank(id)) {
            return Result.error("拼好券id为空");
        }
        HashMap<String, Object> map = new HashMap<>();
        MarketingCertificateGroupBaseSetting marketingCertificateGroupBaseSetting = iMarketingCertificateGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateGroupBaseSetting>()
                .eq(MarketingCertificateGroupBaseSetting::getDelFlag, "0"));
        //活动别名
        map.put("anotherName",marketingCertificateGroupBaseSetting.getAnotherName());
        //活动标签
        map.put("label",marketingCertificateGroupBaseSetting.getLabel());
        //获取拼团id
        MarketingCertificateGroupManage marketingCertificateGroupManage = iMarketingCertificateGroupManageService.getById(id);
        //限时抢券id
        map.put("id",marketingCertificateGroupManage.getId());
        //活动价
        map.put("activityPrice", marketingCertificateGroupManage.getActivityPrice());

        MarketingCertificateGroupList marketingCertificateGroupList = iMarketingCertificateGroupListService.getById(marketingCertificateGroupManage.getMarketingCertificateGroupListId());
        if (marketingCertificateGroupList.getDelFlag().equals("0")){
            if (marketingCertificateGroupList.getStatus().equals("1")){
                map.put("onOffState","0");
            }else {
                map.put("onOffState","1");
            }
        }else {
            map.put("onOffState","1");
        }
        //获取兑换券
        MarketingCertificate marketingCertificate = iMarketingCertificateService.getById(marketingCertificateGroupList.getMarketingCertificateId());
        //兑换券id
        map.put("marketingCertificateId",marketingCertificate.getId());
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
        return Result.ok(map);
    }
}
