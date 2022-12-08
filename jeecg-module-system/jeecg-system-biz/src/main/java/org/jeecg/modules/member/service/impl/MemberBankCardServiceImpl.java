package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.dto.MemberBankCardDTO;
import org.jeecg.modules.member.entity.MemberBankCard;
import org.jeecg.modules.member.mapper.MemberBankCardMapper;
import org.jeecg.modules.member.service.IMemberBankCardService;
import org.jeecg.modules.member.vo.MemberBankCardVO;
import org.springframework.stereotype.Service;

/**
 * @Description: 会员银行卡
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Service
public class MemberBankCardServiceImpl extends ServiceImpl<MemberBankCardMapper, MemberBankCard> implements IMemberBankCardService {

    @Override
    public IPage<MemberBankCardVO> findMemberBankCard(Page<MemberBankCardVO> page, MemberBankCardDTO memberBankCardDTO) {
        return baseMapper.findMemberBankCard(page, memberBankCardDTO);
    }
}
