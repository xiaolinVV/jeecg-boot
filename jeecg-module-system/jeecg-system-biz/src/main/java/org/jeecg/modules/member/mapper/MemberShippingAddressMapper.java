package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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
public interface MemberShippingAddressMapper extends BaseMapper<MemberShippingAddress> {
    IPage<MemberShippingAddressDto> getMemberShippingAddressVo(Page<MemberShippingAddressDto> page, @Param("memberShippingAddressVo")MemberShippingAddressVo memberShippingAddressVo);

    /**
     * 根据用户id查询收货地址
     * @param page
     * @param memberId
     * @return
     */
    public IPage<Map<String,Object>> getMemberShippingAddressByMemberId(Page<Map<String,Object>> page,@Param("memberId") String memberId);

    List<Map<String,Object>> findMemberShippingAddressByMemberId(@Param("memberListId") String memberListId);

}
