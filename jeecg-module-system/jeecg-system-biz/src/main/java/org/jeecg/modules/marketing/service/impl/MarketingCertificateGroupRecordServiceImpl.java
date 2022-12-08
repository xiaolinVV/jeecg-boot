package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupRecord;
import org.jeecg.modules.marketing.mapper.MarketingCertificateGroupRecordMapper;
import org.jeecg.modules.marketing.service.IMarketingCertificateGroupRecordService;
import org.jeecg.modules.marketing.vo.MarketingCertificateGroupRecordVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼好券记录
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Service
public class MarketingCertificateGroupRecordServiceImpl extends ServiceImpl<MarketingCertificateGroupRecordMapper, MarketingCertificateGroupRecord> implements IMarketingCertificateGroupRecordService {

    @Override
    public IPage<MarketingCertificateGroupRecordVO> queryPageList(Page<MarketingCertificateGroupRecordVO> page, QueryWrapper<MarketingCertificateGroupRecordVO> queryWrapper) {
        return baseMapper.queryPageList(page,queryWrapper);
    }

    @Override
    public List<Map<String, Object>> getMemberList() {
        return baseMapper.getMemberList();
    }

    @Override
    public List<Map<String, Object>> getMemberListByMarketingCertificateGroupManageId(String marketingCertificateGroupManageId) {
        return baseMapper.getMemberListByMarketingCertificateGroupManageId(marketingCertificateGroupManageId);
    }

    @Override
    public List<MarketingCertificateGroupRecordVO> getMarketingCertificateGroupManageRecordList(String id) {
        return baseMapper.getMarketingCertificateGroupManageRecordList(id);
    }
}
