package org.jeecg.modules.marketing.store.prefecture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.jeecg.modules.index.utils.SetttingViewUtils;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureList;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureSettting;
import org.jeecg.modules.marketing.store.prefecture.mapper.MarketingStorePrefectureSetttingMapper;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureListService;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureSetttingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺专区设置
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@Service
public class MarketingStorePrefectureSetttingServiceImpl extends ServiceImpl<MarketingStorePrefectureSetttingMapper, MarketingStorePrefectureSettting> implements IMarketingStorePrefectureSetttingService {


    @Autowired
    private SetttingViewUtils setttingViewUtils;

    @Autowired
    private IMarketingStorePrefectureListService iMarketingStorePrefectureListService;


    @Override
    public MarketingStorePrefectureSettting getMarketingStorePrefectureSettting() {
        return this.getOne(new LambdaQueryWrapper<MarketingStorePrefectureSettting>().eq(MarketingStorePrefectureSettting::getStatus,"1"));
    }

    @Override
    public void settingView(Map<String, Object> resultMap, String softModel, String storeManageId) {
        MarketingStorePrefectureSettting marketingStorePrefectureSettting=this.getMarketingStorePrefectureSettting();
        Map<String,Object> marketingStorePrefectureSetttingMap= Maps.newHashMap();
        //判断店铺是否有专区
        List<Map<String,Object>> marketingStorePrefectureLists=iMarketingStorePrefectureListService.listMaps(new QueryWrapper<MarketingStorePrefectureList>()
                .select("id","icon","prefecture_name as prefectureName")
                .lambda()
                .eq(MarketingStorePrefectureList::getStoreManageId,storeManageId)
                .eq(MarketingStorePrefectureList::getStatus,"1")
                .orderByDesc(MarketingStorePrefectureList::getUpdateTime));
        if(marketingStorePrefectureLists.size()==0){
            marketingStorePrefectureSetttingMap.put("isViewMarketingStorePrefectureSettting","0");
            resultMap.put("marketingStorePrefectureSetttingMap",marketingStorePrefectureSetttingMap);
            return;
        }else {
            marketingStorePrefectureSetttingMap.put("marketingStorePrefectureLists",marketingStorePrefectureLists);
            setttingViewUtils.setView(resultMap, "marketingStorePrefectureSetttingMap", marketingStorePrefectureSetttingMap, "isViewMarketingStorePrefectureSettting", marketingStorePrefectureSettting, softModel);
        }
    }
}
