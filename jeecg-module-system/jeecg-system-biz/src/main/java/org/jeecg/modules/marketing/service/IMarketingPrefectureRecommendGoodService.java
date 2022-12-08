package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IMarketingPrefectureRecommendGoodService extends IService<MarketingPrefectureRecommendGood> {

    IPage<MarketingPrefectureRecommendGoodVO> queryPageList(Page<MarketingPrefectureRecommendGoodVO> page, MarketingPrefectureRecommendGoodDTO marketingPrefectureRecommendGoodDTO);

    IPage<Map<String,Object>> getMarketingPrefectureRecommendGoodList(Page<Map<String,Object>> page,Map<String, Object> map);

    IPage<Map<String,Object>> getMarketingPrefectureRecommendGoodListOne(Page<Map<String,Object>> page, HashMap<String, Object> map);
}
