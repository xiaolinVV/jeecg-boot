package org.jeecg.modules.alliance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.alliance.dto.AllianceManageDTO;
import org.jeecg.modules.alliance.entity.AllianceManage;
import org.jeecg.modules.alliance.mapper.AllianceManageMapper;
import org.jeecg.modules.alliance.service.IAllianceManageService;
import org.jeecg.modules.alliance.vo.AllianceManageVO;
import org.jeecg.modules.alliance.vo.AllianceWorkbenchVO;
import org.jeecg.modules.member.service.IMemberDesignationGroupService;
import org.jeecg.modules.member.vo.MemberDesignationGroupVO;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 加盟商管理
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
@Service
public class AllianceManageServiceImpl extends ServiceImpl<AllianceManageMapper, AllianceManage> implements IAllianceManageService {
    @Autowired
    @Lazy
    private IStoreManageService iStoreManageService;
    @Autowired
    @Lazy
    private IMemberDesignationGroupService iMemberDesignationGroupService;
    @Override
    public AllianceManageVO findAllianceManageInfo(String id) {
        return baseMapper.findAllianceManageInfo(id);
    }

    @Override
    public IPage<AllianceManageVO> queryPageList(Page<AllianceManage> page, AllianceManageDTO allianceManageDTO) {
        return baseMapper.queryPageList(page,allianceManageDTO);
    }

    @Override
    public AllianceWorkbenchVO findWorkbench(String userId) {
        return baseMapper.findWorkbench(userId);
    }

    @Override
    public List<Map<String, Object>> findAllianceManageByphone(String phone) {
        return baseMapper.findAllianceManageByphone(phone);
    }

    @Override
    public List<Map<String, Object>> getAllianceStoreManage(String id) {
        return baseMapper.getAllianceStoreManage(id);
    }

    @Override
    public Map<String, Object> partnerSum() {
        HashMap<String, Object> objectHashMap = new HashMap<>();
        //获取当前登录人
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //获取加盟商店铺
        List<MemberDesignationGroupVO> partnerSumList = iStoreManageService.getPartnerSum(user.getId());
        //统计全国合伙人总数
        BigDecimal partnerSum = partnerSumList.stream()
                .map(MemberDesignationGroupVO::getTotalMembers)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Map<String, Object>> maps = new ArrayList<>();
        partnerSumList.forEach(psl->{
            HashMap<String, Object> map = new HashMap<>();
            map.put("storeName",psl.getStoreName());
            map.put("number",psl.getTotalMembers().intValue());
            map.put("id",psl.getId());
            //占比
            map.put("numberProportion",psl.getTotalMembers()
                    .divide(partnerSum, 4, BigDecimal.ROUND_DOWN)
                    .multiply(new BigDecimal(100))
                    .stripTrailingZeros()
                    .toPlainString());
            maps.add(map);
        });
        objectHashMap.put("partnerSum",partnerSum);
        objectHashMap.put("storeList",maps);
        return objectHashMap;
    }

    @Override
    public Map<String, Object> toDayPartner() {
        //获取当前登录人
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        HashMap<String, Object> objectHashMap = new HashMap<>();

        String date = DateUtils.getDate("yyyy-MM-dd");

        List<Map<String, Object>> toDayPartnerList = iMemberDesignationGroupService.getToDayPartner(user.getId(), date);

        BigDecimal countSum = toDayPartnerList.stream()
                .map(m -> new BigDecimal(String.valueOf(m.get("countSum"))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        objectHashMap.put("countSum",countSum);
        objectHashMap.put("toDayPartnerList",toDayPartnerList);
        return objectHashMap;
    }

    @Override
    public Map<String, Object> getPerformanceSum() {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<Map<String, Object>> performanceSumList = iMemberDesignationGroupService.getPerformanceSum(user.getId());
        BigDecimal priceSum = performanceSumList.stream()
                .map(m -> new BigDecimal(String.valueOf(m.get("priceSum"))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        performanceSumList.forEach(psl->{
            psl.put("performanceProportion",new BigDecimal(String.valueOf(psl.get("priceSum")))
                    .divide(priceSum,4,BigDecimal.ROUND_DOWN)
                    .multiply(new BigDecimal(100))
                    .stripTrailingZeros()
                    .toPlainString());
        });
        HashMap<String, Object> map = new HashMap<>();
        map.put("priceSum",priceSum);
        map.put("performanceSumList",performanceSumList);
        return map;
    }

    @Override
    public Map<String, Object> getToDayPerformance() {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<Map<String, Object>> toDayPerformanceList = iMemberDesignationGroupService.getToDayPerformance(user.getId());
        BigDecimal priceSum = toDayPerformanceList.stream()
                .map(m -> (BigDecimal) m.get("priceSum"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        HashMap<String, Object> map = new HashMap<>();
        map.put("priceSum",priceSum);
        map.put("toDayPerformanceList",toDayPerformanceList);
        return map;
    }

}
