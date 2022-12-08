package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingFreeAdvertising;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单广告
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface IMarketingFreeAdvertisingService extends IService<MarketingFreeAdvertising> {

    /**
     * 首页广告查询
     *
     * @return
     */
    public List<Map<String,Object>> selectMarketingFreeAdvertisingIndex();

    /**
     * 停止超时广告
     * 张靠勤  2021-4-8
     */
    public void stopMarketingFreeAdvertising();

}
