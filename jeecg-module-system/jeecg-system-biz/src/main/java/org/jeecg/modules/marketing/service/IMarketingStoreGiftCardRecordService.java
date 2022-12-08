package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 店铺礼品卡使用记录
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
public interface IMarketingStoreGiftCardRecordService extends IService<MarketingStoreGiftCardRecord> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, QueryWrapper<MarketingStoreGiftCardRecord> queryWrapper, Map<String, Object> requestMap);

    IPage<Map<String,Object>> findMarketingStoreGiftCardRecordList(Page<Map<String,Object>> page, HashMap<String, Object> map);

    Map<String,Object> findMarketingStoreGiftCardRecordParticulars(String id);
}
