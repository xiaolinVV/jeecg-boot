package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardMemberList;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺会员礼品卡
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
public interface MarketingStoreGiftCardMemberListMapper extends BaseMapper<MarketingStoreGiftCardMemberList> {

    /**
     * 根据会员iD获取礼品卡
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getByMemberId(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);


    /**
     * 获取礼品卡店铺第一分类
     *
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> getGoodTypeOne(@Param("paramMap") Map<String,Object> paramMap);


    /**
     * 获取礼品卡店铺第一分类
     *
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> getGoodTypeTwo(@Param("paramMap") Map<String,Object> paramMap);


    /**
     * 获取礼品卡商品列表
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getGoodList(Page<Map<String, Object>> page,@Param("paramMap") Map<String, Object> paramMap);

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param(Constants.WRAPPER) QueryWrapper<MarketingStoreGiftCardMemberList> queryWrapper,@Param("paramMap") Map<String, Object> paramMap);

    IPage<Map<String,Object>> giveList(Page<Map<String,Object>> page,@Param(Constants.WRAPPER) QueryWrapper<MarketingStoreGiftCardMemberList> queryWrapper,@Param("paramMap") Map<String, Object> paramMap);
}
