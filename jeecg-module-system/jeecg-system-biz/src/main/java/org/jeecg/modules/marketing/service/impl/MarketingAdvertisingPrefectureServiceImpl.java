package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.marketing.dto.MarketingAdvertisingPrefectureDTO;
import org.jeecg.modules.marketing.entity.MarketingAdvertisingPrefecture;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.mapper.MarketingAdvertisingPrefectureMapper;
import org.jeecg.modules.marketing.service.IMarketingAdvertisingPrefectureService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.marketing.vo.MarketingAdvertisingPrefectureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 专区广告
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
@Service
@Slf4j
public class MarketingAdvertisingPrefectureServiceImpl extends ServiceImpl<MarketingAdvertisingPrefectureMapper, MarketingAdvertisingPrefecture> implements IMarketingAdvertisingPrefectureService {
@Autowired
private IMarketingPrefectureService iMarketingPrefectureService;
    @Override
    public List<Map<String, Object>> findByMarketingPrefectureId(String id) {
        return baseMapper.findByMarketingPrefectureId(id);
    }

    /**
     * 查询专区广告推广集合
     * @param page
     * @param marketingAdvertisingPrefectureVO
     * @return
     */
    @Override
    public  IPage<MarketingAdvertisingPrefectureDTO> getMarketingAdvertisingPrefectureDTO(Page<MarketingAdvertisingPrefecture> page, MarketingAdvertisingPrefectureVO marketingAdvertisingPrefectureVO){
        return baseMapper.getMarketingAdvertisingPrefectureDTO(page,marketingAdvertisingPrefectureVO);
    };

    /**
     * 修改专区商品启用判断是否可启用
     */
    @Override
    public Map<String,Object> linkToUpdate(String marketingAdvertisingPrefectureId) {
        //Result<MarketingPrefecture> result = new Result<MarketingPrefecture>();
        Map<String,Object> map = Maps.newHashMap();
        MarketingAdvertisingPrefecture marketingAdvertisingPrefecture = baseMapper.selectById(marketingAdvertisingPrefectureId);
        if(marketingAdvertisingPrefecture ==null){
            map.put("data","1");// 0 :修改成功 1:修改失败
            map.put("msg","专区商品未找到！");

            return map;
        }
        QueryWrapper<MarketingPrefecture> queryWrapperMarketingPrefecture = new QueryWrapper();
        queryWrapperMarketingPrefecture.eq("id",marketingAdvertisingPrefecture.getMarketingPrefectureId());
        queryWrapperMarketingPrefecture.eq("status","1");
        MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getOne(queryWrapperMarketingPrefecture);
        if (marketingPrefecture == null) {
            map.put("data","1");// 0 :修改成功 1:修改失败
            map.put("msg","专区被停用,先去启用专区！");
            return map;
        }
        if(marketingPrefecture.getIsViewType().equals("0")){
            map.put("data","1");// 0 :修改成功 1:修改失败
            map.put("msg","专区已改为无分类专区,先去修改专区！");
            return map;
        }
        marketingAdvertisingPrefecture.setStatus("1");
        baseMapper.updateById(marketingAdvertisingPrefecture);
        map.put("data", "0");// 0 :修改成功 1:修改失败
        map.put("msg", "可以启用!");
        return map;
    }

    /**
     * 过期时间停用推荐广告
     */
    @Override
    public void   getMarketingAdvertisingPrefectureOvertime() {
        log.info("专区广告时间过期被停用专区广告定时器启动!!!");
        List<MarketingAdvertisingPrefecture> marketingAdvertisingList = baseMapper.selectList(new LambdaQueryWrapper<MarketingAdvertisingPrefecture>()
                .eq(MarketingAdvertisingPrefecture::getStatus, "1")
                .lt(MarketingAdvertisingPrefecture::getEndTime, new Date()));
        marketingAdvertisingList.forEach(mp -> {
            log.info("专区广告时间过期被停用广告:" + mp.getTitle() + "推荐分类id:" + mp.getId());
            //停用
            mp.setStatus("0");
            baseMapper.updateById(mp);

        });
    }

}
