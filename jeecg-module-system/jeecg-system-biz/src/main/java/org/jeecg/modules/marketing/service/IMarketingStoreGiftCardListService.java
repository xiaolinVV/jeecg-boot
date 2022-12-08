package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IMarketingStoreGiftCardListService extends IService<MarketingStoreGiftCardList> {

    /**
     * 选择商品
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> selectStoreGoods(Page<Map<String,Object>> page, Map<String,Object> paramMap);


    /**
     * 获取已选择的商品
     *
     * @param marketingStoreGiftCardListId
     * @return
     */
    public IPage<Map<String,Object>> getSelectGoods(Page<Map<String,Object>> page,String marketingStoreGiftCardListId);

    /**
     * 获取已选择的商品
     *
     * @param marketingStoreGiftCardListId
     * @return
     */
    public List<Map<String,Object>> getSelectGoods(String marketingStoreGiftCardListId);


    /**
     * 礼品卡后端列表
     *
     * @param page
     * @param marketingStoreGiftCardListDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,MarketingStoreGiftCardListDTO marketingStoreGiftCardListDTO);


    /**
     * 根据礼包获取礼品卡列表
     *
     * @param marketingGiftBagId
     * @return
     */
    public List<Map<String,Object>> getGiftCarList(String marketingGiftBagId);


    /**
     * 成功会员礼品卡
     *
     * @param marketingStoreGiftCardListId
     * @param memberId
     */
    public void generate(String marketingStoreGiftCardListId,String memberId,String waysObtain);

}
