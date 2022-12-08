package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingLiveStreaming;

import java.util.Map;

/**
 * @Description: 直播管理
 * @Author: jeecg-boot
 * @Date:   2021-04-24
 * @Version: V1.0
 */
public interface IMarketingLiveStreamingService extends IService<MarketingLiveStreaming> {


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
    public Map<String,Object> getMarketingLiveStreamingListById(String id);


}
