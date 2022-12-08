package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingEnterMoneyDTO;
import org.jeecg.modules.marketing.entity.MarketingEnterMoney;
import org.jeecg.modules.marketing.vo.MarketingEnterMoneyVO;

/**
 * @Description: 称号出账资金
 * @Author: jeecg-boot
 * @Date:   2020-06-17
 * @Version: V1.0
 */
public interface IMarketingEnterMoneyService extends IService<MarketingEnterMoney> {

    IPage<MarketingEnterMoneyVO> queryPageList(Page<MarketingEnterMoney> page, MarketingEnterMoneyDTO marketingEnterMoneyDTO);

    void marketingGiftBagShareProfit();

}
