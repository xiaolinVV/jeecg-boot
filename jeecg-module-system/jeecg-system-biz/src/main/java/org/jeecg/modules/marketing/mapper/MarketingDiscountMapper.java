package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingDiscountDTO;
import org.jeecg.modules.marketing.entity.MarketingDiscount;
import org.jeecg.modules.marketing.vo.MarketingDiscountVO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 优惠券
 * @Author: jeecg-boot
 * @Date:   2019-11-11
 * @Version: V1.0
 */
@Repository
@Mapper
public interface MarketingDiscountMapper extends BaseMapper<MarketingDiscount> {

  void  updateMarketingDiscountById(@Param("id")String id,@Param("delExplain")String delExplain);

  /**
   *
   * @param paramMap
   * @return
   */
  public IPage<Map<String,Object>> findMarketingDiscountByGoodId(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);


  /**
   *根据店铺id查询优惠券信息
   * @param sysUserId
   * @return
   */
  public IPage<Map<String,Object>> findMarketingDiscountBySysUserId(Page<Map<String,Object>> page, @Param("sysUserId")   String sysUserId);

  /**
   *根据门槛查询优惠券信息
   * @param isThreshold
   * @return
   */
  public IPage<Map<String,Object>> findMarketingDiscountByIsThreshold(Page<Map<String,Object>> page, @Param("isThreshold")   String isThreshold, @Param("name") String name);

  /**
   *根据店铺id查询优惠券信息
   * @param id
   * @return
   */
  public Map<String,Object> findMarketingDiscountById(@Param("id") String id);


  /**
   * 平台优惠券列表查询
   * @param page
   * @param marketingDiscountVO
   * @return
   */
  IPage<MarketingDiscountDTO> getMarketingDiscountList(Page<MarketingDiscountDTO> page,@Param("marketingDiscountVO") MarketingDiscountVO marketingDiscountVO);

  MarketingDiscountDTO findMarketingDiscountDTO(@Param("id") String id);

  List<MarketingDiscountVO> findMarketingDiscountVO(@Param("marketingDiscountVO") MarketingDiscountVO marketingDiscountVO);

  IPage<MarketingDiscountDTO> findMarketingDiscountStoreList(Page<MarketingDiscountDTO> page,@Param("marketingDiscountVO") MarketingDiscountVO marketingDiscountVO);

  List<MarketingDiscountVO> findGiveMarketingDiscountVO(@Param("marketingDiscountVO") MarketingDiscountVO marketingDiscountVO);

  IPage<Map<String,Object>> findMarketingDiscountPage(Page<Map<String,Object>> page);
}
