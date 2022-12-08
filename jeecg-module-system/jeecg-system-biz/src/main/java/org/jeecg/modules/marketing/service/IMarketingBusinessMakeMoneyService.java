package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.marketing.entity.MarketingBusinessMakeMoney;
import org.jeecg.modules.marketing.vo.MarketingBusinessMakeMoneyVO;

/**
 * @Description: 创业进账资金
 * @Author: jeecg-boot
 * @Date:   2021-08-11
 * @Version: V1.0
 */
public interface IMarketingBusinessMakeMoneyService extends IService<MarketingBusinessMakeMoney>,JeecgService<MarketingBusinessMakeMoney> {

    IPage<MarketingBusinessMakeMoneyVO> queryPageList(Page<MarketingBusinessMakeMoneyVO> page, QueryWrapper<MarketingBusinessMakeMoney> queryWrapper);
}
