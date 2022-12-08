package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingActivityListRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingActivityListRecord;

import java.util.Map;

/**
 * @Description: 活动记录
 * @Author: jeecg-boot
 * @Date:   2021-05-17
 * @Version: V1.0
 */
public interface IMarketingActivityListRecordService extends IService<MarketingActivityListRecord> {



    /**
     * 查询记录列表
     *
     * @param page
     * @return
     */
    public IPage<MarketingActivityListRecord> getMarketingActivityListRecordList(Page<MarketingActivityListRecord> page, MarketingActivityListRecordDTO marketingActivityListRecordDTO);

    /**
     * 获取会员的活动记录
     *
     * @param page
     * @param memberId
     * @return
     */
    public IPage<Map<String,Object>> getmarketingActivityListRecordByMemberId(Page<Map<String,Object>> page,String memberId);

}
