package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingDiscountCouponDTO;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.marketing.vo.MarketingDiscountCouponVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 优惠券记录
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
public interface MarketingDiscountCouponMapper extends BaseMapper<MarketingDiscountCoupon> {
    /**
     * 查询核销券
     * @param qqzixuangu 券码
     * @param sysUserId 店铺Id
     * @return
     */
  IPage<MarketingDiscountCouponDTO>  couponVerification(Page<MarketingDiscountCouponVO> page, @Param("qqzixuangu") String qqzixuangu, @Param("sysUserId") String sysUserId);

    IPage<MarketingDiscountCouponVO> findDiscountCoupon(Page<MarketingDiscountCouponVO> page,@Param("marketingDiscountCouponVO") MarketingDiscountCouponVO marketingDiscountCouponVO);


    /**
     *根据会员id查询会员优惠券记录
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> findMarketingDiscountCouponByMemberId(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);



  /**
   *根据会员id和商品id查询会员优惠券记录
   * @param paramMap
   * @return
   */
  public List<Map<String,Object>> findMarketingDiscountCouponByGoodIds(@Param("paramMap") Map<String,Object> paramMap);

    /**
     * 根据id获取优惠券详情
     *
     * @param id
     * @return
     */
    public Map<String,Object> findMarketingDiscountCouponInfo(@Param("id") String id);

    IPage<MarketingDiscountCouponVO> findDiscountCouponTarrace(Page<MarketingDiscountCouponVO> page, @Param("marketingDiscountCouponVO") MarketingDiscountCouponVO marketingDiscountCouponVO);

    void updateMarketingDiscountTakeEffect();

    void updateMarketingDiscountPastDue();


}
