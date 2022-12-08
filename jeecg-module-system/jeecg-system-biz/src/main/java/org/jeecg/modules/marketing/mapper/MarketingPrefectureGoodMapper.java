package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.good.vo.SearchTermsVO;
import org.jeecg.modules.marketing.dto.MarketingPrefectureGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGood;
import org.jeecg.modules.marketing.vo.MarketingPrefectureGoodVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 专区商品
 * @Author: jeecg-boot
 * @Date:   2020-03-26
 * @Version: V1.0
 */
public interface MarketingPrefectureGoodMapper extends BaseMapper<MarketingPrefectureGood> {
    /**
     * 商品列表
     * @param page
     * @param marketingPrefectureGoodVO
     * @return
     */
   IPage<MarketingPrefectureGoodDTO> getMarketingPrefectureGood(Page page, @Param("marketingPrefectureGoodVO") MarketingPrefectureGoodVO marketingPrefectureGoodVO);

    /**
     * 根据专区类型查询专区商品
     *
     * @param page
     * @param
     * @return
     */
    IPage<Map<String,Object>>  findByMarketingPrefectureType(Page<Map<String,Object>> page,@Param("paramObjectMap") Map<String,Object> paramObjectMap);
    /**
     * 查询专区商品返回商品id 商品名称
     * @param marketingPrefectureId
     * @return
     */
    List<Map<String, Object>> getMarketingPrefectureGoodName(@Param("marketingPrefectureId") String marketingPrefectureId);


    /**
     * 查询专商品列表，带模糊查询
     *
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>>  findByMarketingPrefectureIdAndSearch(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);

    /**
     * 商家端查询对应专区商品列表
     *
     * @param page
     * @param searchTermsVO
     * @return
     */
    IPage<Map<String,Object>>  findByMarketingPrefectureIdAndSearchMerchant(Page<Map<String,Object>> page, @Param("searchTermsVO") SearchTermsVO searchTermsVO);


    IPage<Map<String,Object>> findByMarketingPrefectureId(Page<Map<String,Object>> page,@Param("paramMap") Map<String, Object> paramMap);

    IPage<Map<String,Object>> findMarketingPrefectureGoodList(Page<Map<String,Object>> page,@Param("marketingPrefectureGoodDTO") MarketingPrefectureGoodDTO marketingPrefectureGoodDTO);

    IPage<Map<String,Object>> findByMarketingPrefectureTypeOne(Page<Map<String,Object>> page,@Param("paramObjectMap") Map<String, Object> paramObjectMap);

}
