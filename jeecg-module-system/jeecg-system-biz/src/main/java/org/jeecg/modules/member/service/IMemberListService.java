package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 会员列表
 * @Author: jeecg-boot
 * @Date:   2019-10-24
 * @Version: V1.0
 */
public interface IMemberListService extends IService<MemberList> {

    public List<MemberList> selectMemberListById(String memberListId);

    IPage<MemberListVO> findMemberList(Page<MemberListVO> page, MemberListVO memberListVO);

    IPage<MarketingDiscountCouponVO> findMemberDiscount(Page<MarketingDiscountCouponVO> page,MemberDiscountVO memberDiscountVO);

    IPage<MemberCertificateVO> findMemberCertificate(Page<MemberCertificateVO> page, MemberCertificateVO memberCertificateVO);


    /**
     * 查询用户团队数量
     *
     * @param memberId
     * @return
     */
    public MemberListVO findMemberDistributionCount(String memberId);

    /**
     * 查询用户的级别按照时间排序
     *
     * @param page
     * @param memberId
     * @return
     */
    public IPage<Map<String,Object>> findMemberLevelList(Page<Map<String,Object>> page, String memberId);

    MemberListDTO getStoreSexSum(SysWorkbenchVO sysWorkbenchVO);

    AgencyWorkbenchVO getAgencySexSum(AgencyWorkbenchVO agencyWorkbenchVO);

    Map<String,Long> getSysSexSum(SysWorkbenchVO sysWorkbenchVO);

    IPage<MemberListVO> findAgencyMemberList(Page<MemberListVO> page, MemberListVO memberListVO);

    Map<String,Object> returnPromoter(String id);

    Map<String,Object> returnMemberNameById(String promoter, String promoterType);
    /**
     * 获取用户管理会员数据
     * @param page
     * @param sysUserId
     * @param searchNickNamePhone
     * @return
     */
    IPage<Map<String,Object>> getMyMemberList(Page<MemberList> page, String sysUserId, String searchNickNamePhone);

    AllianceWorkbenchVO getFranchiseeSexSum(AllianceWorkbenchVO workbench);

    IPage<MemberListVO> findAllianceMemberlist(Page<StoreManageVO> page, MemberListDTO memberListDTO);

    List<Map<String,Object>> likeMemberByPhone(String phone);
    /**
     * 查询会员信息(包含删除状态)
     * @param memberListId
     * @return
     */
    MemberList  getMemberListById(String memberListId);

    MemberListVO findMemberDistributionCountByMemberType(String id);

    Long findMemberVipByMarketingGiftBag(String id);

    MemberListVO findMemberVipByMarketingGiftBagCount(String id);

    Long findMemberVipByMarketingGiftBagAndStraightPushId(String straightPushId, String id);


    IPage<Map<String,Object>> getmemberListByTManageId(Page<Map<String,Object>> page, String id,String memberDesignationGroupId);

    //boolean designateMember(MemberList levelDownMember, String designateMemberId, String memberId);

    //void totalMembersSub(MemberList memberList,MemberList levelDownMember);

    //void totalMembersAdd(MemberList memberList);

    //void memberListSetTManageId(MemberList memberList, String tMemberId);

    IPage<MemberListVO> memberDesignationPageList(Page<MemberListVO> page, MemberListDTO memberListDTO);

    List<MemberListVO> getUnderlingList(String id);

    void setPromoter(MemberList memberList,String tMemberId);

    List<Map<String,Object>> getDesignateMemberListByPhone(String phone);

    List<MemberList> getMemberDesignationListById(String marketingGiftBagId, String memberDesignationId);

    /**
     * 这是注册登录的时候分享关系和称号团队的设置
     * @param memberList
     */
    public void setLoginRegister(MemberList memberList,String sysUserId,String tMemberId);

    /**
     * 会员添加分享二维码
     * @param memberList
     */
    public void addShareQr(MemberList memberList,String sysUserId);

    /**
     * 增加会员金额
     *
     * @param memberId
     * @param balance
     * @param orderNo
     * @param payType
     * @return
     */
    public boolean addBlance(String memberId, BigDecimal balance, String orderNo, String payType);

    /**
     * 增加会员金额
     *
     * @param memberId
     * @param balance
     * @param orderNo
     * @param payType
     * @return
     */
    public boolean subtractBlance(String memberId, BigDecimal balance, String orderNo, String payType);


    /**
     * 余额充值成功
     * @param payBalanceLogId
     */
    public void rechargeBalance(String payBalanceLogId);

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
    public int betweenPush(String memberId);
}
