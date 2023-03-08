package org.jeecg.modules.marketing.store.prefecture.service;

import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺专区
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
public interface IMarketingStorePrefectureListService extends IService<MarketingStorePrefectureList> {


    /**
     * 根据店铺id获取专区列表
     *
     *
     * @param storeManageId
     * @return
     */
    public List<Map<String,Object>> getMarketingStorePrefectureListByStoreManageId(String storeManageId);

    /*
    获取限购专区的列表
     */
    public List<Map<String,Object>> getMarketingStorePrefectureList(String storeManageId);

}
