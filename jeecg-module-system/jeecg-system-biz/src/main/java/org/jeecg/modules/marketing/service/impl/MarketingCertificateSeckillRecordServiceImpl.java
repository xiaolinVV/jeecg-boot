package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillRecord;
import org.jeecg.modules.marketing.mapper.MarketingCertificateSeckillRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingCertificateSeckillRecordService;
import org.jeecg.modules.marketing.vo.MarketingCertificateSeckillRecordVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 购买记录
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Service
public class MarketingCertificateSeckillRecordServiceImpl extends ServiceImpl<MarketingCertificateSeckillRecordMapper, MarketingCertificateSeckillRecord> implements IMarketingCertificateSeckillRecordService {

    @Override
    public List<Map<String, Object>> dataList(String marketingCertificateSeckillActivityListId) {
        return baseMapper.dataList(marketingCertificateSeckillActivityListId);
    }

    @Override
    public IPage<MarketingCertificateSeckillRecordVO> queryPageList(Page<MarketingCertificateSeckillRecordVO> page, QueryWrapper<MarketingCertificateSeckillRecordVO> queryWrapper) {
        return baseMapper.queryPageList(page,queryWrapper);
    }

    @Override
    public List<Map<String, Object>> lookQqzixuanguByPayId(String payId) {
        return baseMapper.lookQqzixuanguByPayId(payId);
    }
}
