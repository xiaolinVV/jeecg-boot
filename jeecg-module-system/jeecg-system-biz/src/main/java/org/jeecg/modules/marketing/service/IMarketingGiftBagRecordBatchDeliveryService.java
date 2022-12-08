package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatchDelivery;

import java.util.List;
import java.util.Map;

/**
 * @Description: 采购礼包配送信息
 * @Author: jeecg-boot
 * @Date:   2020-09-04
 * @Version: V1.0
 */
public interface IMarketingGiftBagRecordBatchDeliveryService extends IService<MarketingGiftBagRecordBatchDelivery> {

    IPage<Map<String,Object>> findMarketingGiftBagRecordBatchDeliveryById(Page<Map<String,Object>> page,Map<String,Object> map);

    List<Map<String,Object>> findMarketingGiftBagRecordBatchDeliveryMap(String marketingGiftBagRecordBatchId);


}
