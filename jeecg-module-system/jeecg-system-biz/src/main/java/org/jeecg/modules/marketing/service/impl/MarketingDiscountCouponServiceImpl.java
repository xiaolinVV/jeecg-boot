package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingDiscountCouponDTO;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.marketing.mapper.MarketingDiscountCouponMapper;
import org.jeecg.modules.marketing.service.IMarketingDiscountCouponService;
import org.jeecg.modules.marketing.vo.MarketingDiscountCouponVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description: 优惠券记录
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */

@Service
public class MarketingDiscountCouponServiceImpl extends
        ServiceImpl<MarketingDiscountCouponMapper, MarketingDiscountCoupon>
        implements IMarketingDiscountCouponService {
    @Override
    public IPage<MarketingDiscountCouponDTO> couponVerification(Page<MarketingDiscountCouponVO> page, String qqzixuangu, String sysUserId){
        return baseMapper.couponVerification(page,qqzixuangu,sysUserId);
    }

    @Override
    public IPage<MarketingDiscountCouponVO> findDiscountCoupon(Page<MarketingDiscountCouponVO> page, MarketingDiscountCouponVO marketingDiscountCouponVO) {
        return baseMapper.findDiscountCoupon(page,marketingDiscountCouponVO);
    }

    @Override
    public IPage<Map<String, Object>> findMarketingDiscountCouponByMemberId(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.findMarketingDiscountCouponByMemberId(page,paramMap);
    }

    @Override
    public Map<String, Object> findMarketingDiscountCouponInfo(String id) {
        return baseMapper.findMarketingDiscountCouponInfo(id);
    }

    @Override
    public List<Map<String, Object>> findMarketingDiscountCouponByGoodIds(Map<String, Object> paramMap) {
        return baseMapper.findMarketingDiscountCouponByGoodIds(paramMap);
    }

    @Override
    public IPage<MarketingDiscountCouponVO> findDiscountCouponTarrace(Page<MarketingDiscountCouponVO> page, MarketingDiscountCouponVO marketingDiscountCouponVO) {
        return baseMapper.findDiscountCouponTarrace(page,marketingDiscountCouponVO);
    }

    @Override
    @Transactional
    public void updateMarketingDiscountCouponJob() {
        baseMapper.updateMarketingDiscountTakeEffect();
        baseMapper.updateMarketingDiscountPastDue();
    }

    ;
}