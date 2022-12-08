package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupGood;

import java.util.Map;

/**
 * @Description: 专区商品
 * @Author: jeecg-boot
 * @Date:   2021-07-23
 * @Version: V1.0
 */
public interface MarketingZoneGroupGoodMapper extends BaseMapper<MarketingZoneGroupGood> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param(Constants.WRAPPER) QueryWrapper<MarketingZoneGroupGood> queryWrapper,@Param("requestMap") Map<String, Object> requestMap);


    /**
     * 根据专区id获取专区团商品
     *
     * @param page
     * @param marketingZoneGroupId
     * @return
     */
    public IPage<Map<String,Object>> getByMarketingZoneGroupId(Page<Map<String,Object>> page,@Param("marketingZoneGroupId") String marketingZoneGroupId);
}
