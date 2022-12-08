package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingMaterialBrowse;
import org.jeecg.modules.marketing.mapper.MarketingMaterialBrowseMapper;
import org.jeecg.modules.marketing.service.IMarketingMaterialBrowseService;
import org.jeecg.modules.marketing.vo.MarketingMaterialBrowseVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 素材浏览记录
 * @Author: jeecg-boot
 * @Date:   2020-06-03
 * @Version: V1.0
 */
@Service
public class MarketingMaterialBrowseServiceImpl extends ServiceImpl<MarketingMaterialBrowseMapper, MarketingMaterialBrowse> implements IMarketingMaterialBrowseService {
    /**
     * 查询素材浏览记录分页数据
     * @param page
     * @param marketingMaterialBrowseVO
     * @return
     */
  public  IPage<Map<String,Object>> getMarketingMaterialBrowseMap(Page<Map<String,Object>> page, MarketingMaterialBrowseVO marketingMaterialBrowseVO){
      return  baseMapper.getMarketingMaterialBrowseMap(page,marketingMaterialBrowseVO);
  };

}
