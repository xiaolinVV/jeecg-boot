package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingBusinessMakeMoneyRecord;
import org.jeecg.modules.marketing.vo.MarketingBusinessMakeMoneyRecordVO;

/**
 * @Description: 创业出账资金
 * @Author: jeecg-boot
 * @Date:   2021-08-11
 * @Version: V1.0
 */
public interface MarketingBusinessMakeMoneyRecordMapper extends BaseMapper<MarketingBusinessMakeMoneyRecord> {

    IPage<MarketingBusinessMakeMoneyRecordVO> findMoneyRecordListByMarketingBusinessMakeMoneyId(Page<MarketingBusinessMakeMoneyRecordVO> page, @Param("marketingBusinessMakeMoneyId") String marketingBusinessMakeMoneyId);
}
