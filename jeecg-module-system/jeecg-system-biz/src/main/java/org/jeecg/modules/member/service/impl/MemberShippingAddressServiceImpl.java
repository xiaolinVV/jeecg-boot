package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.member.dto.MemberShippingAddressDto;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.mapper.MemberShippingAddressMapper;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.member.vo.MemberShippingAddressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 收货地址
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
@Service
public class MemberShippingAddressServiceImpl extends ServiceImpl<MemberShippingAddressMapper, MemberShippingAddress> implements IMemberShippingAddressService {

    @Autowired(required = false)
    private MemberShippingAddressMapper memberShippingAddressMapper;

    @Override
    public IPage<MemberShippingAddressDto> getMemberShippingAddressVo(Page<MemberShippingAddressDto> page, MemberShippingAddressVo memberShippingAddressVo) {
        return baseMapper.getMemberShippingAddressVo(page, memberShippingAddressVo);
    }

    @Override
    public IPage<Map<String, Object>> getMemberShippingAddressByMemberId(Page<Map<String, Object>> page, String memberId) {
        return memberShippingAddressMapper.getMemberShippingAddressByMemberId(page,memberId);
    }

    @Override
    public List<Map<String, Object>> findMemberShippingAddressByMemberId(String memberListId) {
        return memberShippingAddressMapper.findMemberShippingAddressByMemberId(memberListId);
    }

}
