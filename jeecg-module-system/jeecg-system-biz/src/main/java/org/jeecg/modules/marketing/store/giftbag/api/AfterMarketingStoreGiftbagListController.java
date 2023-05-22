package org.jeecg.modules.marketing.store.giftbag.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagList;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagRecord;
import org.jeecg.modules.marketing.store.giftbag.entity.MarketingStoreGiftbagSetting;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagListService;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagRecordService;
import org.jeecg.modules.marketing.store.giftbag.service.IMarketingStoreGiftbagSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("after/marketingStoreGiftbagList")
public class AfterMarketingStoreGiftbagListController {

    @Autowired
    private IMarketingStoreGiftbagListService iMarketingStoreGiftbagListService;


    @Autowired
    private IMarketingStoreGiftbagSettingService iMarketingStoreGiftbagSettingService;

    @Autowired
    private IMarketingStoreGiftbagRecordService iMarketingStoreGiftbagRecordService;


    /**
     * 礼包团页面
     *
     * @param storeManageId
     * @return
     */
    @PostMapping("viewMarketingStoreGiftbag")
    public Result<?> viewMarketingStoreGiftbag(String storeManageId,@RequestAttribute("memberId") String memberId){
        Map<String,Object> resultMap= Maps.newHashMap();
        MarketingStoreGiftbagSetting marketingStoreGiftbagSetting=iMarketingStoreGiftbagSettingService.getMarketingStoreGiftbagSetting();
        resultMap.put("rule",marketingStoreGiftbagSetting.getRule());
        MarketingStoreGiftbagList marketingStoreGiftbagList=iMarketingStoreGiftbagListService.getMarketingStoreGiftbagListByStoreManageId(storeManageId);
        if (marketingStoreGiftbagList != null) {
            resultMap.put("price",marketingStoreGiftbagList.getPrice());
            resultMap.put("detailsFigure",marketingStoreGiftbagList.getDetailsFigure());
            resultMap.put("id",marketingStoreGiftbagList.getId());
            resultMap.put("buyCount",iMarketingStoreGiftbagRecordService.count(new LambdaQueryWrapper<MarketingStoreGiftbagRecord>()
                    .eq(MarketingStoreGiftbagRecord::getMarketingStoreGiftbagListId,marketingStoreGiftbagList.getId())
                    .eq(MarketingStoreGiftbagRecord::getMemebrListId,memberId)));
            resultMap.put("shareFigure",marketingStoreGiftbagList.getShareFigure());
        }
        return Result.ok(resultMap);
    }
}
