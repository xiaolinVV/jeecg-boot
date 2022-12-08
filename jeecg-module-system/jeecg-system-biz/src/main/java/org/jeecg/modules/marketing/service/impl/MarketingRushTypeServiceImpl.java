package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.entity.MarketingPrefectureType;
import org.jeecg.modules.marketing.entity.MarketingRushType;
import org.jeecg.modules.marketing.mapper.MarketingRushTypeMapper;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureTypeService;
import org.jeecg.modules.marketing.service.IMarketingRushTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 抢购活动-分类管理
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Service
public class MarketingRushTypeServiceImpl extends ServiceImpl<MarketingRushTypeMapper, MarketingRushType> implements IMarketingRushTypeService {
    @Autowired
    private IMarketingPrefectureTypeService iMarketingPrefectureTypeService;
    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper<MarketingRushType> queryWrapper) {
        IPage<Map<String, Object>> mapIPage = baseMapper.queryPageList(page, queryWrapper);
        mapIPage.getRecords().forEach(mp->{
            if (StringUtils.isNotBlank(String.valueOf(mp.get("marketingPrefectureId")))){
                MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getById(String.valueOf(mp.get("marketingPrefectureId")));
                if (StringUtils.isNotBlank(String.valueOf(mp.get("marketingPrefectureTypeId")))){
                    MarketingPrefectureType marketingPrefectureType = iMarketingPrefectureTypeService.getById(String.valueOf(mp.get("marketingPrefectureTypeId")));
                    if (marketingPrefectureType!=null&&marketingPrefectureType.getLevel().doubleValue()==2){
                        mp.put("marketingPrefectureTypeId",iMarketingPrefectureTypeService.getById(marketingPrefectureType.getPid()).getId()+","+marketingPrefectureType.getId());
                    }
                    String prefectureTypeName = marketingPrefectureType!=null?marketingPrefectureType.getTypeName()+"分类":"";
                    mp.put("consignmentCondition",marketingPrefecture.getPrefectureName()+"专区"+prefectureTypeName+"复购一单");
                }
            }
        });
        return mapIPage;
    }
}
