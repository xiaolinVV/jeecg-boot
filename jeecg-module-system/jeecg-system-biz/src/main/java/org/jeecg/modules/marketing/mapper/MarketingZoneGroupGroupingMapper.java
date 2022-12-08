package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupGrouping;

import java.util.Map;

/**
 * @Description: 专区团分组
 * @Author: jeecg-boot
 * @Date:   2021-07-31
 * @Version: V1.0
 */
public interface MarketingZoneGroupGroupingMapper extends BaseMapper<MarketingZoneGroupGrouping> {

    IPage<Map<String,Object>> findMarketingZoneGroupGroupingByMemberId(Page<Map<String,Object>> page,@Param("memberId") String memberId);
}
