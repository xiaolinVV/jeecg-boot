package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralManage;
import org.jeecg.modules.marketing.mapper.MarketingGroupIntegralManageMapper;
import org.jeecg.modules.marketing.service.IMarketingGroupIntegralManageService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 拼购管理
 * @Author: jeecg-boot
 * @Date:   2021-06-27
 * @Version: V1.0
 */
@Service
public class MarketingGroupIntegralManageServiceImpl extends ServiceImpl<MarketingGroupIntegralManageMapper, MarketingGroupIntegralManage> implements IMarketingGroupIntegralManageService {

    @Override
    public IPage<Map<String, Object>> getMarketingGroupIntegralManage(Page<Map<String, Object>> page) {
        return baseMapper.getMarketingGroupIntegralManage(page);
    }
}
