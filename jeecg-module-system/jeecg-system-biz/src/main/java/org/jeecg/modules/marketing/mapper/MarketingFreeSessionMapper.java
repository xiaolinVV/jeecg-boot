package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.marketing.entity.MarketingFreeSession;

import java.util.Map;

/**
 * @Description: 免单场次
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface MarketingFreeSessionMapper extends BaseMapper<MarketingFreeSession> {


    /**
     * 获取当前场次信息
     * @return
     */
    public Map<String,Object> selectCurrentSession();
}
