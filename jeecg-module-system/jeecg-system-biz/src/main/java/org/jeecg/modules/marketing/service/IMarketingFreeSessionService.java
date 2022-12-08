package org.jeecg.modules.marketing.service;

import org.jeecg.modules.marketing.entity.MarketingFreeSession;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 免单场次
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface IMarketingFreeSessionService extends IService<MarketingFreeSession> {

    /**
     * 获取当前场次信息
     * @return
     */
    public Map<String,Object> selectCurrentSession();

    public void autoCreateOrStop();
}
