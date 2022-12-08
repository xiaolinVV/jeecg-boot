package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.entity.MemberGiveWelfarePayments;
import java.math.BigDecimal;
import java.util.Map;
/**
 * @Description: 会员福利金可获赠数量
 * @Author: jeecg-boot
 * @Date:   2021-08-17
 * @Version: V1.0
 */
public interface IMemberGiveWelfarePaymentsService extends IService<MemberGiveWelfarePayments> {
    /**
     * 获赠金额增加
     *
     * @param memberId
     * @param welfarePayments
     * @param tradeType
     * @param tradeNo
     * @return
     */
    public boolean add(String memberId, BigDecimal welfarePayments,String tradeType,String tradeNo);


    /**
     * 获赠金额减少
     *
     * @param memberId
     * @param welfarePayments
     * @param tradeType
     * @param tradeNo
     * @return
     */
    public boolean subtract(String memberId, BigDecimal welfarePayments,String tradeType,String tradeNo);


    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, Map<String, Object> requestMap);
}
