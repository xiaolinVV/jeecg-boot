package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingPrefectureRecommendDTO;
import org.jeecg.modules.marketing.entity.MarketingPrefectureRecommend;
import org.jeecg.modules.marketing.mapper.MarketingPrefectureRecommendMapper;
import org.jeecg.modules.marketing.service.IMarketingPrefectureRecommendService;
import org.jeecg.modules.marketing.vo.MarketingPrefectureRecommendVO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 专区推荐
 * @Author: jeecg-boot
 * @Date:   2020-09-14
 * @Version: V1.0
 */
@Service
public class MarketingPrefectureRecommendServiceImpl extends ServiceImpl<MarketingPrefectureRecommendMapper, MarketingPrefectureRecommend> implements IMarketingPrefectureRecommendService {

    @Override
    public IPage<MarketingPrefectureRecommendVO> queryPageList(Page<MarketingPrefectureRecommendVO> page, MarketingPrefectureRecommendDTO marketingPrefectureRecommendDTO) {
        return baseMapper.queryPageList(page,marketingPrefectureRecommendDTO);
    }

    @Override
    public List<Map<String, Object>> getMarketingPrefectureRecommendList(String marketingPrefectureId) {
        return baseMapper.getMarketingPrefectureRecommendList(marketingPrefectureId);
    }

    @Override
    public List<Map<String, Object>> getMarketingPrefectureRecommendColumn(HashMap<String, Object> map) {
        return baseMapper.getMarketingPrefectureRecommendColumn(map);
    }

}
