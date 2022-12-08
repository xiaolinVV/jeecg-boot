package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingBusinessMakeMoney;
import org.jeecg.modules.marketing.mapper.MarketingBusinessMakeMoneyMapper;
import org.jeecg.modules.marketing.service.IMarketingBusinessMakeMoneyService;
import org.jeecg.modules.marketing.vo.MarketingBusinessMakeMoneyVO;
import org.springframework.stereotype.Service;

/**
 * @Description: 创业进账资金
 * @Author: jeecg-boot
 * @Date:   2021-08-11
 * @Version: V1.0
 */
@Service
public class MarketingBusinessMakeMoneyServiceImpl extends ServiceImpl<MarketingBusinessMakeMoneyMapper, MarketingBusinessMakeMoney> implements IMarketingBusinessMakeMoneyService {

    @Override
    public IPage<MarketingBusinessMakeMoneyVO> queryPageList(Page<MarketingBusinessMakeMoneyVO> page, QueryWrapper<MarketingBusinessMakeMoney> queryWrapper) {
        return baseMapper.queryPageList(page,queryWrapper);
    }
}
