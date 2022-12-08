package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.marketing.entity.MarketingMaterialColumn;

import java.util.List;
import java.util.Map;

/**
 * @Description: 素材库栏目
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
public interface MarketingMaterialColumnMapper extends BaseMapper<MarketingMaterialColumn> {
    /**
     * 查询素材库栏目名称
     * @return
     */
    List<Map<String,Object>> getMarketingMaterialColumnName();

}
