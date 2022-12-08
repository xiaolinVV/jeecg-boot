package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupManage;
import org.jeecg.modules.marketing.mapper.MarketingCertificateGroupManageMapper;
import org.jeecg.modules.marketing.service.IMarketingCertificateGroupManageService;
import org.jeecg.modules.marketing.vo.MarketingCertificateGroupManageVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼好券管理
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Service
public class MarketingCertificateGroupManageServiceImpl extends ServiceImpl<MarketingCertificateGroupManageMapper, MarketingCertificateGroupManage> implements IMarketingCertificateGroupManageService {

    @Override
    public List<Map<String, Object>> dataList() {
        return baseMapper.dataList();
    }

    @Override
    public IPage<Map<String, Object>> myMarketingCertificateGroupManage(Page<Map<String, Object>> page,String memberId, String status) {
        return baseMapper.myMarketingCertificateGroupManage(page,memberId,status);
    }

    @Override
    public IPage<MarketingCertificateGroupManageVO> queryPageList(Page<MarketingCertificateGroupManageVO> page, QueryWrapper<MarketingCertificateGroupManage> queryWrapper) {
        return baseMapper.queryPageList(page,queryWrapper);
    }

    @Override
    public Map<String, Object> getInfo(String id) {
        return baseMapper.getInfo(id);
    }
}
