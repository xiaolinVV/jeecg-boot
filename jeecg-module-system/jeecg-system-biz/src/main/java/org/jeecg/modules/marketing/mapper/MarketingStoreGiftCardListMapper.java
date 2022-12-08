package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingStoreGiftCardListDTO;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardList;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺礼品卡列表
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
public interface MarketingStoreGiftCardListMapper extends BaseMapper<MarketingStoreGiftCardList> {


    /**
     * 选择商品
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> selectStoreGoods(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);


    /**
     * 获取已选择的商品
     *
     * @param marketingStoreGiftCardListId
     * @return
     */
    public IPage<Map<String,Object>> getSelectGoods(Page<Map<String,Object>> page,@Param("marketingStoreGiftCardListId") String marketingStoreGiftCardListId);


    /**
     * 获取已选择的商品
     *
     * @param marketingStoreGiftCardListId
     * @return
     */
    public List<Map<String,Object>> getSelectGoods(@Param("marketingStoreGiftCardListId") String marketingStoreGiftCardListId);


    /**
     * 礼品卡后端列表
     *
     * @param page
     * @param marketingStoreGiftCardListDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param("marketingStoreGiftCardListDTO") MarketingStoreGiftCardListDTO marketingStoreGiftCardListDTO);

    /**
     * 根据礼包获取礼品卡列表
     *
     * @param marketingGiftBagId
     * @return
     */
    public List<Map<String,Object>> getGiftCarList(@Param("marketingGiftBagId") String marketingGiftBagId);
 }
