package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingActivityList;

import java.util.Map;

/**
 * @Description: 活动列表
 * @Author: jeecg-boot
 * @Date:   2021-05-17
 * @Version: V1.0
 */
public interface MarketingActivityListMapper extends BaseMapper<MarketingActivityList> {

    /**
     * 查询活动列表
     *
     * @param page
     * @return
     */
    public IPage<MarketingActivityList> getMarketingActivityList(Page<MarketingActivityList> page);


    /**
     * 获取活动详情
     *
     * @param paramMap
     * @return
     */
    public Map<String,Object> getMarketingActivityListById(@Param("paramMap") Map<String,Object> paramMap);

}
