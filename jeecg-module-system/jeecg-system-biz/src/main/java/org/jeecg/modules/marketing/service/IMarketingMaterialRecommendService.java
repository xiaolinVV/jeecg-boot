package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IMarketingMaterialRecommendService extends IService<MarketingMaterialRecommend> {
    /**
     * 推荐素材列表
     * @param page
     * @param marketingMaterialRecommendVO
     * @return
     */
    IPage<MarketingMaterialRecommendDTO> getMarketingMaterialRecommendDTO(Page page,MarketingMaterialRecommendVO marketingMaterialRecommendVO);
    /**
     * 查询素材推荐列表 的素材id集合
     * @return
     */
    List<String> getMarketingMaterialListIdList();
}
