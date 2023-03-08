package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingDiscountCouponDTO;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.marketing.vo.MarketingDiscountCouponVO;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.entity.OrderStoreList;

import java.util.List;
import java.util.Map;

/**
 * @Description: 优惠券记录
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
public interface IMarketingDiscountCouponService extends IService<MarketingDiscountCoupon> {
    /**
     * 查询核销券
     * @param qqzixuangu 券码
     * @param sysUserId 店铺Id
     * @return
     */
  public IPage<MarketingDiscountCouponDTO> couponVerification(Page<MarketingDiscountCouponVO> page, String qqzixuangu, String sysUserId);

    IPage<MarketingDiscountCouponVO> findDiscountCoupon(Page<MarketingDiscountCouponVO> page, MarketingDiscountCouponVO marketingDiscountCouponVO);


    /**
     *根据会员id查询会员优惠券记录
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> findMarketingDiscountCouponByMemberId(Page<Map<String,Object>> page,Map<String,Object> paramMap);

    /**
     * 根据id获取优惠券详情
     *
     * @param id
     * @return
     */
    public Map<String,Object> findMarketingDiscountCouponInfo(String id);

  /**
   *根据会员id和商品id查询会员优惠券记录
   * @param paramMap
   * @return
   */
  public List<Map<String,Object>> findMarketingDiscountCouponByGoodIds( Map<String,Object> paramMap);

    IPage<MarketingDiscountCouponVO> findDiscountCouponTarrace(Page<MarketingDiscountCouponVO> page, MarketingDiscountCouponVO marketingDiscountCouponVO);

    void updateMarketingDiscountCouponJob();


  /**
   * 退回平台优惠券
   *
   * @param orderList
   */
  public void sendBackOrderMarketingDiscountCoupon(OrderList orderList);

  /**
   * 退回平台优惠券
   *
   * @param orderStoreList
   */
  public void sendBackOrderStoreMarketingDiscountCoupon(OrderStoreList orderStoreList);


}
