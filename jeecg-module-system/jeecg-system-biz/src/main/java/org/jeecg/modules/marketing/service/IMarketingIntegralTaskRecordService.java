package org.jeecg.modules.marketing.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingIntegralTask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.modules.marketing.entity.MarketingIntegralTaskRecord;
import org.jeecg.modules.marketing.vo.MarketingIntegralTaskRecordVO;

/**
 * @Description: 积分任务记录
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
public interface IMarketingIntegralTaskRecordService extends IService<MarketingIntegralTaskRecord> {


    /**
     * 形成任务记录
     *
     * @param marketingIntegralTask
     * @param memberListId
     * @return
     */
    public boolean addMarketingIntegralTaskRecord(MarketingIntegralTask marketingIntegralTask, String memberListId);


    IPage<MarketingIntegralTaskRecordVO> queryPageList(Page<MarketingIntegralTaskRecordVO> page, QueryWrapper<MarketingIntegralTaskRecordVO> queryWrapper);

}
