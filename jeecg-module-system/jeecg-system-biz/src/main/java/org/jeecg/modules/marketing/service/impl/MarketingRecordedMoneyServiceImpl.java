package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingRecordedMoneyDTO;
import org.jeecg.modules.marketing.entity.MarketingRecordedMoney;
import org.jeecg.modules.marketing.mapper.MarketingRecordedMoneyMapper;
import org.jeecg.modules.marketing.service.IMarketingRecordedMoneyService;
import org.jeecg.modules.marketing.vo.MarketingRecordedMoneyVO;
import org.springframework.stereotype.Service;

/**
 * @Description: 称号入账资金
 * @Author: jeecg-boot
 * @Date:   2020-06-17
 * @Version: V1.0
 */
@Service
public class MarketingRecordedMoneyServiceImpl extends ServiceImpl<MarketingRecordedMoneyMapper, MarketingRecordedMoney> implements IMarketingRecordedMoneyService {

    @Override
    public IPage<MarketingRecordedMoneyVO> queryPageList(Page<MarketingRecordedMoney> page, MarketingRecordedMoneyDTO marketingRecordedMoneyDTO) {
        return baseMapper.queryPageList(page,marketingRecordedMoneyDTO);
    }
}
