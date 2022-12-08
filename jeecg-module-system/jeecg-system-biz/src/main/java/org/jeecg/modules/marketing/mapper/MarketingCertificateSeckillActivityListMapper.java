package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillActivityList;

import java.util.Map;

/**
 * @Description: 活动列表
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
public interface MarketingCertificateSeckillActivityListMapper extends BaseMapper<MarketingCertificateSeckillActivityList> {

    Map<String,Object> getInfo(@Param("id") String id);
}
