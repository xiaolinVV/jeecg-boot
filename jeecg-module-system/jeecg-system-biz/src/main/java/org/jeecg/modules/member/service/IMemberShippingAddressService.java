package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.dto.MemberShippingAddressDto;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.vo.MemberShippingAddressVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 收货地址
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
public interface IMemberShippingAddressService extends IService<MemberShippingAddress> {
    IPage<MemberShippingAddressDto> getMemberShippingAddressVo(Page<MemberShippingAddressDto> page, MemberShippingAddressVo memberShippingAddressVo );


    /**
     * 根据用户id查询收货地址
     * @param page
     * @param memberId
     * @return
     */
   public IPage<Map<String,Object>> getMemberShippingAddressByMemberId(Page<Map<String,Object>> page,String memberId);

    List<Map<String,Object>> findMemberShippingAddressByMemberId(String memberListId);
}
