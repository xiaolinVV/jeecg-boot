package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingMaterialDianzan;
import org.jeecg.modules.marketing.vo.MarketingMaterialDianzanVO;

import java.util.Map;

/**
 * @Description: 素材点赞
 * @Author: jeecg-boot
 * @Date:   2020-06-03
 * @Version: V1.0
 */
public interface MarketingMaterialDianzanMapper extends BaseMapper<MarketingMaterialDianzan> {
    /**
     * 查询点赞分页数据
     * @param page
     * @param marketingMaterialDianzanVO
     * @return
     */
  IPage<Map<String,Object>> getMarketingMaterialDianzanMap(Page<Map<String,Object>> page, @Param("marketingMaterialDianzanVO") MarketingMaterialDianzanVO marketingMaterialDianzanVO);
}
