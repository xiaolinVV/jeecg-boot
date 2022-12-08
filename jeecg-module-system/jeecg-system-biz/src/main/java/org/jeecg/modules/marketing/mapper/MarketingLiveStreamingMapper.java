package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingLiveStreaming;

import java.util.Map;

/**
 * @Description: 直播管理
 * @Author: jeecg-boot
 * @Date:   2021-04-24
 * @Version: V1.0
 */
public interface MarketingLiveStreamingMapper extends BaseMapper<MarketingLiveStreaming> {

    /**
     * 获取直播列表
     *
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> getMarketingLiveStreamingList(Page<Map<String,Object>> page);

    /**
     * 根据直播id获取直播详情
     *
     * @param id
     * @return
     */
    public Map<String,Object> getMarketingLiveStreamingListById(@Param("id") String id);

}
