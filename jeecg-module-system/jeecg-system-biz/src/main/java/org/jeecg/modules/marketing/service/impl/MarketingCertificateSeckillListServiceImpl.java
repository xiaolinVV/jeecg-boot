package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillList;
import org.jeecg.modules.marketing.mapper.MarketingCertificateSeckillListMapper;
import org.jeecg.modules.marketing.service.IMarketingCertificateSeckillListService;
import org.jeecg.modules.marketing.vo.MarketingCertificateSeckillListVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 限时抢券列表
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Service
public class MarketingCertificateSeckillListServiceImpl extends ServiceImpl<MarketingCertificateSeckillListMapper, MarketingCertificateSeckillList> implements IMarketingCertificateSeckillListService {

    @Override
    public List<Map<String, Object>> pageListBySout(String marketingCertificateSeckillActivityListId, Integer sout) {
        return baseMapper.pageListBySout(marketingCertificateSeckillActivityListId,sout);
    }

    @Override
    public IPage<MarketingCertificateSeckillListVO> queryPageList(Page<MarketingCertificateSeckillListVO> page, QueryWrapper<MarketingCertificateSeckillListVO> queryWrapper) {
        return baseMapper.queryPageList(page,queryWrapper);
    }

    @Override
    public IPage<Map<String, Object>> pageListByMarketingCertificateSeckillActivityListId(Page<Map<String, Object>> page, String marketingCertificateSeckillActivityListId) {
        return baseMapper.pageListByMarketingCertificateSeckillActivityListId(page,marketingCertificateSeckillActivityListId);
    }
}
