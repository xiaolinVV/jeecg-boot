package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupList;
import org.jeecg.modules.marketing.mapper.MarketingCertificateGroupListMapper;
import org.jeecg.modules.marketing.service.IMarketingCertificateGroupListService;
import org.jeecg.modules.marketing.vo.MarketingCertificateGroupListVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼好券
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Service
public class MarketingCertificateGroupListServiceImpl extends ServiceImpl<MarketingCertificateGroupListMapper, MarketingCertificateGroupList> implements IMarketingCertificateGroupListService {

    @Override
    public IPage<MarketingCertificateGroupListVO> queryPageList(Page<MarketingCertificateGroupListVO> page, QueryWrapper<MarketingCertificateGroupListVO> queryWrapper) {
        return baseMapper.queryPageList(page,queryWrapper);
    }

    @Override
    public List<Map<String, Object>> pageListBySout(Integer sout) {
        return baseMapper.pageListBySout(sout);
    }

    @Override
    public IPage<Map<String, Object>> pageListByMarketingCertificateGroupList(Page<Map<String, Object>> page) {
        return baseMapper.pageListByMarketingCertificateGroupList(page);
    }
}
