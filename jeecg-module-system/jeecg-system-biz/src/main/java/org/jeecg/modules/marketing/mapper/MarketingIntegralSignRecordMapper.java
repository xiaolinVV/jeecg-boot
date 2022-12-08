package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingIntegralSignRecord;

import java.util.Map;

/**
 * @Description: 签到次数
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
public interface MarketingIntegralSignRecordMapper extends BaseMapper<MarketingIntegralSignRecord> {

    /**
     * 获取当前签到时间的数据
     *
     * @param paramMap
     * @return
     */
    public Map<String,Object> getSignTime(@Param("paramMap") Map<String,Object> paramMap);

}
