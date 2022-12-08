package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.dto.MarketingMaterialRecommendDTO;
import org.jeecg.modules.marketing.entity.MarketingMaterialRecommend;
import org.jeecg.modules.marketing.mapper.MarketingMaterialRecommendMapper;
import org.jeecg.modules.marketing.service.IMarketingMaterialRecommendService;
import org.jeecg.modules.marketing.vo.MarketingMaterialRecommendVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 素材推荐表
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
@Service
public class MarketingMaterialRecommendServiceImpl extends ServiceImpl<MarketingMaterialRecommendMapper, MarketingMaterialRecommend> implements IMarketingMaterialRecommendService {
    /**
     * 推荐素材列表
     * @param page
     * @param marketingMaterialRecommendVO
     * @return
     */
    @Override
  public  IPage<MarketingMaterialRecommendDTO> getMarketingMaterialRecommendDTO(Page page, MarketingMaterialRecommendVO marketingMaterialRecommendVO){
      return baseMapper.getMarketingMaterialRecommendDTO(page,marketingMaterialRecommendVO);
  };
    /**
     * 查询素材推荐列表 的素材id集合
     * @return
     */
    @Override
   public List<String> getMarketingMaterialListIdList(){
        return baseMapper.getMarketingMaterialListIdList();
    };
}
