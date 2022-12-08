package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingMaterialDianzan;
import org.jeecg.modules.marketing.mapper.MarketingMaterialDianzanMapper;
import org.jeecg.modules.marketing.service.IMarketingMaterialDianzanService;
import org.jeecg.modules.marketing.vo.MarketingMaterialDianzanVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 素材点赞
 * @Author: jeecg-boot
 * @Date:   2020-06-03
 * @Version: V1.0
 */
@Service
public class MarketingMaterialDianzanServiceImpl extends ServiceImpl<MarketingMaterialDianzanMapper, MarketingMaterialDianzan> implements IMarketingMaterialDianzanService {

    /**
     * 查询点赞分页数据
     * @param page
     * @param marketingMaterialDianzanVO
     * @return
     */
  public  IPage<Map<String,Object>> getMarketingMaterialDianzanMap(Page<Map<String,Object>> page, @Param("marketingMaterialDianzanVO") MarketingMaterialDianzanVO marketingMaterialDianzanVO){
      return  baseMapper.getMarketingMaterialDianzanMap(page,marketingMaterialDianzanVO);
  };

}
