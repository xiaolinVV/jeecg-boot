package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.dto.MemberBankCardDTO;
import org.jeecg.modules.member.entity.MemberBankCard;
import org.jeecg.modules.member.mapper.MemberBankCardMapper;
import org.jeecg.modules.member.service.IMemberBankCardService;
import org.jeecg.modules.member.vo.MemberBankCardVO;
import org.jeecg.modules.pay.utils.HftxPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 会员银行卡
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Service
public class MemberBankCardServiceImpl extends ServiceImpl<MemberBankCardMapper, MemberBankCard> implements IMemberBankCardService {

    @Autowired
    private HftxPayUtils hftxPayUtils;


    @Override
    public IPage<MemberBankCardVO> findMemberBankCard(Page<MemberBankCardVO> page, MemberBankCardDTO memberBankCardDTO) {
        return baseMapper.findMemberBankCard(page, memberBankCardDTO);
    }

    @Override
    public Result<?> createMemberPrivate(MemberBankCard memberBankCard) {
        //新增会员信息和结算账户对象
        if (hftxPayUtils.createMemberPrivate(memberBankCard.getId(), memberBankCard.getCardholder())) {
            //新增结算账户信息
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("card_id", memberBankCard.getBankCard());
            paramMap.put("card_name", memberBankCard.getCardholder());
            paramMap.put("cert_id", memberBankCard.getIdentityNumber());
            paramMap.put("tel_no", memberBankCard.getPhone());
            paramMap.put("bank_acct_type", "2");
            Map<String, Object> resultMap = hftxPayUtils.createSettleAccountPrivate(memberBankCard.getId(), paramMap);
            if (resultMap.get("status").toString().equals("succeeded")) {
                if (StringUtils.isNotBlank(resultMap.get("id").toString())) {
                    memberBankCard.setSettleAccount(resultMap.get("id").toString());
                    this.saveOrUpdate(memberBankCard);
                    return Result.ok("设置成功");
                } else {
                    return Result.error(String.valueOf(resultMap.get("error_msg")));
                }
            } else {
                return Result.error(String.valueOf(resultMap.get("error_msg")));
            }
        } else {
            return Result.error("分账设置失败");
        }
    }

    @Override
    public Result<?> updateSettleAccountPrivate(MemberBankCard memberBankCard) {
        //修改结算账户对象
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("card_id", memberBankCard.getBankCard());
        paramMap.put("card_name", memberBankCard.getCardholder());
        paramMap.put("cert_id", memberBankCard.getIdentityNumber());
        paramMap.put("tel_no", memberBankCard.getPhone());
        paramMap.put("bank_acct_type", "2");
        Map<String, Object> resultMap = hftxPayUtils.updateSettleAccountPrivate(memberBankCard.getId(), paramMap, memberBankCard.getSettleAccount());
        if (resultMap.get("status").toString().equals("succeeded")) {
            memberBankCard.setSettleAccount(resultMap.get("id").toString());
            this.saveOrUpdate(memberBankCard);
            return Result.ok("设置成功");
        } else {
            memberBankCard.setSettleAccount("");
            this.saveOrUpdate(memberBankCard);
            return Result.error(String.valueOf(resultMap.get("error_msg")));
        }
    }
}
