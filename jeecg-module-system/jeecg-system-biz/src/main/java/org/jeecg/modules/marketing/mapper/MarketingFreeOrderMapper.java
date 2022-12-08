package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingFreeOrder;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单订单
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface MarketingFreeOrderMapper extends BaseMapper<MarketingFreeOrder> {

    /**
     * 场次分组订单信息
     *
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> selectMarketingFreeOrderGroupByPayTime(@Param("paramMap") Map<String,Object> paramMap);


    /**
     * 根据场次id查询订单信息
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> selectMarketingFreeOrderByMarketingFreeSessionId(Page<Map<String,Object>> page, @Param("paramMap") Map<String, Object> paramMap);

}
