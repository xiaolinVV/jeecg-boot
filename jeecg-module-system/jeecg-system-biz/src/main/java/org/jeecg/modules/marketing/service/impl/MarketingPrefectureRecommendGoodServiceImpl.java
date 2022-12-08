package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingPrefectureRecommendGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingPrefectureRecommendGood;
import org.jeecg.modules.marketing.mapper.MarketingPrefectureRecommendGoodMapper;
import org.jeecg.modules.marketing.service.IMarketingPrefectureRecommendGoodService;
import org.jeecg.modules.marketing.vo.MarketingPrefectureRecommendGoodVO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 专区推荐商品
 * @Author: jeecg-boot
 * @Date:   2020-09-14
 * @Version: V1.0
 */
@Service
public class MarketingPrefectureRecommendGoodServiceImpl extends ServiceImpl<MarketingPrefectureRecommendGoodMapper, MarketingPrefectureRecommendGood> implements IMarketingPrefectureRecommendGoodService {

    @Override
    public IPage<MarketingPrefectureRecommendGoodVO> queryPageList(Page<MarketingPrefectureRecommendGoodVO> page, MarketingPrefectureRecommendGoodDTO marketingPrefectureRecommendGoodDTO) {
        return baseMapper.queryPageList(page,marketingPrefectureRecommendGoodDTO);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingPrefectureRecommendGoodList(Page<Map<String, Object>> page,Map<String, Object> map) {
        return baseMapper.getMarketingPrefectureRecommendGoodList(page,map);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingPrefectureRecommendGoodListOne(Page<Map<String, Object>> page, HashMap<String, Object> map) {
        return baseMapper.getMarketingPrefectureRecommendGoodListOne(page,map);
    }
}
