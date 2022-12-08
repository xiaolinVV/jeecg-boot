package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillActivityList;

import java.util.Map;

/**
 * @Description: 活动列表
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
public interface IMarketingCertificateSeckillActivityListService extends IService<MarketingCertificateSeckillActivityList> {

    Map<String,Object> getInfo(String id);
}
