package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.good.vo.SearchTermsVO;
import org.jeecg.modules.marketing.dto.MarketingPrefectureGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGood;
import org.jeecg.modules.marketing.entity.MarketingPrefectureType;
import org.jeecg.modules.marketing.vo.MarketingPrefectureGoodVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 专区商品
 * @Author: jeecg-boot
 * @Date:   2020-03-26
 * @Version: V1.0
 */
public interface IMarketingPrefectureGoodService extends IService<MarketingPrefectureGood> {
    /**
     * 商品列表
     * @param page
     * @param marketingPrefectureGoodVO
     * @return
     */
    IPage<MarketingPrefectureGoodDTO> getMarketingPrefectureGood(Page page, MarketingPrefectureGoodVO marketingPrefectureGoodVO);




    /**
     * 根据专区类型查询专区商品
     *
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>>  findByMarketingPrefectureType(Page<Map<String,Object>> page,Map<String,Object> paramObjectMap);
    /**
     * 查询专区商品返回商品id 商品名称
     * @param marketingPrefectureId
     * @return
     */

    List<Map<String,Object>> getMarketingPrefectureGoodName(String marketingPrefectureId);

    /**
     * 查询专商品列表，带模糊查询
     *
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>>  findByMarketingPrefectureIdAndSearch(Page<Map<String,Object>> page,Map<String,Object> paramMap);
    /**
     * 删除专区 关联删除 商品 分类 广告
     * @param ids 专区商品id
     * @param marketingPrefectureId 专区id
     * @param goodListId 商品id
     */
    public void linkToDelete(String ids,String marketingPrefectureId,String goodListId);


    /**
     * 修改专区商品上架判断是否可上架
     */

    public Map<String,Object> linkToUpdate(String marketingPrefectureGoodId);

    /**
     * 修改专区商品
     * @param marketingPrefectureGoodVO
     * @param marketingPrefectureGood
     */
    public void updateMarketingPrefectureGoodAndSpecification(MarketingPrefectureGoodVO marketingPrefectureGoodVO,MarketingPrefectureGood marketingPrefectureGood);


    /**
     * 商家端查询对应专区商品列表
     *
     * @param page
     * @param searchTermsVO
     * @return
     */
    IPage<Map<String,Object>>  findByMarketingPrefectureIdAndSearchMerchant(Page<Map<String,Object>> page, SearchTermsVO searchTermsVO);


    IPage<Map<String,Object>> findByMarketingPrefectureId(Page<Map<String,Object>> page, Map<String, Object> paramObjectMap);

    IPage<Map<String,Object>> findMarketingPrefectureGoodList(Page<Map<String,Object>> page,MarketingPrefectureGoodDTO marketingPrefectureGoodDTO);

    IPage<Map<String,Object>> findByMarketingPrefectureTypeOne(Page<Map<String,Object>> page, Map<String, Object> paramObjectMap);
}
