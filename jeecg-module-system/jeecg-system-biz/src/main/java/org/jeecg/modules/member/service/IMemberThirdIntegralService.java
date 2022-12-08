package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.entity.MemberThirdIntegral;

import java.math.BigDecimal;

/**
 * @Description: 会员第三积分
 * @Author: jeecg-boot
 * @Date:   2021-06-30
 * @Version: V1.0
 */
public interface IMemberThirdIntegralService extends IService<MemberThirdIntegral> {


    /**
     * 查询会员第三积分
     *
     * @param memberId
     * @return
     */
    public BigDecimal totalIntegral(String memberId);

    /**
     * 第三积分增加
     *
     * @param marketingGroupIntegralManageId
     * @param memberId
     * @param integral
     * @param tradeNo
     * @param tradeType
     * @return
     */
    public boolean addThirdIntegral(String marketingGroupIntegralManageId, String memberId, BigDecimal integral,String tradeNo,String tradeType);


    /**
     * 第三积分减少
     *
     * @param marketingGroupIntegralManageId
     * @param memberId
     * @param integral
     * @param tradeNo
     * @param tradeType
     * @return
     */
    public boolean subtractThirdIntegral(String marketingGroupIntegralManageId, String memberId, BigDecimal integral,String tradeNo,String tradeType);

}
