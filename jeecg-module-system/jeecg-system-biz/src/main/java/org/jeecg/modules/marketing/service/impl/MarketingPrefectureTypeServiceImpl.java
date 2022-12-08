package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGood;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGoodSpecification;
import org.jeecg.modules.marketing.entity.MarketingPrefectureType;
import org.jeecg.modules.marketing.mapper.MarketingPrefectureMapper;
import org.jeecg.modules.marketing.mapper.MarketingPrefectureTypeMapper;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodSpecificationService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description: 平台专区分类
 * @Author: jeecg-boot
 * @Date: 2020-03-25
 * @Version: V1.0
 */
@Service
public class MarketingPrefectureTypeServiceImpl extends ServiceImpl<MarketingPrefectureTypeMapper, MarketingPrefectureType> implements IMarketingPrefectureTypeService {
    @Autowired
    private IMarketingPrefectureGoodService iMarketingPrefectureGoodService;
    @Autowired
    private IMarketingPrefectureGoodSpecificationService iMarketingPrefectureGoodSpecificationService;
    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;
    @Resource
    private MarketingPrefectureTypeMapper marketingPrefectureTypeMapper;
    @Resource
    private MarketingPrefectureMapper marketingPrefectureMapper;


    @Override
    public List<Map<String, Object>> findByMarketingPrefectureId(String id) {
        return baseMapper.findByMarketingPrefectureId(id);
    }


    /**
     * 删除分类 关联删除 商品
     */
    @Override
    public void linkToDelete(String ids) {
        Result<MarketingPrefecture> result = new Result<MarketingPrefecture>();
        if (StringUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
        } else {
            List<MarketingPrefectureGood> marketingPrefectureGoodList;
            List<MarketingPrefectureGoodSpecification> marketingPrefectureGoodSpecificationList;

            List<String> listid = Arrays.asList(ids.split(","));
            QueryWrapper<MarketingPrefectureGood> queryWrapperMarketingPrefectureGood = new QueryWrapper();
            QueryWrapper<MarketingPrefectureGoodSpecification> queryWrapperMarketingPrefectureGoodSpecification = new QueryWrapper();


            //查询关联专区商品
            queryWrapperMarketingPrefectureGood = new QueryWrapper();
            queryWrapperMarketingPrefectureGood.in("marketing_prefecture_type_id", listid);
            marketingPrefectureGoodList = iMarketingPrefectureGoodService.list(queryWrapperMarketingPrefectureGood);

            if (marketingPrefectureGoodList.size() == 0) {
                // result.error500("未找到对应实体");
            } else {
                List<String> marketingPrefectureGoodids = new ArrayList<>();
                for (MarketingPrefectureGood marketingPrefectureGood : marketingPrefectureGoodList) {
                    marketingPrefectureGoodids.add(marketingPrefectureGood.getId());
                }
                //规格
                queryWrapperMarketingPrefectureGoodSpecification = new QueryWrapper();
                queryWrapperMarketingPrefectureGoodSpecification.in("marketing_prefecture_good_id", marketingPrefectureGoodids);
                marketingPrefectureGoodSpecificationList = iMarketingPrefectureGoodSpecificationService.list(queryWrapperMarketingPrefectureGoodSpecification);
                List<String> marketingPrefectureGoodidSpecificationIds = new ArrayList<>();
                for (MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification : marketingPrefectureGoodSpecificationList) {
                    marketingPrefectureGoodidSpecificationIds.add(marketingPrefectureGoodSpecification.getId());
                }
                //删除规格
                iMarketingPrefectureGoodSpecificationService.removeByIds(marketingPrefectureGoodidSpecificationIds);
                //删除关联商品
                iMarketingPrefectureGoodService.removeByIds(marketingPrefectureGoodids);
            }
        }
    }

    /**
     * 修改停用专区 关联修改下架 商品 分类 广告
     */
    @Override
    public void linkToUpdateStatus(String ids) {
        Result<MarketingPrefecture> result = new Result<MarketingPrefecture>();
        if (StringUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
        } else {
            List<MarketingPrefectureGood> marketingPrefectureGoodList;


            List<String> listid = Arrays.asList(ids.split(","));
            QueryWrapper<MarketingPrefectureGood> queryWrapperMarketingPrefectureGood = new QueryWrapper();
            //查询关联专区商品
            queryWrapperMarketingPrefectureGood = new QueryWrapper();
            queryWrapperMarketingPrefectureGood.in("marketing_prefecture_type_id", listid);
            queryWrapperMarketingPrefectureGood.in("status", "1");
            marketingPrefectureGoodList = iMarketingPrefectureGoodService.list(queryWrapperMarketingPrefectureGood);

            if (marketingPrefectureGoodList.size() == 0) {
                // result.error500("未找到对应实体");
            } else {
                marketingPrefectureGoodList.forEach(mgl -> {
                    //停用
                    mgl.setStatus("0");
                    //删除关联商品
                    iMarketingPrefectureGoodService.updateById(mgl);
                });

            }

        }
    }


    /**
     * 修改专区商品启用判断是否可启用
     */
    @Override
    public Map<String, Object> linkToUpdate(String marketingPrefectureTypeId) {
        //Result<MarketingPrefecture> result = new Result<MarketingPrefecture>();
        Map<String, Object> map = Maps.newHashMap();
        MarketingPrefectureType marketingPrefectureType = baseMapper.selectById(marketingPrefectureTypeId);
        if (marketingPrefectureType == null) {
            map.put("data", "1");// 0 :修改成功 1:修改失败
            map.put("msg", "专区商品未找到！");

            return map;
        }
        QueryWrapper<MarketingPrefecture> queryWrapperMarketingPrefecture = new QueryWrapper();
        queryWrapperMarketingPrefecture.eq("id", marketingPrefectureType.getMarketingPrefectureId());
        queryWrapperMarketingPrefecture.eq("status", "1");
        MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getOne(queryWrapperMarketingPrefecture);
        if (marketingPrefecture == null) {
            map.put("data", "1");// 0 :修改成功 1:修改失败
            map.put("msg", "专区被停用,先去启用专区！");
            return map;
        }
        if (marketingPrefecture.getIsViewType().equals("0")) {
            map.put("data", "1");// 0 :修改成功 1:修改失败
            map.put("msg", "专区已改为无分类专区,先去修改专区！");
            return map;
        }
        marketingPrefectureType.setStatus("1");
        baseMapper.updateById(marketingPrefectureType);
        map.put("data", "0");// 0 :修改成功 1:修改失败
        map.put("msg", "可以启用!");
        return map;
    }

    @Override
    public List<Map<String, Object>> findUnderlingListMap(String id) {
        return baseMapper.findUnderlingListMap(id);
    }

    @Override
    public List<Map<String, Object>> findMarketingPrefectureTypeTwoById(String id) {
        return baseMapper.findMarketingPrefectureTypeTwoById(id);
    }

    @Override
    public List<MarketingPrefectureType> getUnderlingList(String id) {
        return baseMapper.getUnderlingList(id);
    }


    @Override

    public List<Map<String, Object>> getMarketingPrefectureTypeAll(String marketingPrefectureId) {

        List<MarketingPrefectureType> marketingPrefectureTypes = selectList(marketingPrefectureId);

        List<Map<String, Object>> list = new ArrayList<>();
        for (MarketingPrefectureType marketingPrefectureType : marketingPrefectureTypes) {

            HashMap<String, Object> oneLevelMap = new HashMap<>();
            oneLevelMap.put("label", marketingPrefectureType.getTypeName());
            oneLevelMap.put("value", marketingPrefectureType.getId());

            QueryWrapper<MarketingPrefectureType> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pid", marketingPrefectureType.getId());
            queryWrapper.eq("del_flag", "0");
            queryWrapper.eq("status", "1");
            List<MarketingPrefectureType> marketingPrefectureTypesTwo = marketingPrefectureTypeMapper.selectList(queryWrapper);
            if (marketingPrefectureTypesTwo.size() > 0) {
                ArrayList<Map<String, String>> childList = new ArrayList<>();
                for (MarketingPrefectureType prefectureType : marketingPrefectureTypesTwo) {
                    HashMap<String, String> twoLevelMap = new HashMap<>();
                    twoLevelMap.put("label", prefectureType.getTypeName());
                    twoLevelMap.put("value", prefectureType.getId());
                    childList.add(twoLevelMap);
                }
                oneLevelMap.put("children", childList);
            }
            list.add(oneLevelMap);
        }
        return list;
    }

    @SuppressWarnings("all")
    private List<MarketingPrefectureType> selectList(String marketingPrefectureId) {

        QueryWrapper<MarketingPrefectureType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("marketing_prefecture_id", marketingPrefectureId);
        queryWrapper.eq("del_flag", "0");
        queryWrapper.eq("status", "1");
        queryWrapper.eq("level", "1");
        queryWrapper.orderByAsc("sort");
        return marketingPrefectureTypeMapper.selectList(queryWrapper);

    }


}
