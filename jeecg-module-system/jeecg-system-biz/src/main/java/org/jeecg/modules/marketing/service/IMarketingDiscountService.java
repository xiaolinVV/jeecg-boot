package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.dto.MarketingDiscountDTO;
import org.jeecg.modules.marketing.entity.MarketingDiscount;
import org.jeecg.modules.marketing.vo.MarketingDiscountVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 优惠券
 * @Author: jeecg-boot
 * @Date:   2019-11-11
 * @Version: V1.0
 */
public interface IMarketingDiscountService extends IService<MarketingDiscount> {

    public String renoveById(String id,String delExplain);

    /**
     *根据门槛查询优惠券信息
     * @param isThreshold
     * @return
     */
    public IPage<Map<String,Object>> findMarketingDiscountByIsThreshold(Page<Map<String,Object>> page,String isThreshold,String name);



    /**
     *根据店铺id查询优惠券信息
     * @param id
     * @return
     */
    public Map<String,Object> findMarketingDiscountById(String id);

    /**
     *根据商品id查询优惠券规则
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> findMarketingDiscountByGoodId(Page<Map<String,Object>> page,Map<String,Object> paramMap);


    /**
     *根据店铺id查询优惠券信息
     * @param   sysUserId
     * @return
     */
    public IPage<Map<String,Object>> findMarketingDiscountBySysUserId(Page<Map<String,Object>> page,  String sysUserId);

    /**
     *新增优惠券
     */
    void savaMarketingDiscount(MarketingDiscount MarketingDiscount, String goodStoreListIds, String channelIds, String isPlatform);



    /**
     * 平台优惠券列表查询
     * @param page
     * @param marketingDiscountVO
     * @return
     */
    IPage<MarketingDiscountDTO> getMarketingDiscountList(Page<MarketingDiscountDTO> page, MarketingDiscountVO marketingDiscountVO);


    MarketingDiscountDTO findMarketingDiscountDTO(String id);

    List<MarketingDiscountVO> findMarketingDiscountVO(MarketingDiscountVO marketingDiscountVO);


    IPage<MarketingDiscountDTO> findMarketingDiscountStoreList(Page<MarketingDiscountDTO>page, MarketingDiscountVO marketingDiscountVO);

    Result<MarketingDiscountDTO> edit(MarketingDiscountVO marketingDiscountVO);

    List<MarketingDiscountVO> findGiveMarketingDiscountVO(MarketingDiscountVO marketingDiscountVO);

    IPage<Map<String,Object>> findMarketingDiscountPage(Page<Map<String, Object>> page);
}
