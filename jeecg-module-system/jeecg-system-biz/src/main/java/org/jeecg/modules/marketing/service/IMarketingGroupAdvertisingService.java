package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingGroupAdvertising;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团基础设置
 * @Author: jeecg-boot
 * @Date:   2021-03-21
 * @Version: V1.0
 */
public interface IMarketingGroupAdvertisingService extends IService<MarketingGroupAdvertising> {


    /**
     * 首页广告模块
     *
     * 张靠勤    2021-3-31
     *
     * @return
     */
    public List<Map<String,Object>> selectMarketingGroupAdvertisingIndex();

    /**
     * 停止超时的中奖拼团广告
     *
     * 张靠勤  2021-4-8
     */
    public void stopMarketingGroupAdvertising();

}
