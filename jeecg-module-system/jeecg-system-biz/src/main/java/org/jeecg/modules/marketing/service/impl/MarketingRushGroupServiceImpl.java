package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.marketing.entity.MarketingRushBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingRushGroup;
import org.jeecg.modules.marketing.entity.MarketingRushRecord;
import org.jeecg.modules.marketing.entity.MarketingRushType;
import org.jeecg.modules.marketing.mapper.MarketingRushGroupMapper;
import org.jeecg.modules.marketing.mapper.MarketingRushRecordMapper;
import org.jeecg.modules.marketing.mapper.MarketingRushTypeMapper;
import org.jeecg.modules.marketing.service.IMarketingRushBaseSettingService;
import org.jeecg.modules.marketing.service.IMarketingRushGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 抢购活动-寄售记录
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Service
public class MarketingRushGroupServiceImpl extends ServiceImpl<MarketingRushGroupMapper, MarketingRushGroup> implements IMarketingRushGroupService {
    @Autowired(required = false)
    private MarketingRushRecordMapper marketingRushRecordMapper;
    @Autowired(required = false)
    private MarketingRushTypeMapper marketingRushTypeMapper;
    @Autowired
    private IMarketingRushBaseSettingService iMarketingRushBaseSettingService;
    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, QueryWrapper<MarketingRushGroup> queryWrapper, Map<String, Object> requestMap) {
        IPage<Map<String, Object>> mapIPage = baseMapper.queryPageList(page, queryWrapper, requestMap);
        List<Map<String, Object>> records = mapIPage.getRecords();
        records.forEach(rs->{
            List<MarketingRushRecord> marketingRushRecordList = marketingRushRecordMapper.selectList(new LambdaQueryWrapper<MarketingRushRecord>()
                    .eq(MarketingRushRecord::getDelFlag, "0")
                    .eq(MarketingRushRecord::getStatus,"1")
                    .eq(MarketingRushRecord::getMarketingRushGroupId,String.valueOf(rs.get("id")))
                    .eq(MarketingRushRecord::getMemberListId,String.valueOf(rs.get("memberListId")))
            );
            rs.put("consignmentPrice",marketingRushRecordList.stream()
                    .map(MarketingRushRecord::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            rs.put("consignmentGoods",marketingRushRecordList.size()+"件");
        });
        return mapIPage;
    }

    @Override
    public IPage<Map<String,Object>> findMarketingRushGroup(Page<Map<String,Object>> page, String memberId, String consignmentStatus) {
        IPage<Map<String, Object>> marketingRushGroup = baseMapper.findMarketingRushGroup(page, memberId, consignmentStatus);
        marketingRushGroup.getRecords().forEach(mrg->{
            List<MarketingRushRecord> marketingRushRecordList = marketingRushRecordMapper.selectList(new LambdaQueryWrapper<MarketingRushRecord>()
                    .eq(MarketingRushRecord::getMarketingRushGroupId, String.valueOf(mrg.get("id")))
                    .eq(MarketingRushRecord::getDelFlag, "0")
                    .eq(MarketingRushRecord::getStatus, "1")
                    .eq(MarketingRushRecord::getMemberListId, memberId)
                    .orderByAsc(MarketingRushRecord::getCreateTime)
            );
            mrg.put("RushGoods",marketingRushRecordList.size()+"件");
            mrg.put("goodsMainPicture",marketingRushRecordList.stream()
                    .map(MarketingRushRecord::getMainPicture)
                    .collect(Collectors.toList()));
            mrg.put("beginTime",marketingRushRecordList.size()>0? DateUtils.date2Str(marketingRushRecordList.get(0).getCreateTime(),DateUtils.datetimeFormat.get()):"-");
            mrg.put("endTime",Integer.valueOf(String.valueOf(mrg.get("transformationThreshold")))<=marketingRushRecordList.size()?DateUtils.date2Str(marketingRushRecordList.get(marketingRushRecordList.size()-1).getCreateTime(),DateUtils.datetimeFormat.get()):"-");
        });
        return marketingRushGroup;
    }

    @Override
    public Map<String, Object> getMarketingRushGroupParticulars(Page<Map<String, Object>> page, String id) {
        Map<String, Object> map = new HashMap<>();
        MarketingRushGroup marketingRushGroup = this.getById(id);
        map.put("id",marketingRushGroup.getId());
        map.put("consignmentStatus",marketingRushGroup.getConsignmentStatus());
        map.put("serialNumber",marketingRushGroup.getSerialNumber());
        map.put("ifPurchase",marketingRushGroup.getIfPurchase());
        map.put("consignmentTime",marketingRushGroup.getConsignmentStatus().equals("2")&&marketingRushGroup.getConsignmentTime()!=null?DateUtils.date2Str(marketingRushGroup.getConsignmentTime(),DateUtils.datetimeFormat.get()):"");
        MarketingRushType marketingRushType = marketingRushTypeMapper.selectById(marketingRushGroup.getMarketingRushTypeId());
        map.put("rushName",marketingRushType.getRushName());
        map.put("rushGoods",marketingRushRecordMapper.selectCount(new LambdaQueryWrapper<MarketingRushRecord>()
                .eq(MarketingRushRecord::getDelFlag,"0")
                .eq(MarketingRushRecord::getMarketingRushGroupId,marketingRushGroup.getId())
                .eq(MarketingRushRecord::getMemberListId,marketingRushGroup.getMemberListId())
                .eq(MarketingRushRecord::getStatus,"1")
        ));
        IPage<Map<String, Object>> marketingRushGoodPage = baseMapper.findMarketingRushGoodPage(page, id, marketingRushGroup.getMemberListId());
        MarketingRushBaseSetting marketingRushBaseSetting = iMarketingRushBaseSettingService.getOne(new LambdaQueryWrapper<MarketingRushBaseSetting>()
                .eq(MarketingRushBaseSetting::getDelFlag, "0")
                .orderByDesc(MarketingRushBaseSetting::getCreateTime)
                .last("limit 1")
        );
        marketingRushGoodPage.getRecords().forEach(mrgp->{
            mrgp.put("rushPrice",marketingRushType.getPrice());
            mrgp.put("label",marketingRushBaseSetting.getLabel());
        });
        map.put("rushGoodPageList",marketingRushGoodPage);
        return map;
    }

    @Override
    @Transactional
    public boolean consignmentSales(MarketingRushGroup marketingRushGroup) {
        boolean b = this.updateById(marketingRushGroup
                .setConsignmentStatus("2")
                .setConsignmentTime(new Date()));
        if (b){
            return true;
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

    }
}
