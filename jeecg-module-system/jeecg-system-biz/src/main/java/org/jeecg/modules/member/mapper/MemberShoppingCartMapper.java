package org.jeecg.modules.member.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.dto.MemberShoppingCartDto;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.member.vo.MemberShoppingCartVo;

/**
 * @Description: 购物车商品
 * @Author: jeecg-boot
 * @Date:   2019-10-30
 * @Version: V1.0
 */
public interface MemberShoppingCartMapper extends BaseMapper<MemberShoppingCart> {
    IPage<MemberShoppingCartDto> getMemberShoppingCartVo(Page<MemberShoppingCartDto> page, @Param("memberShoppingCartVo")MemberShoppingCartVo memberShoppingCartVo);

}
