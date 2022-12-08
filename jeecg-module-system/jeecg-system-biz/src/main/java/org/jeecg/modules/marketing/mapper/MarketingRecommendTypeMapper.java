package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingRecommendTypeDTO;
import org.jeecg.modules.marketing.entity.MarketingRecommendType;
import org.jeecg.modules.marketing.vo.MarketingRecommendTypeVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 推荐分类
 * @Author: jeecg-boot
 * @Date:   2019-12-07
 * @Version: V1.0
 */
public interface MarketingRecommendTypeMapper extends BaseMapper<MarketingRecommendType> {

    IPage<MarketingRecommendTypeDTO> getMarketingRecommendTypeDTO(Page<MarketingRecommendType> page, @Param("marketingRecommendTypeVO") MarketingRecommendTypeVO marketingRecommendTypeVO);


    /**
     * 获取推荐类型列表
     * 
     * @return
     */
    List<Map<String,Object>> findMarketingRecommendTypes();

}
