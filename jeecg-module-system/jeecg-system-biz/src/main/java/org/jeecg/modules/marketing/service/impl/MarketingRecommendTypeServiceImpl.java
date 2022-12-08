package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.marketing.dto.MarketingRecommendTypeDTO;
import org.jeecg.modules.marketing.entity.MarketingRecommendType;
import org.jeecg.modules.marketing.mapper.MarketingRecommendTypeMapper;
import org.jeecg.modules.marketing.service.IMarketingRecommendTypeService;
import org.jeecg.modules.marketing.vo.MarketingRecommendTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 推荐分类
 * @Author: jeecg-boot
 * @Date:   2019-12-07
 * @Version: V1.0
 */
@Slf4j
@Service
public class MarketingRecommendTypeServiceImpl extends ServiceImpl<MarketingRecommendTypeMapper, MarketingRecommendType> implements IMarketingRecommendTypeService {
  @Autowired(required = false)
  private MarketingRecommendTypeMapper marketingRecommendTypeMapper;

  @Override
   public IPage<MarketingRecommendTypeDTO> getMarketingRecommendTypeDTO(Page<MarketingRecommendType> page, MarketingRecommendTypeVO marketingRecommendTypeVO){
   return marketingRecommendTypeMapper.getMarketingRecommendTypeDTO(page,marketingRecommendTypeVO);
   }

    @Override
    public List<Map<String, Object>> findMarketingRecommendTypes() {
        return marketingRecommendTypeMapper.findMarketingRecommendTypes();
    }

    /**
     * 过期时间停用推荐分类
     */
    @Override
    public void   getMarketingRecommendTypeOvertime(){
        log.info("推荐分类时间过期被停用分类定时器启动!!!");
        List<MarketingRecommendType> marketingRecommendTypes = baseMapper.selectList(new LambdaQueryWrapper<MarketingRecommendType>()
                .eq(MarketingRecommendType::getStatus,"1")
                .lt(MarketingRecommendType::getEndTime,new Date()));
        marketingRecommendTypes.forEach(mp->{
            log.info("推荐分类时间过期被停用分类:"+mp.getNickName()+"推荐分类id:"+mp.getId());
            //停用
            mp.setStatus("0");
            baseMapper.updateById(mp);

        });

    }

}
