package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.agency.vo.AgencyWorkbenchVO;
import org.jeecg.modules.alliance.vo.AllianceWorkbenchVO;
import org.jeecg.modules.marketing.vo.MarketingDiscountCouponVO;
import org.jeecg.modules.member.dto.MemberListDTO;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.vo.MemberCertificateVO;
import org.jeecg.modules.member.vo.MemberDiscountVO;
import org.jeecg.modules.member.vo.MemberListVO;
import org.jeecg.modules.store.vo.StoreManageVO;
import org.jeecg.modules.system.vo.SysWorkbenchVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 会员列表
 * @Author: jeecg-boot
 * @Date:   2019-10-24
 * @Version: V1.0
 */
public interface MemberListMapper extends BaseMapper<MemberList> {
     List<MemberList> selectMemberListById(@Param("memberListId") String memberListId);

    IPage<MemberListVO> findMemberList(Page<MemberListVO> page,@Param("memberListVO") MemberListVO memberListVO);

    IPage<MarketingDiscountCouponVO> findMemberDiscount(Page<MarketingDiscountCouponVO> page,@Param("memberDiscountVO") MemberDiscountVO memberDiscountVO);

    IPage<MemberCertificateVO> findMemberCertificate(Page<MemberCertificateVO> page, @Param("memberCertificateVO") MemberCertificateVO memberCertificateVO);


    /**
     * 查询用户团队数量
     *
     * @param memberId
     * @return
     */
    public MemberListVO findMemberDistributionCount(@Param("memberId") String memberId);

    /**
     * 查询用户的级别按照时间排序
     *
     * @param page
     * @param memberId
     * @return
     */
    public IPage<Map<String,Object>> findMemberLevelList(Page<Map<String,Object>> page,@Param("memberId") String memberId);

    Map<String,Long> getStoreSexSum(@Param("sysWorkbenchVO") SysWorkbenchVO sysWorkbenchVO);

    Map<String,Object> getAgencySexSum(@Param("agencyWorkbenchVO") AgencyWorkbenchVO agencyWorkbenchVO);

    Map<String,Long> getSysSexSum(@Param("sysWorkbenchVO") SysWorkbenchVO sysWorkbenchVO);

    IPage<MemberListVO> findAgencyMemberList(Page<MemberListVO> page,@Param("memberListVO") MemberListVO memberListVO);


    /**
     * 获取用户管理会员数据
     * @param page
     * @param sysUserId
     * @param searchNickNamePhone
     * @return
     */
    IPage<Map<String,Object>> getMyMemberList(Page<MemberList> page,@Param("sysUserId")String sysUserId,@Param("searchNickNamePhone")String searchNickNamePhone);

    Map<String,Object> getFranchiseeSexSum(@Param("workbench") AllianceWorkbenchVO workbench);

    IPage<MemberListVO> findAllianceMemberlist(Page<StoreManageVO> page,@Param("memberListDTO") MemberListDTO memberListDTO);

    List<Map<String,Object>> likeMemberByPhone(@Param("phone") String phone);

    /**
     * 查询会员信息(包含删除状态)
     * @param memberListId
     * @return
     */
    MemberList  getMemberListById(@Param("memberListId") String memberListId);

    MemberListVO findMemberDistributionCountByMemberType(@Param("id") String id);

    Long findMemberVipByMarketingGiftBag(@Param("id") String id);

    MemberListVO findMemberVipByMarketingGiftBagCount(@Param("id") String id);

    Long findMemberVipByMarketingGiftBagAndStraightPushId(@Param("straightPushId") String straightPushId,@Param("id") String id);

    IPage<Map<String,Object>> getmemberListByTManageId(Page<Map<String,Object>> page,@Param("id") String id,@Param("memberDesignationGroupId") String memberDesignationGroupId);

    IPage<MemberListVO> memberDesignationPageList(Page<MemberListVO> page,@Param("memberListDTO") MemberListDTO memberListDTO);

    List<MemberListVO> getUnderlingList(@Param("id") String id);

    List<Map<String,Object>> getDesignateMemberListByPhone(@Param("phone") String phone);

    List<MemberList> getMemberDesignationListById(@Param("marketingGiftBagId") String marketingGiftBagId,@Param("memberDesignationId") String memberDesignationId);

    /**
     * 查询直推人数
     *
     * @param memberId
     * @return
     */
    IPage<Map<String,Object>> pushingNumber(Page<Map<String,Object>> page,@Param("memberId") String memberId);

    /**
     * 获取间推人数
     *
     * @param memberId
     * @return
     */
    public int betweenPush(@Param("memberId") String memberId);

}
