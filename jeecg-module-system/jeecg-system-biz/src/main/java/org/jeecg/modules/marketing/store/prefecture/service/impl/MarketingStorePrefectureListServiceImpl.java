package org.jeecg.modules.marketing.store.prefecture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureList;
import org.jeecg.modules.marketing.store.prefecture.mapper.MarketingStorePrefectureListMapper;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureListService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺专区
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@Service
public class MarketingStorePrefectureListServiceImpl extends ServiceImpl<MarketingStorePrefectureListMapper, MarketingStorePrefectureList> implements IMarketingStorePrefectureListService {

    @Override
    public List<Map<String, Object>> getMarketingStorePrefectureListByStoreManageId(String storeManageId) {
        return this.listMaps(new QueryWrapper<MarketingStorePrefectureList>()
                .select("id","prefecture_name as prefectureName")
                .lambda()
                .eq(MarketingStorePrefectureList::getStoreManageId,storeManageId)
                .eq(MarketingStorePrefectureList::getStatus,"1"));
    }

    @Override
    public List<Map<String, Object>> getMarketingStorePrefectureList(String storeManageId) {
        return this.listMaps(new QueryWrapper<MarketingStorePrefectureList>()
                .select("id","prefecture_name as prefectureName")
                .lambda()
                .eq(MarketingStorePrefectureList::getPrefectureType,"1")
                .eq(MarketingStorePrefectureList::getStoreManageId,storeManageId)
                .eq(MarketingStorePrefectureList::getStatus,"1"));
    }
}
