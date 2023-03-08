package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardMemberList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺会员礼品卡
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
public interface IMarketingStoreGiftCardMemberListService extends IService<MarketingStoreGiftCardMemberList> {


    /**
     * 根据会员iD获取礼品卡
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getByMemberId(Page<Map<String,Object>> page,Map<String,Object> paramMap);


    /**
     * 根据会员iD获取礼品卡(全部)
     *
     * @param
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> getByMemberId(Map<String,Object> paramMap);


    /**
     * 获取礼品卡店铺第一分类
     *
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> getGoodTypeOne(Map<String,Object> paramMap);


    /**
     * 获取礼品卡店铺第一分类
     *
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> getGoodTypeTwo(Map<String,Object> paramMap);


    /**
     * 获取礼品卡商品列表
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getGoodList(Page<Map<String, Object>> page,Map<String, Object> paramMap);

    /**
     * 会员礼品卡
     * @param page
     * @param queryWrapper
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, QueryWrapper<MarketingStoreGiftCardMemberList> queryWrapper, Map<String, Object> paramMap);

    IPage<Map<String,Object>> giveList(Page<Map<String,Object>> page, QueryWrapper<MarketingStoreGiftCardMemberList> queryWrapper, Map<String, Object> paramMap);


    /**
     * 礼品卡扣除
     *
     * @param marketingStoreGiftCardMemberListId
     * @param balance
     * @param tradeNo
     */
    public void subtractBlance(String marketingStoreGiftCardMemberListId, BigDecimal balance, String tradeNo,String tredeType);

    /**
     * 礼品卡增加
     *
     * @param marketingStoreGiftCardMemberListId
     * @param balance
     * @param tradeNo
     */
    public void addBlance(String marketingStoreGiftCardMemberListId, BigDecimal balance,String tradeNo,String tredeType);
}
