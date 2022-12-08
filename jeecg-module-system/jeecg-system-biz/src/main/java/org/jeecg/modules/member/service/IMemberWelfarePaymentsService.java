package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberWelfarePayments;
import org.jeecg.modules.member.vo.MemberWelfarePaymentsVO;
import org.jeecg.modules.store.entity.StoreManage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 会员福利金
 * @Author: jeecg-boot
 * @Date:   2019-11-16
 * @Version: V1.0
 */
public interface IMemberWelfarePaymentsService extends IService<MemberWelfarePayments> {


    /**
     * 分页查询用户福利金列表
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> findMemberWelfarePaymentsByMemberId(Page<Map<String,Object>> page, Map<String,Object> paramMap);

    IPage<MemberWelfarePaymentsVO> findMemberWelfarePayments(Page<MemberWelfarePaymentsVO> page, MemberWelfarePaymentsVO memberWelfarePaymentsVO);

    IPage<MemberWelfarePaymentsVO> findMemberPayRecord(Page<MemberWelfarePaymentsVO> page, MemberWelfarePaymentsVO memberWelfarePaymentsVO);

    IPage<Map<String,Object>> findMemberWelfarePaymentsPageByMemberId(Page<Map<String,Object>> page, Map<String, Object> paramObjectMap);

    void PresentedMemberWelfarePayments(MemberList memberList, String amont, String giveExplain, MemberList benefactor);

    IPage<Map<String,Object>> findfrozenAandUnusableById(Page<Map<String,Object>> page, Map<String, Object> paramObjectMap);

    void postWelfarePayment(MemberList memberList, BigDecimal bigDecimal, StoreManage storeManage, String giveExplain, BigDecimal proportion);

    void storeGiveWelfarePayment(MemberList memberList, BigDecimal bigDecimal, StoreManage storeManage, BigDecimal proportion);

    List<MemberWelfarePayments> getAbnormalWelfarePam(HashMap<String, Object> map);


    /**
     * 转入积分
     *
     * @param membeId
     * @param welfarePayments
     * @param tradeType
     * @return
     */
    public boolean addWelfarePayments(String membeId, BigDecimal  welfarePayments, String tradeType,String tradeNo,String remark);


    /**
     * 转出积分
     * @param membeId
     * @param welfarePayments
     * @param tradeType
     * @return
     */
    public boolean subtractWelfarePayments(String membeId, BigDecimal  welfarePayments, String tradeType,String tradeNo,String remark);


    /**
     * 转入积分到待结算
     *
     * @param membeId
     * @param welfarePayments
     * @param tradeType
     * @return
     */
    public boolean addAccountWelfarePayments(String membeId, BigDecimal  welfarePayments, String tradeType,String tradeNo);


    /**
     * 转出积分到待结算
     * @param membeId
     * @param welfarePayments
     * @param tradeType
     * @return
     */
    public boolean subtractAccountWelfarePayments(String membeId, BigDecimal  welfarePayments, String tradeType,String tradeNo);


    /**
     * 转入积分到不可用待结算
     *
     * @param membeId
     * @param welfarePayments
     * @param tradeType
     * @return
     */
    public boolean addUnusableWelfarePayments(String membeId, BigDecimal  welfarePayments, String tradeType,String tradeNo);


    /**
     * 转出积分到不可用待结算
     * @param membeId
     * @param welfarePayments
     * @param tradeType
     * @return
     */
    public boolean subtractUnusableWelfarePayments(String membeId, BigDecimal  welfarePayments, String tradeType,String tradeNo);


    /**
     * 冻结转解冻
     *
     *
     * @param memberWelfarePaymentsId
     */
    public void freezeToNomal(String memberWelfarePaymentsId);
}
