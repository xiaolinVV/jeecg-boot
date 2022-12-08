package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingIntegralRecord;
import org.jeecg.modules.marketing.vo.MarketingIntegralRecordVO;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description: 积分记录
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
public interface IMarketingIntegralRecordService extends IService<MarketingIntegralRecord> {

    /**
     * 增加积分记录
     *
     * @param tradeType
     * @param integral
     * @param memberListId
     * @return
     */
    public boolean addMarketingIntegralRecord(String tradeType, BigDecimal integral,String memberListId,String tradeNo);

    /**
     * 减少积分
     * 
     * @param tradeType
     * @param integral
     * @param memberListId
     * @param tradeNo
     * @return
     */
    public boolean subtractMarketingIntegralRecord(String tradeType, BigDecimal integral,String memberListId,String tradeNo);


    IPage<MarketingIntegralRecordVO> queryPageList(Page<MarketingIntegralRecordVO> page, QueryWrapper<MarketingIntegralRecordVO> queryWrapper,Map<String, Object> requestMap);

    IPage<Map<String,Object>> getMarketingIntegralRecordPageMap(Page<Map<String,Object>> page, String memberId);
}
