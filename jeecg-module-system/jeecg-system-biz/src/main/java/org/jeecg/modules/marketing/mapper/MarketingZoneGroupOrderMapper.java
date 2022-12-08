package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupOrder;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼中商品
 * @Author: jeecg-boot
 * @Date:   2021-07-26
 * @Version: V1.0
 */
public interface MarketingZoneGroupOrderMapper extends BaseMapper<MarketingZoneGroupOrder> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param("requestMap") Map<String, Object> requestMap);

    IPage<Map<String,Object>> marketingZoneGroupOrderRecord(Page<Map<String,Object>> page, @Param(Constants.WRAPPER) QueryWrapper<MarketingZoneGroupOrder> queryWrapper,@Param("requestMap") Map<String, Object> requestMap);

    Map<String,Object> memberRecord(@Param("id") String id);

    Map<String,Object> goodInfo(@Param("id") String id);

    List<Map<String,Object>> listByGroupingId(@Param("id") String id);

    IPage<Map<String,Object>> getMarketingZoneGroupOrderPageByGroupingId(Page<Map<String,Object>> page,@Param("id") String id);
}
