package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.dto.MarketingLeagueGoodListDTO;
import org.jeecg.modules.marketing.entity.MarketingLeagueGoodList;
import org.jeecg.modules.marketing.mapper.MarketingLeagueGoodListMapper;
import org.jeecg.modules.marketing.service.IMarketingLeagueGoodListService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 加盟专区-商品列表
 * @Author: jeecg-boot
 * @Date:   2021-12-23
 * @Version: V1.0
 */
@Service
public class MarketingLeagueGoodListServiceImpl extends ServiceImpl<MarketingLeagueGoodListMapper, MarketingLeagueGoodList> implements IMarketingLeagueGoodListService {

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, MarketingLeagueGoodListDTO marketingLeagueGoodListDTO) {
        return baseMapper.queryPageList(page,marketingLeagueGoodListDTO);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingLeagueGoodListByTypeId(Page<Map<String, Object>> page, String typeId) {
        return baseMapper.getMarketingLeagueGoodListByTypeId(page,typeId);
    }
}
