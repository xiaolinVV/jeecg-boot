package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingBusinessMakeMoneyRecord;
import org.jeecg.modules.marketing.mapper.MarketingBusinessMakeMoneyRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingBusinessMakeMoneyRecordService;
import org.jeecg.modules.marketing.vo.MarketingBusinessMakeMoneyRecordVO;
import org.springframework.stereotype.Service;

/**
 * @Description: 创业出账资金
 * @Author: jeecg-boot
 * @Date:   2021-08-11
 * @Version: V1.0
 */
@Service
public class MarketingBusinessMakeMoneyRecordServiceImpl extends ServiceImpl<MarketingBusinessMakeMoneyRecordMapper, MarketingBusinessMakeMoneyRecord> implements IMarketingBusinessMakeMoneyRecordService {

    @Override
    public IPage<MarketingBusinessMakeMoneyRecordVO> findMoneyRecordListByMarketingBusinessMakeMoneyId(Page<MarketingBusinessMakeMoneyRecordVO> page, String marketingBusinessMakeMoneyId) {
        return baseMapper.findMoneyRecordListByMarketingBusinessMakeMoneyId(page,marketingBusinessMakeMoneyId);
    }
}
