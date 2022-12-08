package org.jeecg.modules.marketing.service.impl;

import org.jeecg.modules.marketing.entity.MarketingMaterialColumn;
import org.jeecg.modules.marketing.mapper.MarketingMaterialColumnMapper;
import org.jeecg.modules.marketing.service.IMarketingMaterialColumnService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 素材库栏目
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
@Service
public class MarketingMaterialColumnServiceImpl extends ServiceImpl<MarketingMaterialColumnMapper, MarketingMaterialColumn> implements IMarketingMaterialColumnService {
    /**
     * 查询素材库栏目名称
     * @return
     */
    @Override
   public List<Map<String,Object>> getMarketingMaterialColumnName(){
       return baseMapper.getMarketingMaterialColumnName();
   }
}
