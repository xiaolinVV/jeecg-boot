package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IMarketingPrefectureRecommendService extends IService<MarketingPrefectureRecommend> {

    IPage<MarketingPrefectureRecommendVO> queryPageList(Page<MarketingPrefectureRecommendVO> page, MarketingPrefectureRecommendDTO marketingPrefectureRecommendDTO);

    List<Map<String, Object>> getMarketingPrefectureRecommendList(String marketingPrefectureId);

    List<Map<String,Object>> getMarketingPrefectureRecommendColumn(HashMap<String, Object> map);
}

