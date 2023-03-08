package org.jeecg.modules.marketing.service.impl;

import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.marketing.dto.MarketingDiscountCouponDTO;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.marketing.entity.MarketingDiscountCouponRecord;
import org.jeecg.modules.marketing.mapper.MarketingDiscountCouponMapper;
import org.jeecg.modules.marketing.service.IMarketingDiscountCouponRecordService;
import org.jeecg.modules.marketing.service.IMarketingDiscountCouponService;
import org.jeecg.modules.marketing.vo.MarketingDiscountCouponVO;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IMarketingDiscountCouponRecordService marketingDiscountCouponRecordService;

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

    @Override
    public void sendBackOrderMarketingDiscountCoupon(OrderList orderList) {
        if(StringUtils.isNotBlank(orderList.getMarketingDiscountCouponId())){
            this.updateById(new MarketingDiscountCoupon().setId(orderList.getMarketingDiscountCouponId()).setStatus("1"));
        }
    }

    @Override
    public void sendBackOrderStoreMarketingDiscountCoupon(OrderStoreList orderStoreList) {
        if (StringUtils.isBlank(orderStoreList.getMarketingDiscountCouponId())) {
            return;
        }
        MarketingDiscountCoupon marketingDiscountCoupon = this.getById(orderStoreList.getMarketingDiscountCouponId());
        if (marketingDiscountCoupon == null) {
            return;
        }
        if (StrUtil.equals(marketingDiscountCoupon.getIsNomal(),"2")) {
            //折扣券，扣除已使用金额、删除使用记录
            LambdaQueryWrapper<MarketingDiscountCouponRecord> marketingDiscountCouponRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
            marketingDiscountCouponRecordLambdaQueryWrapper.eq(MarketingDiscountCouponRecord::getOrderStoreListId,orderStoreList.getId())
                    .eq(MarketingDiscountCouponRecord::getMarketingDiscountCouponId,marketingDiscountCoupon.getId());
            MarketingDiscountCouponRecord marketingDiscountCouponRecord = marketingDiscountCouponRecordService.getOne(marketingDiscountCouponRecordLambdaQueryWrapper, false);
            if (marketingDiscountCouponRecord != null) {
                marketingDiscountCouponRecord.setDelReason("取消订单，扣除折扣券已使用金额，删除折扣券使用记录~");
                marketingDiscountCouponRecordService.removeById(marketingDiscountCouponRecord);
                if (marketingDiscountCoupon.getDiscountUseAmount().compareTo(marketingDiscountCouponRecord.getDiscountUseAmount()) >= 0) {
                    marketingDiscountCoupon.setDiscountUseAmount(NumberUtil.sub(marketingDiscountCoupon.getDiscountUseAmount(),marketingDiscountCouponRecord.getDiscountUseAmount()));
                }
            }
        }
        this.updateById(marketingDiscountCoupon.setStatus("1"));
    }
}