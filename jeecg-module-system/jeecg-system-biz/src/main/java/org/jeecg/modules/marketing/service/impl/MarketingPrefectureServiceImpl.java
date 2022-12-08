package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.mapper.MarketingPrefectureMapper;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 平台专区
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
@Slf4j
@Service
public class MarketingPrefectureServiceImpl extends ServiceImpl<MarketingPrefectureMapper, MarketingPrefecture> implements IMarketingPrefectureService {
    /**
     * 返回 平台专区 所有 启用 Id 名称 logo
     * @param paramMap
     * @return
     */
    @Override
   public List<Map<String,Object>> getMarketingPrefectureIdName(Map<String,Object> paramMap){
       return baseMapper.getMarketingPrefectureIdName(paramMap);
   }

    @Override
    public List<Map<String, Object>> findPrefectureIndex(String softModel) {
        return baseMapper.findPrefectureIndex(softModel);
    }

    /**
     * 过期时间停用专区
     */
    @Override
    public void   getMarketingPrefectureOvertime(){
        List<MarketingPrefecture> marketingPrefectureList = this.list(new LambdaQueryWrapper<MarketingPrefecture>()
                .eq(MarketingPrefecture::getValidTime,"1")
                .lt(MarketingPrefecture::getEndTime,new Date())
                .eq(MarketingPrefecture::getStatus,"1"));
        marketingPrefectureList.forEach(mp->{
            log.info("专区时间过期被停用专区:"+mp.getPrefectureName()+"专区id:"+mp.getId());
            //停用
            mp.setStatus("0");
            baseMapper.updateById(mp);
        });

    }

    @Override
    public List<Map<String, Object>> getFiltrationGoodIds(Map<String, Object> paramMap) {
        return baseMapper.getFiltrationGoodIds(paramMap);
    }

    @Override
    public List<Map<String, Object>> getMarketingPrefectureByRecommend() {
        return baseMapper.getMarketingPrefectureByRecommend();
    }


}
