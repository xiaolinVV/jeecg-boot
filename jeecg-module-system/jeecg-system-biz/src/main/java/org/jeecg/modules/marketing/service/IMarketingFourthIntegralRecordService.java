package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingFourthIntegralRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingFourthIntegralRecord;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description: 第四积分记录
 * @Author: jeecg-boot
 * @Date:   2021-06-24
 * @Version: V1.0
 */
public interface IMarketingFourthIntegralRecordService extends IService<MarketingFourthIntegralRecord> {

    /**
     * 查询第四积分记录列表
     *
     * @param page
     * @param marketingFourthIntegralRecordDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, MarketingFourthIntegralRecordDTO marketingFourthIntegralRecordDTO);


    /**
     * 增加会员积分
     *
     * @param memberId
     * @param integral
     * @param tradeNo
     * @param tradeType
     * @return
     */
    public boolean addFourthIntegral(String memberId, BigDecimal integral, String tradeNo, String tradeType);

    /**
     * 减少会员积分
     *
     * @param memberId
     * @param integral
     * @param tradeNo
     * @param tradeType
     * @return
     */
    public boolean subtractFourthIntegral(String memberId, BigDecimal integral, String tradeNo, String tradeType);


    /**
     * 增加会员积分
     *
     * @param memberId
     * @param integral
     * @param tradeNo
     * @param tradeType
     * @return
     */
    public boolean addVirtualFourthIntegral(String memberId, BigDecimal integral, String tradeNo, String tradeType);

    /**
     * 减少会员积分
     *
     * @param memberId
     * @param integral
     * @param tradeNo
     * @param tradeType
     * @return
     */
    public boolean subtractVirtualFourthIntegral(String memberId, BigDecimal integral, String tradeNo, String tradeType);

}
