package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.dto.MemberShoppingCartDto;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.member.vo.MemberShoppingCartVo;

/**
 * @Description: 购物车商品
 * @Author: jeecg-boot
 * @Date:   2019-10-30
 * @Version: V1.0
 */
public interface IMemberShoppingCartService extends IService<MemberShoppingCart> {
    IPage<MemberShoppingCartDto> getMemberShoppingCartVo(Page<MemberShoppingCartDto> page, MemberShoppingCartVo memberShoppingCartVo );


    /**
     * 新增购物车
     * @param isPlatform
     * @param goodId
     * @param specification
     * @param memberId
     * @param quantity
     * @param isView
     * @return
     */
    public String addGoodToShoppingCart(Integer isPlatform, String goodId, String specification, String memberId, Integer quantity, String isView,
                                        String marketingPrefectureId, String marketingFreeGoodListId, String marketingGroupRecordId, String marketingStoreGiftCardMemberListId, String marketingRushGroupId,String marketingLeagueGoodListId,String marketingStorePrefectureGoodId
                                        );


    /**
     * 新增购物车针对兑换券使用
     *
     * @param isPlatform
     * @param goodSpecificationId
     * @param memberId
     * @param quantity
     * @param isView
     * @param marketingCertificateRecordId
     * @return
     */
    public String addGoodToShoppingCartCertificate(Integer isPlatform,String goodSpecificationId,String memberId,Integer quantity,String isView,String marketingCertificateRecordId);
}
