package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.marketing.entity.MarketingBusinessMakeMoneyRecord;
import org.jeecg.modules.marketing.vo.MarketingBusinessMakeMoneyRecordVO;

/**
 * @Description: 创业出账资金
 * @Author: jeecg-boot
 * @Date:   2021-08-11
 * @Version: V1.0
 */
public interface IMarketingBusinessMakeMoneyRecordService extends IService<MarketingBusinessMakeMoneyRecord>,JeecgService<MarketingBusinessMakeMoneyRecord> {

    IPage<MarketingBusinessMakeMoneyRecordVO> findMoneyRecordListByMarketingBusinessMakeMoneyId(Page<MarketingBusinessMakeMoneyRecordVO> page, String marketingBusinessMakeMoneyId);
}
