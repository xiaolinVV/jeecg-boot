package org.jeecg.modules.marketing.service.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.jeecg.modules.marketing.entity.MarketingSearchterm;
import org.jeecg.modules.marketing.mapper.MarketingSearchtermMapper;
import org.jeecg.modules.marketing.service.IMarketingSearchtermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 推荐搜词
 * @Author: jeecg-boot
 * @Date:   2019-10-11
 * @Version: V1.0
 */
@Service
@Slf4j
public class MarketingSearchtermServiceImpl extends ServiceImpl<MarketingSearchtermMapper, MarketingSearchterm> implements IMarketingSearchtermService {
    @Autowired
    public MarketingSearchtermMapper marketingSearchtermMapper;
   @Override
    public List<MarketingSearchterm> getmarketingSearchtermList(){
        return marketingSearchtermMapper.getmarketingSearchtermList();
    }
}
