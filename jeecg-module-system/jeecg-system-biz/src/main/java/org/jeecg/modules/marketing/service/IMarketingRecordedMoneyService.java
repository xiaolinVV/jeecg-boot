package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingRecordedMoneyDTO;
import org.jeecg.modules.marketing.entity.MarketingRecordedMoney;
import org.jeecg.modules.marketing.vo.MarketingRecordedMoneyVO;

/**
 * @Description: 称号入账资金
 * @Author: jeecg-boot
 * @Date:   2020-06-17
 * @Version: V1.0
 */
public interface IMarketingRecordedMoneyService extends IService<MarketingRecordedMoney> {

    IPage<MarketingRecordedMoneyVO> queryPageList(Page<MarketingRecordedMoney> page, MarketingRecordedMoneyDTO marketingRecordedMoneyDTO);
}
