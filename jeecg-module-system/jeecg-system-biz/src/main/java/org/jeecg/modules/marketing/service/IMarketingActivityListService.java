package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingActivityList;

import java.util.Map;

/**
 * @Description: 活动列表
 * @Author: jeecg-boot
 * @Date:   2021-05-17
 * @Version: V1.0
 */
public interface IMarketingActivityListService extends IService<MarketingActivityList> {


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
    public Map<String,Object> getMarketingActivityListById(Map<String,Object> paramMap);


    /**
     * 状态变更，定时器驱动
     */
    public void updateStatus();

}
