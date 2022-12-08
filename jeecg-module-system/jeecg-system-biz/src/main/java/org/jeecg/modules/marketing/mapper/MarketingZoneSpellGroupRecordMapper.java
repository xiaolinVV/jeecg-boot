package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingZoneSpellGroupRecord;

import java.util.Map;

/**
 * @Description: 拼团次数明细
 * @Author: jeecg-boot
 * @Date:   2021-07-24
 * @Version: V1.0
 */
public interface MarketingZoneSpellGroupRecordMapper extends BaseMapper<MarketingZoneSpellGroupRecord> {

    IPage<Map<String,Object>> findMarketingZoneGroupRecordByMarketingZoneGroupTimeId(Page<Map<String,Object>> page, @Param("marketingZoneGroupTimeId") String marketingZoneGroupTimeId,@Param("goAndCome") String goAndCome);
}
