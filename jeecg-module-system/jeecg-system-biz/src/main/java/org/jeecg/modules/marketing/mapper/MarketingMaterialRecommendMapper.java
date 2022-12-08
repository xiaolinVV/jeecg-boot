package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingMaterialRecommendDTO;
import org.jeecg.modules.marketing.entity.MarketingMaterialRecommend;
import org.jeecg.modules.marketing.vo.MarketingMaterialRecommendVO;

import java.util.List;

/**
 * @Description: 素材推荐表
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
public interface MarketingMaterialRecommendMapper extends BaseMapper<MarketingMaterialRecommend> {
    /**
     * 推荐素材列表
     * @param page
     * @param marketingMaterialRecommendVO
     * @return
     */
    IPage<MarketingMaterialRecommendDTO> getMarketingMaterialRecommendDTO(Page page, @Param("marketingMaterialRecommendVO") MarketingMaterialRecommendVO marketingMaterialRecommendVO);

    /**
     * 查询素材推荐列表 的素材id集合
     * @return
     */
    List<String> getMarketingMaterialListIdList();

}
