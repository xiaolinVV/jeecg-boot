package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.agency.vo.AgencyWorkbenchVO;
import org.jeecg.modules.alliance.vo.AllianceWorkbenchVO;
import org.jeecg.modules.jwt.utils.WeixinQRUtils;
import org.jeecg.modules.marketing.entity.MarketingDistributionSetting;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecord;
import org.jeecg.modules.marketing.service.IMarketingDistributionSettingService;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordService;
import org.jeecg.modules.marketing.vo.MarketingDiscountCouponVO;
import org.jeecg.modules.member.dto.MemberListDTO;
import org.jeecg.modules.member.entity.*;
import org.jeecg.modules.member.mapper.MemberListMapper;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.member.utils.QrCodeUtils;
import org.jeecg.modules.member.vo.MemberCertificateVO;
import org.jeecg.modules.member.vo.MemberDiscountVO;
import org.jeecg.modules.member.vo.MemberListVO;
import org.jeecg.modules.pay.entity.PayBalanceLog;
import org.jeecg.modules.pay.service.IPayBalanceLogService;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.vo.StoreManageVO;
import org.jeecg.modules.system.entity.SysSmallcode;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysSmallcodeService;
import org.jeecg.modules.system.vo.SysWorkbenchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 会员列表
 * @Author: jeecg-boot
 * @Date:   2019-10-24
 * @Version: V1.0
 */
@Service
@Slf4j
public class MemberListServiceImpl extends ServiceImpl<MemberListMapper, MemberList> implements IMemberListService {

    @Autowired
    @Lazy
    private IStoreManageService iStoreManageService;

    @Autowired
    @Lazy
    private IMemberDesignationService iMemberDesignationService;
    @Autowired
    @Lazy
    private IMemberDesignationCountService iMemberDesignationCountService;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private ISysSmallcodeService iSysSmallcodeService;

    @Autowired
    private WeixinQRUtils weixinQRUtils;

    @Autowired
    @Lazy
    private IMarketingGiftBagRecordService iMarketingGiftBagRecordService;

    @Autowired
    @Lazy
    private IMarketingDistributionSettingService iMarketingDistributionSettingService;

    @Autowired
    @Lazy
    private IMemberDesignationMemberListService iMemberDesignationMemberListService;

    @Autowired
    private IPayBalanceLogService iPayBalanceLogService;

    @Autowired
    private IMemberAccountCapitalService iMemberAccountCapitalService;


    @Autowired
    @Lazy
    private IMemberGiveWelfarePaymentsService iMemberGiveWelfarePaymentsService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    @Autowired
    private QrCodeUtils qrCodeUtils;

    @Override
    public List<MemberList> selectMemberListById(String memberListId) {
        List<MemberList> memberLists = baseMapper.selectMemberListById(memberListId);
        return memberLists;
    }


    @Override
    public IPage<MemberListVO> findMemberList(Page<MemberListVO> page, MemberListVO memberListVO) {
        return baseMapper.findMemberList(page,memberListVO);
    }

    @Override
    public IPage<MarketingDiscountCouponVO> findMemberDiscount(Page<MarketingDiscountCouponVO> page,MemberDiscountVO memberDiscountVO) {
        return baseMapper.findMemberDiscount(page,memberDiscountVO);
    }

    @Override
    public IPage<MemberCertificateVO> findMemberCertificate(Page<MemberCertificateVO> page, MemberCertificateVO memberCertificateVO) {
        return baseMapper.findMemberCertificate(page,memberCertificateVO);
    }


    @Override
    public MemberListVO findMemberDistributionCount(String memberId) {
        return baseMapper.findMemberDistributionCount(memberId);
    }

    @Override
    public IPage<Map<String, Object>> findMemberLevelList(Page<Map<String, Object>> page, String memberId) {
        return baseMapper.findMemberLevelList(page,memberId);
    }

    @Override
    public MemberListDTO getStoreSexSum(SysWorkbenchVO sysWorkbenchVO) {
        ArrayList<Map<String, Object>> memberList = new ArrayList<>();
        ArrayList<Map<String, Object>> memberSexList = new ArrayList<>();
        Map<String, Long> storeSexSum = baseMapper.getStoreSexSum(sysWorkbenchVO);
        storeSexSum.forEach((k,v)-> {
            HashMap<String, Object> mapx = new HashMap<>();
            if (k.equals("asordinarySum")) {
                mapx.put("item", "普通会员");
                mapx.put("count", String.valueOf(v));
                memberList.add(mapx);
            } else if (k.equals("vipSum")) {
                mapx.put("item", "vip");
                mapx.put("count", String.valueOf(v));
                memberList.add(mapx);
            } else if (k.equals("memberMan")) {
                mapx.put("item", "男");
                mapx.put("count", String.valueOf(v));
                memberSexList.add(mapx);
            } else if (k.equals("memberWoMan")) {
                mapx.put("item", "女");
                mapx.put("count",String.valueOf(v));
                memberSexList.add(mapx);
            } else if (k.equals("memberUnknown")) {
                mapx.put("item", "未知");
                mapx.put("count", String.valueOf(v));
                memberSexList.add(mapx);
            }
        });
        MemberListDTO memberListDTO = new MemberListDTO();
        memberListDTO.setMemberList(memberList);
        memberListDTO.setMemberSexList(memberSexList);
        memberListDTO.setMemberPatch(storeSexSum.get("asordinarySum")+storeSexSum.get("vipSum"));
        return memberListDTO;

    }

    @Override
    public AgencyWorkbenchVO getAgencySexSum(AgencyWorkbenchVO agencyWorkbenchVO) {
        ArrayList<Map<String, Object>> memberList = new ArrayList<>();
        ArrayList<Map<String, Object>> memberSexList = new ArrayList<>();
        Map<String, Object> agencySexSum = baseMapper.getAgencySexSum(agencyWorkbenchVO);
        agencySexSum.forEach((k,v)->{
            HashMap<String, Object> mapx = new HashMap<>();
            if (k.equals("asordinarySum")){
                mapx.put("item","普通会员");
                mapx.put("count",new String((byte[])v));
                memberList.add(mapx);
            }else if (k.equals("vipSum")){
                mapx.put("item","vip");
                mapx.put("count",new String((byte[])v));
                memberList.add(mapx);
            }else if (k.equals("memberMan")){
                mapx.put("item","男");
                mapx.put("count",new String((byte[])v));
                memberSexList.add(mapx);
            }else if (k.equals("memberWoMan")){
                mapx.put("item","女");
                mapx.put("count",new String((byte[])v));
                memberSexList.add(mapx);
            }else if (k.equals("memberUnknown")){
                mapx.put("item","未知");
                mapx.put("count",new String((byte[])v));
                memberSexList.add(mapx);
            }
        });
        AgencyWorkbenchVO agencyWorkbenchVO1 = new AgencyWorkbenchVO();
        agencyWorkbenchVO1.setMemberList(memberList);
        agencyWorkbenchVO1.setMemberSexList(memberSexList);
        agencyWorkbenchVO1.setMemberPatch(Long.valueOf(new String((byte[])agencySexSum.get("asordinarySum")))+Long.valueOf(new String((byte[])agencySexSum.get("vipSum"))));
        return agencyWorkbenchVO1;
    }

    @Override
    public Map<String, Long> getSysSexSum(SysWorkbenchVO sysWorkbenchVO) {
        return baseMapper.getSysSexSum(sysWorkbenchVO);
    }

    @Override
    public IPage<MemberListVO> findAgencyMemberList(Page<MemberListVO> page, MemberListVO memberListVO) {
        return baseMapper.findAgencyMemberList(page,memberListVO);
    }

    @Override
    public Map<String, Object> returnPromoter(String id) {
        HashMap<String, Object> map = new HashMap<>();
        MemberList member = this.getById(id);
        if (member.getPromoterType().equals("0")){
            if (StringUtils.isNotBlank(member.getPromoter())){
                StoreManage storeManage = iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                        .eq(StoreManage::getSysUserId,member.getPromoter())
                        .eq(StoreManage::getPayStatus,"1"));
                if (oConvertUtils.isNotEmpty(storeManage)){
                    if (StringUtils.isNotBlank(storeManage.getSubStoreName())){
                        map.put("promoterName",storeManage.getStoreName()+"("+storeManage.getSubStoreName()+")");
                    }else {
                        map.put("promoterName",storeManage.getStoreName());
                    }
                }else {
                    map.put("promoterName","无");
                }
            }
        }
        if (member.getPromoterType().equals("1")){
            MemberList memberList = this.getById(member.getPromoter());
            if (oConvertUtils.isNotEmpty(memberList)){
                map.put("promoterName",memberList.getNickName()+"("+memberList.getPhone()+")");
            }else {
                map.put("promoterName","无");
            }

        }
        if (member.getPromoterType().equals("2")|| StringUtils.isBlank(member.getPromoterType())){
            map.put("promoterName","平台");
        }
        return map;
    }

    @Override
    public Map<String, Object> returnMemberNameById(String promoter, String promoterType) {
        HashMap<String, Object> map = new HashMap<>();
        if (promoterType.equals("1")){
            MemberList member = this.getById(promoter);
            map.put("information",member.getNickName()+"("+member.getPhone()+")");
        }
        if (promoterType.equals("0")){
            if (StringUtils.isNotBlank(promoter)){
                StoreManage storeManage = iStoreManageService.getById(promoter);
                if (StringUtils.isNotBlank(storeManage.getSubStoreName())){
                    map.put("promoter",storeManage.getStoreName()+"("+storeManage.getSubStoreName()+")");
                }else {
                    map.put("promoter",storeManage.getStoreName());
                }
            }
        }
        return map;
    }

    /**
     * 获取用户管理会员数据
     * @param page
     * @param sysUserId
     * @param searchNickNamePhone
     * @return
     */
    @Override
  public  IPage<Map<String,Object>> getMyMemberList(Page<MemberList> page, String sysUserId, String searchNickNamePhone){
      return baseMapper.getMyMemberList(page,sysUserId,searchNickNamePhone );
  }

    @Override
    public AllianceWorkbenchVO getFranchiseeSexSum(AllianceWorkbenchVO workbench) {
        ArrayList<Map<String, Object>> memberList = new ArrayList<>();
        ArrayList<Map<String, Object>> memberSexList = new ArrayList<>();
        Map<String, Object> agencySexSum = baseMapper.getFranchiseeSexSum(workbench);
        agencySexSum.forEach((k,v)->{
            HashMap<String, Object> mapx = new HashMap<>();
            if (k.equals("asordinarySum")){
                mapx.put("item","普通会员");
                mapx.put("count",String.valueOf(v));
                memberList.add(mapx);
            }else if (k.equals("vipSum")){
                mapx.put("item","vip");
                mapx.put("count",String.valueOf(v));
                memberList.add(mapx);
            }else if (k.equals("memberMan")){
                mapx.put("item","男");
                mapx.put("count",String.valueOf(v));
                memberSexList.add(mapx);
            }else if (k.equals("memberWoMan")){
                mapx.put("item","女");
                mapx.put("count",String.valueOf(v));
                memberSexList.add(mapx);
            }else if (k.equals("memberUnknown")){
                mapx.put("item","未知");
                mapx.put("count",String.valueOf(v));
                memberSexList.add(mapx);
            }
        });
        AllianceWorkbenchVO allianceWorkbenchVO = new AllianceWorkbenchVO();
        allianceWorkbenchVO.setMemberList(memberList);
        allianceWorkbenchVO.setMemberSexList(memberSexList);
        allianceWorkbenchVO.setMemberPatch((Long) agencySexSum.get("asordinarySum")+(Long) agencySexSum.get("vipSum"));
        return allianceWorkbenchVO;
    }

    @Override
    public IPage<MemberListVO> findAllianceMemberlist(Page<StoreManageVO> page, MemberListDTO memberListDTO) {
        return baseMapper.findAllianceMemberlist(page,memberListDTO);
    }

    @Override
    public List<Map<String, Object>> likeMemberByPhone(String phone) {
        return baseMapper.likeMemberByPhone(phone);
    }
    /**
     * 查询会员信息(包含删除状态)
     * @param memberListId
     * @return
     */
    @Override
    public   MemberList  getMemberListById(String memberListId){
        return baseMapper.getMemberListById(memberListId);
    }

    @Override
    public MemberListVO findMemberDistributionCountByMemberType(String id) {
        return baseMapper.findMemberDistributionCountByMemberType(id);
    }

    @Override
    public Long findMemberVipByMarketingGiftBag(String id) {
        return baseMapper.findMemberVipByMarketingGiftBag(id);
    }

    @Override
    public MemberListVO findMemberVipByMarketingGiftBagCount(String id) {
        return baseMapper.findMemberVipByMarketingGiftBagCount(id);
    }

    @Override
    public Long findMemberVipByMarketingGiftBagAndStraightPushId(String straightPushId, String id) {
        return baseMapper.findMemberVipByMarketingGiftBagAndStraightPushId(straightPushId,id);
    }

    @Override
    public IPage<Map<String, Object>> getmemberListByTManageId(Page<Map<String, Object>> page, String id,String memberDesignationGroupId) {
        return baseMapper.getmemberListByTManageId(page,id,memberDesignationGroupId);
    }

    /*@Override
    @Transactional
    public boolean designateMember(MemberList levelDownMember, String designateMemberId, String memberId) {
        MemberList memberList = this.getById(memberId);
        MemberList designateMember = this.getById(designateMemberId);
        totalMembersSub(memberList,levelDownMember);
        //修改团队管理关系
        this.saveOrUpdate(levelDownMember
                .setTManageId(designateMember.getId())
                .setIsChange("1")
                .setChangeTime(new Date())
        );
        totalMembersAdd(levelDownMember);
        return true;
    }*/

   /* @Override
    @Transactional
    public void totalMembersSub(MemberList memberList, MemberList levelDownMember) {
        //自己团队网状总人数减一
        this.saveOrUpdate(memberList
                .setTotalMembers(memberList.getTotalMembers().subtract(new BigDecimal(1))));
        LambdaQueryWrapper<MemberDesignationCount> memberDesignationCountLambdaQueryWrapper = new LambdaQueryWrapper<MemberDesignationCount>()
                .eq(MemberDesignationCount::getDelFlag, "0")
                .eq(MemberDesignationCount::getMemberListId, memberList.getId())
                .eq(MemberDesignationCount::getMemberDesignationId, levelDownMember.getMemberDesignationId());
        if (iMemberDesignationCountService.count(memberDesignationCountLambdaQueryWrapper)>0){
            MemberDesignationCount memberDesignationCount = iMemberDesignationCountService.list(memberDesignationCountLambdaQueryWrapper).get(0);
            //对应称号统计减一
            iMemberDesignationCountService.saveOrUpdate(memberDesignationCount
                    .setTotalMembers(memberDesignationCount.getTotalMembers().subtract(new BigDecimal(1))));
        }else {
            iMemberDesignationCountService.save(new MemberDesignationCount()
                    .setDelFlag("0")
                    .setMemberListId(memberList.getId())
                    .setMemberDesignationId(levelDownMember.getMemberDesignationId())
                    .setTotalMembers(new BigDecimal(1))
            );
        }
        String tId = "";
        //是否有团队管理id
        if (StringUtils.isNotBlank(memberList.getTManageId())){
            tId = memberList.getTManageId();
        }

        //获取上级id,封装成队列
        while (StringUtils.isNotBlank(tId)){
            MemberList tMemberList = this.getById(tId);

            this.saveOrUpdate(tMemberList
                    .setTotalMembers(tMemberList.getTotalMembers().subtract(new BigDecimal(1))));
            MemberDesignationCount memberDesignationCount = iMemberDesignationCountService.list(new LambdaQueryWrapper<MemberDesignationCount>()
                    .eq(MemberDesignationCount::getDelFlag, "0")
                    .eq(MemberDesignationCount::getMemberListId, tMemberList.getId())
                    .eq(MemberDesignationCount::getMemberDesignationId, levelDownMember.getMemberDesignationId())
            ).get(0);
            iMemberDesignationCountService.saveOrUpdate(memberDesignationCount
                    .setTotalMembers(memberDesignationCount.getTotalMembers().subtract(new BigDecimal(1))));
            if (StringUtils.isNotBlank(tMemberList.getTManageId())){
                tId = tMemberList.getTManageId();
            }else {
                tId = "";
            }
        }
    }*/

    /*@Override
    @Transactional
    public void totalMembersAdd(MemberList memberList) {
        String tId = "";
        //是否有团队管理id
        if (StringUtils.isNotBlank(memberList.getTManageId())){
            tId = memberList.getTManageId();
        }
        LinkedList<String> strings = new LinkedList<>();

        //获取上级id,封装成队列
        while (StringUtils.isNotBlank(tId)){
            MemberList tMemberList = this.getById(tId);

            this.saveOrUpdate(tMemberList
                    .setTotalMembers(tMemberList.getTotalMembers().add(new BigDecimal(1)))
            );

            List<MemberDesignationCount> memberDesignationCountList = iMemberDesignationCountService.list(new LambdaQueryWrapper<MemberDesignationCount>()
                    .eq(MemberDesignationCount::getDelFlag, "0")
                    .eq(MemberDesignationCount::getMemberDesignationId, memberList.getMemberDesignationId())
                    .eq(MemberDesignationCount::getMemberListId, tMemberList.getId())
            );

            if (memberDesignationCountList.size()>0){
                MemberDesignationCount memberDesignationCount = memberDesignationCountList.get(0);
                iMemberDesignationCountService.saveOrUpdate(memberDesignationCount
                        .setTotalMembers(memberDesignationCount.getTotalMembers().add(new BigDecimal(1))));
            }else {
                iMemberDesignationCountService.save(new MemberDesignationCount()
                        .setDelFlag("0")
                        .setMemberListId(tMemberList.getId())
                        .setMemberDesignationId(memberList.getMemberDesignationId())
                        .setTotalMembers(new BigDecimal(1))
                );
            }


            //封装成LinkedList
            strings.add(tMemberList.getId());

            if (StringUtils.isNotBlank(tMemberList.getTManageId())){
                tId = tMemberList.getTManageId();
            }else {
                tId = "";
            }
        }

        while (strings.size()>0) {
            MemberList member = this.getById(strings.element());
            MemberDesignation memberDesignation = iMemberDesignationService.getById(member.getMemberDesignationId());
            LambdaQueryWrapper<MemberDesignation> memberDesignationLambdaQueryWrapper = new LambdaQueryWrapper<MemberDesignation>()
                    .eq(MemberDesignation::getDelFlag, "0")
                    .eq(MemberDesignation::getStatus, "1");

            if (memberDesignation.getSort().intValue() < 1) {
                memberDesignationLambdaQueryWrapper
                        .gt(MemberDesignation::getSort, "1")
                        .orderByAsc(MemberDesignation::getSort);
            } else {
                memberDesignationLambdaQueryWrapper
                        .gt(MemberDesignation::getSort, memberDesignation.getSort())
                        .orderByAsc(MemberDesignation::getSort);
            }
            if (iMemberDesignationService.count(memberDesignationLambdaQueryWrapper) > 0) {

                MemberDesignation designation = iMemberDesignationService.list(memberDesignationLambdaQueryWrapper).get(0);

                //直推
                if (designation.getDirectReferrals().doubleValue() > 0 && StringUtils.isBlank(designation.getStraightPushId())) {
                    if (member.getTotalMembers().doubleValue() >= designation.getTotalRecommend().doubleValue() &&
                            this.count(new LambdaQueryWrapper<MemberList>()
                                    .eq(MemberList::getDelFlag, "0")
                                    .eq(MemberList::getOldTManageId, member.getId())
                            ) >= designation.getDirectReferrals().doubleValue()&&
                            member.getTotalGiftSales().doubleValue()>=designation.getGiftTotalSales().doubleValue()) {
                        //改变称号
                        this.saveOrUpdate(member.setMemberDesignationId(designation.getId()));
                        if (designation.getIsOpenMoney().equals("0")) {
                            iMemberDesignationService.saveOrUpdate(designation.setIsOpenMoney("1"));
                        }
                        strings.forEach(ss -> {
                            iMemberDesignationService.promoteMemberDesignation(ss, memberDesignation, designation);
                        });
                    }
                }

                //有直推又要团队限制
                if (StringUtils.isNotBlank(designation.getStraightPushId()) && designation.getDirectReferrals().doubleValue() > 0) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("tManageId", member.getId());
                    map.put("memberDesignationId", designation.getStraightPushId());
                    map.put("totalMembers", designation.getStraightPushProple());
                    List<MemberDesignationCountVO> listByMap = iMemberDesignationCountService.getListByMap(map);
                    if (member.getTotalMembers().doubleValue() >= designation.getTotalRecommend().doubleValue() &&
                            this.count(new LambdaQueryWrapper<MemberList>()
                                    .eq(MemberList::getDelFlag, "0")
                                    .eq(MemberList::getOldTManageId, member.getId())
                            ) >= designation.getDirectReferrals().doubleValue() && listByMap.size() >= designation.getTotalTeams().doubleValue()&&
                            member.getTotalGiftSales().doubleValue()>=designation.getGiftTotalSales().doubleValue()) {
                        this.saveOrUpdate(member.setMemberDesignationId(designation.getId()));
                        if (designation.getIsOpenMoney().equals("0")) {
                            iMemberDesignationService.saveOrUpdate(designation.setIsOpenMoney("1"));
                        }
                        strings.forEach(ss -> {
                            iMemberDesignationService.promoteMemberDesignation(ss, memberDesignation, designation);
                        });
                    }
                }

                //团队限制
                if (StringUtils.isNotBlank(designation.getStraightPushId()) && designation.getDirectReferrals().doubleValue() <= 0) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("tManageId", member.getId());
                    map.put("memberDesignationId", designation.getStraightPushId());
                    map.put("totalMembers", designation.getStraightPushProple());
                    List<MemberDesignationCountVO> listByMap = iMemberDesignationCountService.getListByMap(map);
                    if (listByMap.size() >= designation.getTotalTeams().doubleValue()&&
                            member.getTotalGiftSales().doubleValue()>=designation.getGiftTotalSales().doubleValue()) {
                        this.saveOrUpdate(member.setMemberDesignationId(designation.getId()));
                        if (designation.getIsOpenMoney().equals("0")) {
                            iMemberDesignationService.saveOrUpdate(designation.setIsOpenMoney("1"));
                        }
                        strings.forEach(ss -> {
                            iMemberDesignationService.promoteMemberDesignation(ss, memberDesignation, designation);
                        });
                    }
                }
                strings.remove();
            }
        }
    }*/

    /*@Override
    @Transactional
    public void memberListSetTManageId(MemberList memberList, String tMemberId) {
        *//**
         * 绑定称号团队关系条件：0：无；1：需购买礼包；2：需成为vip会员 3:购买vip礼包 这是个双向的条件
         *//*
        String bindingTeamRelationshipCondition = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "binding_team_relationship_condition");

        log.info("绑定团队关系：会员id:"+memberList.getId()+"     推广人会员id:"+tMemberId);
        if (bindingTeamRelationshipCondition.equals("0")){
            if (StringUtils.isBlank(memberList.getOldTManageId())){
                memberList.setOldTManageId(tMemberId);
            }

            this.saveOrUpdate(memberList
                    .setTManageId(tMemberId)
                    .setMemberJoinTime(new Date())
            );
            this.totalMembersAdd(memberList);
        }

        if (bindingTeamRelationshipCondition.equals("1")){

            if (iMarketingGiftBagRecordService.count(new LambdaQueryWrapper<MarketingGiftBagRecord>()
                    .eq(MarketingGiftBagRecord::getDelFlag, "0")
                    .eq(MarketingGiftBagRecord::getPayStatus, "1")
                    .eq(MarketingGiftBagRecord::getMemberListId,tMemberId)
                    .eq(MarketingGiftBagRecord::getTeamPrivileges,"1")
            )>0&&memberList.getIsBuyGift().equals("1")){

                if (StringUtils.isBlank(memberList.getOldTManageId())){
                    memberList.setOldTManageId(tMemberId);
                }
                    this.saveOrUpdate(memberList
                            .setTManageId(tMemberId)
                            .setMemberJoinTime(new Date())
                    );

                    this.totalMembersAdd(memberList);

            }
        }

        if (bindingTeamRelationshipCondition.equals("2")){

            if (this.getById(tMemberId).getMemberType().equals("1")&&memberList.getMemberType().equals("1")){
                if (StringUtils.isBlank(memberList.getOldTManageId())){
                    memberList.setOldTManageId(tMemberId);
                }
                this.saveOrUpdate(memberList
                        .setTManageId(tMemberId)
                        .setMemberJoinTime(new Date())
                );

                this.totalMembersAdd(memberList);
            }
        }

        if (bindingTeamRelationshipCondition.equals("3")){

            MemberList tMember = this.getById(tMemberId);
            int count = iMarketingGiftBagRecordService.count(new LambdaQueryWrapper<MarketingGiftBagRecord>()
                    .eq(MarketingGiftBagRecord::getDelFlag, "0")
                    .eq(MarketingGiftBagRecord::getPayStatus, "1")
                    .eq(MarketingGiftBagRecord::getMemberListId, memberList.getId())
                    .eq(MarketingGiftBagRecord::getVipPrivilege, "1")
            );
            if (iMarketingGiftBagRecordService.count(new LambdaQueryWrapper<MarketingGiftBagRecord>()
                    .eq(MarketingGiftBagRecord::getDelFlag,"0")
                    .eq(MarketingGiftBagRecord::getPayStatus,"1")
                    .eq(MarketingGiftBagRecord::getMemberListId,tMember.getId())
                    .eq(MarketingGiftBagRecord::getVipPrivilege,"1")
                    .eq(MarketingGiftBagRecord::getTeamPrivileges,"1")
            )>0&&count>0){
                if (StringUtils.isBlank(memberList.getOldTManageId())){
                    memberList.setOldTManageId(tMemberId);
                }
                    this.saveOrUpdate(memberList
                            .setTManageId(tMemberId)
                            .setMemberJoinTime(new Date())
                    );
                    this.totalMembersAdd(memberList);
            }
        }
    }*/

    @Override
    public IPage<MemberListVO> memberDesignationPageList(Page<MemberListVO> page, MemberListDTO memberListDTO) {
        return baseMapper.memberDesignationPageList(page,memberListDTO);
    }

    @Override
    public List<MemberListVO> getUnderlingList(String id) {
        return baseMapper.getUnderlingList(id);
    }

    @Override
    public void setPromoter(MemberList memberList, String tMemberId) {
        //查出分销设置
        MarketingDistributionSetting marketingDistributionSetting = iMarketingDistributionSettingService.list(new LambdaQueryWrapper<MarketingDistributionSetting>()
                .eq(MarketingDistributionSetting::getDelFlag, "0")
                .eq(MarketingDistributionSetting::getStatus, "1")).get(0);
        //无门槛
        if (marketingDistributionSetting.getIsThreshold().equals("0")){
            //推广人类型为会员
            memberList.setPromoterType("1");
            memberList.setPromoter(tMemberId);
            if(!this.saveOrUpdate(memberList)){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }

        //需购买礼包
        if (marketingDistributionSetting.getIsThreshold().equals("1")){
            if (iMarketingGiftBagRecordService.count(new LambdaQueryWrapper<MarketingGiftBagRecord>()
                    .eq(MarketingGiftBagRecord::getDelFlag, "0")
                    .eq(MarketingGiftBagRecord::getMemberListId, tMemberId)
                    .eq(MarketingGiftBagRecord::getPayStatus, "1")
                    .eq(MarketingGiftBagRecord::getDistributionPrivileges,"1")
            )>0){
                //推广人类型为会员
                memberList.setPromoterType("1");
                memberList.setPromoter(tMemberId);
                this.saveOrUpdate(memberList);
            }
        }

        //需成为vip会员
        if (marketingDistributionSetting.getIsThreshold().equals("2")){
            MemberList tMemberlist = this.getById(tMemberId);
            if (tMemberlist.getMemberType().equals("1")){
                //推广人类型为会员
                memberList.setPromoterType("1");
                memberList.setPromoter(tMemberId);
                if(!this.saveOrUpdate(memberList)){
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        }
        //购买vip礼包
        if (marketingDistributionSetting.getIsThreshold().equals("3")){
            if (iMarketingGiftBagRecordService.count(new LambdaQueryWrapper<MarketingGiftBagRecord>()
                    .eq(MarketingGiftBagRecord::getDelFlag,"0")
                    .eq(MarketingGiftBagRecord::getMemberListId,tMemberId)
                    .eq(MarketingGiftBagRecord::getVipPrivilege,"1")
                    .eq(MarketingGiftBagRecord::getPayStatus,"1")
                    .eq(MarketingGiftBagRecord::getDistributionPrivileges,"1")
            )>0){
                memberList.setPromoterType("1");
                memberList.setPromoter(tMemberId);
                if(!this.saveOrUpdate(memberList)){
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        }
        log.info("绑定分销关系：会员id:"+memberList.getId()+"     推广人会员id:"+tMemberId);
    }

    @Override
    public List<Map<String, Object>> getDesignateMemberListByPhone(String phone) {
        return baseMapper.getDesignateMemberListByPhone(phone);
    }

    @Override
    public List<MemberList> getMemberDesignationListById(String marketingGiftBagId, String memberDesignationId) {
        return baseMapper.getMemberDesignationListById(marketingGiftBagId,memberDesignationId);
    }

    @Override
    public void setLoginRegister(MemberList memberList,String sysUserId,String tMemberId) {

        //添加会员分享关系
        memberList.setSysUserId(sysUserId);
        //查出分销设置
        MarketingDistributionSetting marketingDistributionSetting = iMarketingDistributionSettingService.getOne(new LambdaQueryWrapper<MarketingDistributionSetting>()
                .eq(MarketingDistributionSetting::getDelFlag, "0")
                .eq(MarketingDistributionSetting::getStatus, "1")
                .last("limit 1"));

        if (marketingDistributionSetting!=null&&StringUtils.isNotBlank(tMemberId)){


            /**
             * 字典名称: system_sharing_model 系统分享模式：0：分享绑定归属店铺；1：分享绑定渠道店铺
             */
            String systemSharingModel= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","system_sharing_model");

            if(systemSharingModel.equals("0")){
                memberList.setSysUserId(this.getById(tMemberId).getSysUserId());
            }

            if(systemSharingModel.equals("1")){
                memberList.setSysUserId(sysUserId);
            }


            log.info("推广人id："+tMemberId+"；推广店铺id："+sysUserId);
            //推广人id不为空
            if (marketingDistributionSetting.getDistributionBuild().equals("0")) {
                this.setPromoter(memberList,tMemberId);

            }
            //称号
            LambdaQueryWrapper<MemberDesignation> memberDesignationLambdaQueryWrapper = new LambdaQueryWrapper<MemberDesignation>()
                    .eq(MemberDesignation::getDelFlag, "0")
                    .eq(MemberDesignation::getStatus, "1")
                    .eq(MemberDesignation::getIsDefault,"1");
            if (iMemberDesignationService.count(memberDesignationLambdaQueryWrapper)>0){
                if (iMemberDesignationMemberListService.count(new LambdaQueryWrapper<MemberDesignationMemberList>()
                        .eq(MemberDesignationMemberList::getDelFlag,"0")
                        .eq(MemberDesignationMemberList::getMemberListId,memberList.getId())
                )<=0){
                    MemberDesignation memberDesignation = iMemberDesignationService
                            .list(memberDesignationLambdaQueryWrapper
                                    .isNull(MemberDesignation::getMemberDesignationGroupId)).get(0);

                    iMemberDesignationMemberListService.save(new MemberDesignationMemberList()
                            .setDelFlag("0")
                            .setMemberListId(memberList.getId())
                            .setMemberDesignationId(memberDesignation.getId())
                            .setMemberJoinTime(new Date())
                    );

                    if (iMemberDesignationCountService.count(new LambdaQueryWrapper<MemberDesignationCount>()
                            .eq(MemberDesignationCount::getDelFlag,"0")
                            .eq(MemberDesignationCount::getMemberDesignationId,memberDesignation.getId())
                            .eq(MemberDesignationCount::getMemberListId,memberList.getId())
                    )<=0){
                        iMemberDesignationCountService.save(new MemberDesignationCount()
                                .setDelFlag("0")
                                .setMemberDesignationId(memberDesignation.getId())
                                .setMemberListId(memberList.getId())
                                .setTotalMembers(new BigDecimal(1))
                        );
                    }
                }
                /*if (StringUtils.isBlank(memberList.getOldTManageId())){
                    this.memberListSetTManageId(memberList,tMemberId);
                }*/
            }

            /*if (StringUtils.isBlank(memberList.getOldTManageId())){
                this.memberListSetTManageId(memberList,tMemberId);
            }*/
        }else {
            if (StringUtils.isNotBlank(sysUserId)) {
                //推广人类型为店铺
                memberList.setPromoterType("0");
                memberList.setPromoter(sysUserId);
            }
        }
        if(!this.saveOrUpdate(memberList)){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public void addShareQr(MemberList memberList,String sysUserId) {
        //增加分享二维码
        if (StringUtils.isBlank(memberList.getSysSmallcodeId())) {
            SysSmallcode sysSmallcode = new SysSmallcode();
            sysSmallcode.setSysUserId(memberList.getSysUserId());

            //判断渠道id归属渠道id
            if (StringUtils.isNotBlank(sysUserId)) {
                String systemSharingModel = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "system_sharing_model");
                if (systemSharingModel.equals("1")) {
                    sysSmallcode.setSysUserId(sysUserId);
                }
            }

            sysSmallcode.setTMemberId(memberList.getId());
            sysSmallcode.setCodeType("1");
            iSysSmallcodeService.save(sysSmallcode);
            memberList.setSysSmallcodeId(sysSmallcode.getId());
            String shareControl= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","share_control");
            String address="";
            if(shareControl.equals("1")){
                address=weixinQRUtils.getQrCode(sysSmallcode.getId());
            }else{
                String shareUrl= notifyUrlUtils.getBseUrl("share_url");
                String param="?tMemberId="+sysSmallcode.getTMemberId()+"&tMemberName="+memberList.getNickName()+"&tMemberHeadportrait="+memberList.getHeadPortrait();
                address=qrCodeUtils.getMemberQrCode(shareUrl+param);
            }
            if(StringUtils.isBlank(address)){
                return;
            }
            sysSmallcode.setAddress(address);
            iSysSmallcodeService.saveOrUpdate(sysSmallcode);
            this.saveOrUpdate(memberList);
        }
    }


    @Override
    @Transactional
    public boolean addBlance(String memberId, BigDecimal balance, String orderNo, String payType) {
        MemberList memberList=this.getById(memberId);

        if(memberList==null){
            return false;
        }

        if(balance.doubleValue()>0){

            log.info("余额增加，会员id："+memberId+";单号："+orderNo+";交易类型："+payType);

            //会员资金加入余额
            memberList.setBalance(memberList.getBalance().add(balance));


            //生成资金流水记录
            MemberAccountCapital memberAccountCapital = new MemberAccountCapital()
                    .setMemberListId(memberId)
                    .setPayType(payType)//代表订单交易
                    .setGoAndCome("0")//支付和收入；0：收入；1：支出
                    .setAmount(balance)
                    .setOrderNo(orderNo)
                    .setBalance(memberList.getBalance());
            //生成资金流水记录
            iMemberAccountCapitalService.save(memberAccountCapital);
            return this.updateById(memberList);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean subtractBlance(String memberId, BigDecimal balance, String orderNo, String payType) {
        MemberList memberList=this.getById(memberId);
        if(memberList==null){
            return false;
        }
        if(balance.doubleValue()>0){

           log.info("余额减少，会员id："+memberId+";单号："+orderNo+";交易类型："+payType);

            //会员资金加入余额
            memberList.setBalance(memberList.getBalance().subtract(balance));

            //生成资金流水记录
            MemberAccountCapital memberAccountCapital = new MemberAccountCapital()
                    .setMemberListId(memberId)
                    .setPayType(payType)//代表订单交易
                    .setGoAndCome("1")//支付和收入；0：收入；1：支出
                    .setAmount(balance)
                    .setOrderNo(orderNo)
                    .setBalance(memberList.getBalance());
            //生成资金流水记录
            iMemberAccountCapitalService.save(memberAccountCapital);
            return this.updateById(memberList);
        }
        return true;
    }

    @Override
    @Transactional
    public void rechargeBalance(String payBalanceLogId) {
        PayBalanceLog payBalanceLog=iPayBalanceLogService.getById(payBalanceLogId);
        payBalanceLog.setPayStatus("1");
        if(!iPayBalanceLogService.saveOrUpdate(payBalanceLog)){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        if(!this.addBlance(payBalanceLog.getMemberListId(),payBalanceLog.getTotalFee(),payBalanceLog.getId(),"18")){
            //手动强制回滚事务，这里一定要第一时间处理
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        String welfareGivenControl= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","welfare_given_control");
        if(StringUtils.isNotBlank(welfareGivenControl)&&welfareGivenControl.equals("1")){
            //获取可获赠金额
            if(!iMemberGiveWelfarePaymentsService.add(payBalanceLog.getMemberListId(),payBalanceLog.getTotalFee(),"2",payBalanceLog.getId())){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
    }

    @Override
    public IPage<Map<String, Object>> pushingNumber(Page<Map<String,Object>> page,String memberId) {
        return baseMapper.pushingNumber(page,memberId);
    }

    @Override
    public int betweenPush(String memberId) {
        return baseMapper.betweenPush(memberId);
    }

}
