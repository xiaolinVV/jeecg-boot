package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingPrefectureRecommendDTO;
import org.jeecg.modules.marketing.entity.MarketingPrefectureRecommend;
import org.jeecg.modules.marketing.vo.MarketingPrefectureRecommendVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 专区推荐
 * @Author: jeecg-boot
 * @Date:   2020-09-14
 * @Version: V1.0
 */
public interface MarketingPrefectureRecommendMapper extends BaseMapper<MarketingPrefectureRecommend> {

    IPage<MarketingPrefectureRecommendVO> queryPageList(Page<MarketingPrefectureRecommendVO> page,@Param("marketingPrefectureRecommendDTO") MarketingPrefectureRecommendDTO marketingPrefectureRecommendDTO);

    List<Map<String,Object>> getMarketingPrefectureRecommendList(@Param("marketingPrefectureId") String marketingPrefectureId);

    List<Map<String,Object>> getMarketingPrefectureRecommendColumn(@Param("map") HashMap<String, Object> map);

}
