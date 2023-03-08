package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.dto.MemberBankCardDTO;
import org.jeecg.modules.member.entity.MemberBankCard;
import org.jeecg.modules.member.vo.MemberBankCardVO;

/**
 * @Description: 会员银行卡
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface IMemberBankCardService extends IService<MemberBankCard> {

    IPage<MemberBankCardVO> findMemberBankCard(Page<MemberBankCardVO> page, MemberBankCardDTO memberBankCardDTO);

    /**
     * 创建汇付天下分账信息
     *
     * @param memberBankCard
     * @return
     */
    public Result<?> createMemberPrivate(MemberBankCard memberBankCard);

    /**
     * 修改汇付天下分账信息
     *
     * @param memberBankCard
     * @return
     */
    public Result<?> updateSettleAccountPrivate(MemberBankCard memberBankCard);

}
