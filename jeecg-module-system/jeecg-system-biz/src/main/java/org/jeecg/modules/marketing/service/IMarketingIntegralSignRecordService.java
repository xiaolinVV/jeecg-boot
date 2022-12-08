package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingIntegralSignRecord;

import java.util.Map;

/**
 * @Description: 签到次数
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
public interface IMarketingIntegralSignRecordService extends IService<MarketingIntegralSignRecord> {


    /**
     * 获取当前签到时间的数据
     *
     * @param paramMap
     * @return
     */
    public Map<String,Object> getSignTime(Map<String,Object> paramMap);

}
