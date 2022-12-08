package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingPrefectureRecommendGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingPrefectureRecommendGood;
import org.jeecg.modules.marketing.vo.MarketingPrefectureRecommendGoodVO;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 专区推荐商品
 * @Author: jeecg-boot
 * @Date:   2020-09-14
 * @Version: V1.0
 */
public interface MarketingPrefectureRecommendGoodMapper extends BaseMapper<MarketingPrefectureRecommendGood> {

    IPage<MarketingPrefectureRecommendGoodVO> queryPageList(Page<MarketingPrefectureRecommendGoodVO> page,@Param("marketingPrefectureRecommendGoodDTO") MarketingPrefectureRecommendGoodDTO marketingPrefectureRecommendGoodDTO);

    IPage<Map<String,Object>> getMarketingPrefectureRecommendGoodList(Page<Map<String, Object>> page,@Param("map") Map<String, Object> map);

    IPage<Map<String,Object>> getMarketingPrefectureRecommendGoodListOne(Page<Map<String,Object>> page,@Param("map") HashMap<String, Object> map);
}
