package org.jeecg.modules.marketing.service;

import org.jeecg.modules.marketing.entity.MarketingSearchterm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 推荐搜词
 * @Author: jeecg-boot
 * @Date:   2019-10-11
 * @Version: V1.0
 */
public interface IMarketingSearchtermService extends IService<MarketingSearchterm> {
    //所有状态可用集合
    public List<MarketingSearchterm> getmarketingSearchtermList();
}
